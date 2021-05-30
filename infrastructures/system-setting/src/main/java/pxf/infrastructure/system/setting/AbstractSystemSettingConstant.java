package pxf.infrastructure.system.setting;

import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.infrastructure.system.setting.entity.SystemSetting;
import pxf.toolkit.basic.annotation.BuiltIn;
import pxf.toolkit.basic.constants.AbstractFindableConstant;

/**
 * 抽象系统设置常量，所有系统常量设置都实现该类
 *
 * @author potatoxf
 * @date 2021/5/28
 */
@BuiltIn
public abstract class AbstractSystemSettingConstant
    extends AbstractFindableConstant<AbstractSystemSettingConstant>
    implements SystemSettingConstant<AbstractSystemSettingConstant> {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractSystemSettingConstant.class);
  /** 设置类型 */
  private final SettingType settingType;
  /** 分类 */
  private final String catalog;
  /** 默认值 */
  private final Object defaultValue;
  /** 模块名 */
  private String module;
  /** 类名 */
  private String clazz;
  /** 缓存值 */
  private volatile Object cacheValue;
  /** 缓存值 */
  private volatile Object[] cacheValues;
  /** 缓存系统设置 */
  private volatile SystemSetting cacheSystemSetting;

  protected AbstractSystemSettingConstant(
      String catalog,
      int identity,
      String identityName,
      String comment,
      SettingType settingType,
      Object defaultValue) {
    super(identity, identityName, comment);
    this.settingType =
        Objects.requireNonNull(settingType, "The Setting type is not allowed to be empty");
    this.catalog = Objects.requireNonNull(catalog, "The Setting type is not allowed to be empty");
    this.defaultValue = checkDefaultValue(defaultValue);
  }

  private Object checkDefaultValue(Object defaultValue) {
    if (defaultValue instanceof Supplier) {
      if (settingType.isSupportResultType(((Supplier) defaultValue).get())) {
        return defaultValue;
      }
    }
    if (settingType.isSupportResultType(defaultValue)) {
      return defaultValue;
    }
    throw new IllegalArgumentException(
        this + "The default value only supports [" + settingType.supportResultType() + "]");
  }

  /**
   * 返回设置类型
   *
   * @return {@code SettingType}
   */
  @Nonnull
  @Override
  public final SettingType getSettingType() {
    return settingType;
  }

  /**
   * 获取值，当有动态态值则优先返回动态值，其次再返回默认值
   *
   * @return 返回值
   */
  @Override
  public final Object getValue() {
    if (getSystemSettingManagerService().hasValue(this)) {
      return getDynamicValue();
    }
    return getDefaultValue();
  }

  /**
   * 获取默认值
   *
   * @return 返回默认值
   */
  @Override
  public final Object getDefaultValue() {
    if (defaultValue instanceof Supplier) {
      return ((Supplier) defaultValue).get();
    }
    return defaultValue;
  }

  /**
   * 获取动态值
   *
   * @return 动态值
   */
  @Override
  public final Object getDynamicValue() {
    if (cacheValue == null) {
      String value;
      if (cacheSystemSetting != null) {
        value = cacheSystemSetting.getValue();
      } else {
        value = getSystemSettingManagerService().getValue(this);
      }
      synchronized (this) {
        if (cacheValue == null) {
          cacheValue = settingType.parseValue(value);
        }
      }
    }
    return cacheValue;
  }

  /**
   * 获取动态值
   *
   * @return 动态值
   */
  @Override
  public final Object[] getDynamicValues() {
    if (cacheValues == null) {
      String value;
      if (cacheSystemSetting != null) {
        value = cacheSystemSetting.getValue();
      } else {
        value = getSystemSettingManagerService().getValue(this);
      }
      synchronized (this) {
        if (cacheValues == null) {
          cacheValues = settingType.parseValues(value);
        }
      }
    }
    return cacheValues;
  }

  /**
   * 设置动态值
   *
   * @param value 设置动态值
   * @return 如果设置成功返回 {@code true}，否则 {@code false}
   */
  @Override
  public final boolean setDynamicValue(Object value) {
    if (settingType.isSupportResultType(value)) {
      return getSystemSettingManagerService().setValue(this, value);
    }
    return false;
  }

  /** 将缓存里的内容重刷新 */
  @Override
  public synchronized void refresh() {
    cacheValue = null;
    cacheValues = null;
    cacheSystemSetting = null;
  }

  /**
   * 获取当前设置模块名
   *
   * @return 返回设置包名
   */
  @Nonnull
  @Override
  public final String getModule() {
    if (module == null) {
      module = getClass().getPackageName();
    }
    return module;
  }
  /**
   * 获取当前设置类名
   *
   * @return 返回设置类名
   */
  @Nonnull
  @Override
  public final String getClazz() {
    if (clazz == null) {
      clazz = getClass().getSimpleName();
    }
    return clazz;
  }
  /**
   * 获取当前设置名称
   *
   * @return 返回设置名称
   */
  @Nonnull
  @Override
  public final String getName() {
    return identityName();
  }
  /**
   * 获取当前设置类别
   *
   * @return 返回设置类别
   */
  @Nonnull
  @Override
  public final String getCatalog() {
    return catalog;
  }

  @Override
  public final boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    AbstractSystemSettingConstant that = (AbstractSystemSettingConstant) other;
    return Objects.equals(getModule(), that.getModule())
        && Objects.equals(getClazz(), that.getClazz())
        && Objects.equals(getCatalog(), that.getCatalog())
        && Objects.equals(getName(), that.getName());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getModule(), getClazz(), getCatalog(), getName());
  }

  @Override
  public final String toString() {
    return getClass().getName() + "[" + getCatalog() + " : " + getName() + "]";
  }

  public synchronized SystemSetting getCacheSystemSetting() {
    return cacheSystemSetting;
  }

  public synchronized void setCacheSystemSetting(SystemSetting cacheSystemSetting) {
    this.cacheSystemSetting = cacheSystemSetting;
  }
}
