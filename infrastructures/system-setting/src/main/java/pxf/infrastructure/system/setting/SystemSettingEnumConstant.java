package pxf.infrastructure.system.setting;

import javax.annotation.Nonnull;
import pxf.toolkit.basic.annotation.BuiltIn;
import pxf.toolkit.basic.constants.FindablePriorityEnumConstant;

/**
 * 系统设置枚举常量
 *
 * @author potatoxf
 * @date 2021/5/28
 */
@BuiltIn
public interface SystemSettingEnumConstant<
        T extends Enum<T> & SystemSettingEnumConstant<T> & SystemSettingConstant<T>>
    extends SystemSettingConstant<T>, FindablePriorityEnumConstant<T> {

  /**
   * 返回设置类型
   *
   * @return {@code SettingType}
   */
  @Nonnull
  @Override
  default SettingType getSettingType() {
    return enumInstance().getSettingType();
  }

  /**
   * 获取值，当有动态态值则优先返回动态值，其次再返回默认值
   *
   * @return 返回值
   */
  @Override
  default Object getValue() {
    return enumInstance().getValue();
  }

  /**
   * 获取默认值
   *
   * @return 返回默认值
   */
  @Override
  default Object getDefaultValue() {
    return enumInstance().getDefaultValue();
  }

  /**
   * 获取动态值
   *
   * @return 动态值
   */
  @Override
  default Object getDynamicValue() {
    return enumInstance().getDynamicValue();
  }

  /**
   * 获取动态值
   *
   * @return 动态值
   */
  @Override
  default Object[] getDynamicValues() {
    return enumInstance().getDynamicValues();
  }

  /**
   * 设置动态值
   *
   * @param value 设置动态值
   * @return 如果设置成功返回 {@code true}，否则 {@code false}
   */
  @Override
  default boolean setDynamicValue(Object value) {
    return enumInstance().setDynamicValue(value);
  }

  /**
   * 获取当前设置类别
   *
   * @return 返回设置类别
   */
  @Nonnull
  @Override
  default String getCatalog() {
    return enumInstance().getCatalog();
  }
  /**
   * 通过身份牌查找
   *
   * @return 返回唯一身份牌
   */
  @Override
  default int identity() {
    return enumInstance().identity();
  }

  /**
   * 通过唯一名称查找
   *
   * @return 返回名称
   */
  @Override
  default String identityName() {
    return enumInstance().identityName();
  }

  /**
   * 该可查找信息注释信息
   *
   * @return 可查找信息注释信息
   */
  @Override
  default String comment() {
    return enumInstance().comment();
  }

  /** 将缓存里的内容重刷新 */
  @Override
  default void refresh() {
    enumInstance().refresh();
  }

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
  default int compareTo(@Nonnull T other) {
    return enumInstance().compareTo(other.enumInstance());
  }

  /**
   * 持有实例 {@code EnumSystemSettingConstant}
   *
   * @return {@code EnumSystemSettingConstant}
   */
  @Nonnull
  EnumSystemSettingConstant enumInstance();
}
