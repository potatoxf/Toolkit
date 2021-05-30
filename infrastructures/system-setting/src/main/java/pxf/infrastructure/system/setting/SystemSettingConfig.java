package pxf.infrastructure.system.setting;

/**
 * 系统设置配置
 *
 * @author potatoxf
 * @date 2021/5/30
 */
public class SystemSettingConfig {

  /** 扫描包 */
  private String[] scanningPackages;

  public String[] getScanningPackages() {
    return scanningPackages;
  }

  public void setScanningPackages(String[] scanningPackages) {
    this.scanningPackages = scanningPackages;
  }
}
