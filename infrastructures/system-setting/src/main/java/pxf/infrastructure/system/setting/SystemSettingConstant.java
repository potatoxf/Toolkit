package pxf.infrastructure.system.setting;

import javax.annotation.Nonnull;
import pxf.infrastructure.system.setting.entity.SystemSettingIndex;
import pxf.toolkit.basic.annotation.BuiltIn;
import pxf.toolkit.basic.constants.FindableConstant;
import pxf.toolkit.basic.function.Refreshable;
import pxf.toolkit.basic.util.Compare;

/**
 * 系统设置常量
 *
 * @author potatoxf
 * @date 2021/5/28
 */
@BuiltIn
public interface SystemSettingConstant<T extends SystemSettingConstant<T>>
    extends FindableConstant<T>, Comparable<T>, Refreshable {

  /**
   * 返回设置类型
   *
   * @return {@code SettingType}
   */
  @Nonnull
  SettingType getSettingType();

  /**
   * 获取值，当有动态态值则优先返回动态值，其次再返回默认值
   *
   * @return 返回值
   */
  Object getValue();

  /**
   * 获取默认值
   *
   * @return 返回默认值
   */
  Object getDefaultValue();

  /**
   * 获取动态值
   *
   * @return 动态值
   */
  Object getDynamicValue();
  /**
   * 获取动态值
   *
   * @return 动态值
   */
  Object[] getDynamicValues();

  /**
   * 设置动态值
   *
   * @param value 设置动态值
   * @return 如果设置成功返回 {@code true}，否则 {@code false}
   */
  boolean setDynamicValue(Object value);

  /**
   * 获取当前设置类别
   *
   * @return 返回设置类别
   */
  @Nonnull
  String getCatalog();

  /**
   * Compares this object with the specified object for order. Returns a negative integer, zero, or
   * a positive integer as this object is less than, equal to, or greater than the specified object.
   *
   * <p>The implementor must ensure {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))} for all
   * {@code x} and {@code y}. (This implies that {@code x.compareTo(y)} must throw an exception iff
   * {@code y.compareTo(x)} throws an exception.)
   *
   * <p>The implementor must also ensure that the relation is transitive: {@code (x.compareTo(y) > 0
   * && y.compareTo(z) > 0)} implies {@code x.compareTo(z) > 0}.
   *
   * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0} implies that {@code
   * sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for all {@code z}.
   *
   * <p>It is strongly recommended, but <i>not</i> strictly required that {@code (x.compareTo(y)==0)
   * == (x.equals(y))}. Generally speaking, any class that implements the {@code Comparable}
   * interface and violates this condition should clearly indicate this fact. The recommended
   * language is "Note: this class has a natural ordering that is inconsistent with equals."
   *
   * <p>In the foregoing description, the notation {@code sgn(}<i>expression</i>{@code )} designates
   * the mathematical <i>signum</i> function, which is defined to return one of {@code -1}, {@code
   * 0}, or {@code 1} according to whether the value of <i>expression</i> is negative, zero, or
   * positive, respectively.
   *
   * @param other the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException if the specified object's type prevents it from being compared to
   *     this object.
   */
  @Override
  default int compareTo(T other) {
    int cp = Compare.objectOrComparableForPriorityNull(getModule(), other.getModule());
    if (cp == 0) {
      cp = Compare.objectOrComparableForPriorityNull(getClazz(), other.getClazz());
      if (cp == 0) {
        cp = Compare.objectOrComparableForPriorityNull(getCatalog(), other.getCatalog());
        if (cp == 0) {
          cp = Compare.objectOrComparableForPriorityNull(getName(), other.getName());
        }
      }
    }
    return cp;
  }

  /**
   * 获取当前设置模块名
   *
   * @return 返回设置包名
   */
  @Nonnull
  default String getModule() {
    return getClass().getPackageName();
  }

  /**
   * 获取当前设置类名
   *
   * @return 返回设置类名
   */
  @Nonnull
  default String getClazz() {
    return getClass().getSimpleName();
  }

  /**
   * 获取当前设置名称
   *
   * @return 返回设置名称
   */
  @Nonnull
  default String getName() {
    return identityName();
  }

  /**
   * 转成索引
   *
   * @return {@code SystemSettingIndex}
   */
  default SystemSettingIndex toIndex() {
    SystemSettingIndex index = new SystemSettingIndex();
    index.setModule(getModule());
    index.setClazz(getClazz());
    index.setCatalog(getCatalog());
    index.setName(getName());
    return index;
  }

  /**
   * 获取管理服务
   *
   * @return {@code SystemSettingManagerService}
   */
  default SystemSettingManagerService getSystemSettingManagerService() {
    return SystemSettingManagerService.getGlobalInstanceHolder();
  }
}
