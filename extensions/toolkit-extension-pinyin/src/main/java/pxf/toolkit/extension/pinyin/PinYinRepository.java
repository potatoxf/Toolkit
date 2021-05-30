package pxf.toolkit.extension.pinyin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.ThreadSafe;
import pxf.toolkit.basic.concurrent.locks.PerformanceLock;
import pxf.toolkit.basic.exception.AbnormalException;
import pxf.toolkit.basic.util.Cast;

/**
 * 拼音仓库
 *
 * @author potatoxf
 * @date 2021/4/29
 */
@ThreadSafe
class PinYinRepository {
  public static final PinYinRepository CHINESE_4E00_9FA5 =
      new PinYinRepository(9, 0x4E00, 0x9FA5, "#\n");
  private static final String PATH_PREFIX = "pxf/toolkit/extension/pinyin/pinyin/";
  private static final String POLY_SUFFIX = "*";
  private final int lineBytes;
  private final int minUnicode;
  private final int maxUnicode;
  private final String trimChars;
  private final PerformanceLock monoLock = new PerformanceLock();
  private final PerformanceLock polyLock = new PerformanceLock();
  private List<String> monoData;
  private Map<String, PinYin> polyData;

  private PinYinRepository(
      final int lineBytes, final int minUnicode, final int maxUnicode, String trimChars) {
    this.lineBytes = lineBytes;
    this.minUnicode = minUnicode;
    this.maxUnicode = maxUnicode;
    this.trimChars = trimChars;
  }

  /**
   * 添加多音字
   *
   * @param codepoint 码点
   * @param pinYin 拼音
   */
  public void addPoly(int codepoint, PinYin pinYin) {
    if (pinYin == null) {
      return;
    }
    initPloyContainer();
    polyData.put(Cast.hexCodePoint(codepoint), pinYin);
  }

  public void addPoly(Map<String, PinYin> polyMap) {
    if (polyMap == null || polyMap.isEmpty()) {
      return;
    }
    initPloyContainer();
    polyData.putAll(polyMap);
  }

  public boolean isSupportPinYin(char c) {
    return c >= minUnicode && c <= maxUnicode;
  }

  public PinYin gainPinYin(char c) {
    checkChar(c);
    return loadPinYin(c);
  }

  protected void checkChar(char c) {
    if (c < minUnicode) {
      throw new IllegalArgumentException(
          "The char must greater " + Integer.toHexString(minUnicode) + " and equal in unicode");
    }
    if (c > maxUnicode) {
      throw new IllegalArgumentException(
          "The char must lesser " + Integer.toHexString(maxUnicode) + " and equal in unicode");
    }
  }

  private PinYin loadPinYin(int codepoint) {
    final int readAmount = codepoint - minUnicode + 1;
    if (!isFinishMonoLoad()) {
      monoLock.writeLock(true);
      if (monoData == null) {
        monoData = new ArrayList<>(readAmount);
      }
      monoLock.writeLock(false);
      monoLock.readLock(true);
      int cacheCount = monoData.size();
      monoLock.readLock(false);
      if (readAmount > cacheCount) {
        readMonoResource(cacheCount, readAmount);
      }
    } else {
      // 加载已经完成，废弃锁
      monoLock.abandon();
    }
    monoLock.readLock(true);
    String word = monoData.get(readAmount - 1);
    monoLock.readLock(false);
    if (word.endsWith(POLY_SUFFIX)) {
      initPloyContainer();
      if (isFinishPolyLoad()) {
        polyLock.abandon();
      } else {
        polyLock.writeLock(true);
        readPolyResource();
        polyLock.writeLock(false);
      }
      polyLock.readLock(true);
      PinYin pinYin = polyData.get(Cast.hexCodePoint(codepoint));
      polyLock.readLock(false);
      return new PinYin(pinYin, word).distinct();
    }
    return new PinYin(word);
  }

  private void initPloyContainer() {
    polyLock.writeLock(true);
    if (polyData == null) {
      polyData = new HashMap<>(2100, 1);
    }
    polyLock.writeLock(false);
  }

  private void readMonoResource(final int cacheCount, final int readCount) {
    InputStream inputStream = null;
    try {
      final int totalReadLine = readCount - cacheCount;
      if (totalReadLine <= 0) {
        return;
      }
      String monoResourcePath = getMonoResourcePath();
      inputStream = ClassLoader.getSystemResourceAsStream(monoResourcePath);
      if (inputStream == null) {
        throw new AbnormalException();
      }
      long needSkip = cacheCount * lineBytes;
      long skip = inputStream.skip(needSkip);
      if (skip == needSkip) {
        byte[] bytes = new byte[lineBytes];
        StringBuilder sb = new StringBuilder(lineBytes);
        monoLock.writeLock(true);
        for (int i = 0; i < totalReadLine; i++) {
          int len = inputStream.read(bytes);
          if (len != lineBytes) {
            throw new IllegalArgumentException(
                "Hope the length of each line is "
                    + lineBytes
                    + "and the length of appearance is"
                    + len);
          }
          String string = new String(bytes, StandardCharsets.UTF_8);
          for (int j = 0; j < string.length(); j++) {
            char c = string.charAt(j);
            if (trimChars.indexOf(c) != -1) {
              continue;
            }
            sb.append(c);
          }
          monoData.add(sb.toString());
          sb.delete(0, lineBytes);
        }
        monoLock.writeLock(false);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private String getMonoResourcePath() {
    return PATH_PREFIX
        + (Integer.toHexString(minUnicode) + "-" + Integer.toHexString(maxUnicode)).toUpperCase();
  }

  private void readPolyResource() {
    LineNumberReader lineNumberReader = null;
    try {
      String polyResourcePath = getPolyResourcePath();
      InputStream inputStream = ClassLoader.getSystemResourceAsStream(polyResourcePath);
      if (inputStream == null) {
        throw new AbnormalException();
      }
      lineNumberReader = new LineNumberReader(new InputStreamReader(inputStream));
      String line;
      while ((line = lineNumberReader.readLine()) != null) {
        String[] tokenize = line.split(" ");
        if (tokenize.length == 1) {
          throw new AbnormalException("The poly must have correct format");
        }
        String[] strings = tokenize[1].split(",");
        PinYin alphabetic = new PinYin(strings);
        polyData.put(tokenize[0], alphabetic);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (lineNumberReader != null) {
        try {
          lineNumberReader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private String getPolyResourcePath() {
    return getMonoResourcePath() + "-MULTI";
  }

  private boolean isFinishMonoLoad() {
    monoLock.readLock(true);
    boolean b = monoData != null && monoData.size() == getTotalSize();
    monoLock.readLock(false);
    return b;
  }

  private int getTotalSize() {
    return maxUnicode - minUnicode;
  }

  private boolean isFinishPolyLoad() {
    polyLock.readLock(true);
    boolean b = polyData != null && !polyData.isEmpty();
    polyLock.readLock(false);
    return b;
  }
}
