package pxf.infrastructure.system.task;

import javax.annotation.Nonnull;
import pxf.toolkit.basic.constants.LocaleFindableEnumConstant;
import pxf.toolkit.basic.constants.i18n.LocaleConstantHelper;

/**
 * 系统任务状态
 *
 * @author potatoxf
 * @date 2021/5/6
 */
public enum SystemTaskStatus implements LocaleFindableEnumConstant<SystemTaskStatus> {
  /**
   * 正常
   */
  NORMAL,
  /**
   * 运作中
   */
  RUNNING,
  /**
   * 暂停
   */
  PAUSE,
  /**
   * 异常
   */
  EXCEPTION;

  private static final LocaleConstantHelper LOCALE_CONSTANT_HELPER = new LocaleConstantHelper(
      "toolkit/resources/i18n/common");


  /**
   * 本地化常量助手
   *
   * @return {@code LocaleConstantHelper}
   */
  @Nonnull
  @Override
  public LocaleConstantHelper localeConstantHelper() {
    return LOCALE_CONSTANT_HELPER;
  }
}
