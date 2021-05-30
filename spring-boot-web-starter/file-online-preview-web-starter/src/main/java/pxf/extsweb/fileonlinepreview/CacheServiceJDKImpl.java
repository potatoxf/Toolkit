package pxf.extsweb.fileonlinepreview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import pxf.toolkit.basic.util.Is;

@Service
@ConditionalOnExpression("'${cache.type:default}'.equals('jdk')")
public class CacheServiceJDKImpl implements CacheService {

  private static final int QUEUE_SIZE = 500000;
  private final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
  private Map<String, String> pdfCache;
  private Map<String, List<String>> imgCache;
  private Map<String, Integer> pdfImagesCache;

  @PostConstruct
  public void initCache() {
    initPDFCachePool(DEFAULT_PDF_CAPACITY);
    initIMGCachePool(DEFAULT_IMG_CAPACITY);
    initPdfImagesCachePool(DEFAULT_PDFIMG_CAPACITY);
  }

  @Override
  public void putPDFCache(String key, String value) {
    pdfCache.put(key, value);
  }

  @Override
  public void putImgCache(String key, List<String> value) {
    imgCache.put(key, value);
  }

  @Override
  public Map<String, String> getPDFCache() {
    return pdfCache;
  }

  @Override
  public String getPDFCache(String key) {
    return pdfCache.get(key);
  }

  @Override
  public Map<String, List<String>> getImgCache() {
    return imgCache;
  }

  @Override
  public List<String> getImgCache(String key) {
    if (Is.empty(key)) {
      return new ArrayList<>();
    }
    return imgCache.get(key);
  }

  @Override
  public Integer getPdfImageCache(String key) {
    return pdfImagesCache.get(key);
  }

  @Override
  public void putPdfImageCache(String pdfFilePath, int num) {
    pdfImagesCache.put(pdfFilePath, num);
  }

  @Override
  public void cleanCache() {
    initPDFCachePool(DEFAULT_PDF_CAPACITY);
    initIMGCachePool(DEFAULT_IMG_CAPACITY);
    initPdfImagesCachePool(DEFAULT_PDFIMG_CAPACITY);
  }

  @Override
  public void addQueueTask(String url) {
    blockingQueue.add(url);
  }

  @Override
  public String takeQueueTask() throws InterruptedException {
    return blockingQueue.take();
  }

  @Override
  public void initPDFCachePool(Integer capacity) {
    pdfCache = new ConcurrentHashMap<>(capacity);
  }

  @Override
  public void initIMGCachePool(Integer capacity) {
    imgCache = new ConcurrentHashMap<>(capacity);
  }

  @Override
  public void initPdfImagesCachePool(Integer capacity) {
    pdfImagesCache = new ConcurrentHashMap<>(capacity);
  }
}
