package pxf.toolkit.extension.template;

import java.util.Map;
import pxf.toolkit.basic.exception.ParseException;

/**
 * 字符串模板解析器
 *
 * @author potatoxf
 * @date 2021/4/19
 */
public interface StringTemplateResolver {

  /**
   * 处理字符串模板解析
   *
   * <p>主要用于字符串变量替换，具体语法参见解析器的支持情况
   *
   * @param template 模板
   * @param container 容器
   * @return 处理后的字符串
   */
  String handle(String template, Map container) throws ParseException;
}
