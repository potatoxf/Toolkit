package pxf.infrastructure.system.setting;

import pxf.toolkit.basic.annotation.BuiltIn;

/**
 * @author potatoxf
 * @date 2021/5/30
 */
@BuiltIn
public class EnumSystemSettingConstant extends AbstractSystemSettingConstant {

  public EnumSystemSettingConstant(
      String catalog,
      int identity,
      String identityName,
      String comment,
      SettingType settingType,
      Object defaultValue) {
    super(catalog, identity, identityName, comment, settingType, defaultValue);
  }
}
