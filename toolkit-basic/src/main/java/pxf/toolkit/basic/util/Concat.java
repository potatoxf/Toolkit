package pxf.toolkit.basic.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 连接操作
 *
 * @author potatoxf
 * @date 2021/4/22
 */
public class Concat {

  private Concat() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 连接任意对象
   *
   * @param input 输入对象
   * @param delimiter 分割符号
   * @return 返回连接后的字符串
   */
  @Nonnull
  public static String any(@Nullable Object input, @Nullable Object delimiter) {
    return Concat.any(input, delimiter, null, null, null, true, String::valueOf);
  }

  /**
   * 连接任意对象
   *
   * @param input 输入对象
   * @param delimiter 分割符号
   * @param tokenPrefix token前缀
   * @param tokenSuffix token后缀
   * @param defaultTokenValue 默认token
   * @param isIgnoredNull 是否忽略null
   * @param elementHandler 元素处理
   * @return 返回连接后的字符串
   */
  @Nonnull
  public static String any(
      @Nullable Object input,
      @Nullable Object delimiter,
      @Nullable Object tokenPrefix,
      @Nullable Object tokenSuffix,
      @Nullable Object defaultTokenValue,
      boolean isIgnoredNull,
      @Nullable Function<Object, String> elementHandler) {
    final String dtv = isIgnoredNull ? null : Valid.string(defaultTokenValue, Const.NULL_STRING);
    if (input == null) {
      if (dtv != null) {
        return dtv;
      }
      return Empty.STRING;
    }
    final String d = Valid.string(delimiter, Const.DEFAULT_DELIMITER);
    final String tp = Valid.string(tokenPrefix, Empty.STRING);
    final String ts = Valid.string(tokenSuffix, Empty.STRING);
    final Function<Object, String> eh = Valid.object(elementHandler, Object::toString);
    StringBuilder sb = new StringBuilder();
    anyRecursion(sb, input, d, tp, ts, dtv, eh);
    sb.setLength(sb.length() - d.length());
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private static void anyRecursion(
      @Nonnull StringBuilder container,
      @Nullable Object input,
      @Nonnull String delimiter,
      @Nonnull String tokenPrefix,
      @Nonnull String tokenSuffix,
      @Nullable String defaultTokenValue,
      @Nonnull Function<Object, String> elementHandler) {
    if (input == null) {
      if (defaultTokenValue != null) {
        container.append(tokenPrefix);
        container.append(defaultTokenValue);
        container.append(tokenSuffix);
        container.append(delimiter);
      }
      return;
    }
    if (input instanceof Collection) {
      for (Object value : (Collection) input) {
        anyRecursion(
            container,
            value,
            delimiter,
            tokenPrefix,
            tokenSuffix,
            defaultTokenValue,
            elementHandler);
      }
    } else if (input instanceof Iterable) {
      Iterable iterable = (Iterable) input;
      for (Object value : iterable) {
        anyRecursion(
            container,
            value,
            delimiter,
            tokenPrefix,
            tokenSuffix,
            defaultTokenValue,
            elementHandler);
      }
    } else if (input instanceof Iterator) {
      Iterator iterator = (Iterator) input;
      while (iterator.hasNext()) {
        anyRecursion(
            container,
            iterator.next(),
            delimiter,
            tokenPrefix,
            tokenSuffix,
            defaultTokenValue,
            elementHandler);
      }
    } else if (input instanceof Map) {
      Set<Map.Entry> set = ((Map) input).entrySet();
      for (Map.Entry value : set) {
        anyRecursion(
            container,
            value.getKey(),
            delimiter,
            tokenPrefix,
            tokenSuffix,
            defaultTokenValue,
            elementHandler);
        anyRecursion(
            container,
            value.getValue(),
            delimiter,
            tokenPrefix,
            tokenSuffix,
            defaultTokenValue,
            elementHandler);
      }
    } else if (input.getClass().isArray()) {
      int length = Array.getLength(input);
      for (int i = 0; i < length; i++) {
        anyRecursion(
            container,
            Array.get(input, i),
            delimiter,
            tokenPrefix,
            tokenSuffix,
            defaultTokenValue,
            elementHandler);
      }
    } else {
      String value = elementHandler.apply(input);
      container.append(tokenPrefix);
      container.append(value);
      container.append(tokenSuffix);
      container.append(delimiter);
    }
  }
}
