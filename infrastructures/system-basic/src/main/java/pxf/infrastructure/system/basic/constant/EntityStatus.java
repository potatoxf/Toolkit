package pxf.infrastructure.system.basic.constant;

import pxf.toolkit.basic.constants.FindableEnumConstant;

/**
 * 实体状态
 *
 * <p>该实体列出来10种内置状态，数据库中实体的状态使用
 *
 * @author potatoxf
 * @date 2021/5/14
 */
public enum EntityStatus implements FindableEnumConstant<EntityStatus> {
  /** 正常 */
  NORMAL(0),
  /** 只读 */
  ONLY_READ(1),
  /** 只写 */
  ONLY_WRITE(2),
  /** 限时 */
  LIMITED_TIME(3),
  /** 被锁上 */
  LOCKED(4),
  // 5-9 不正常
  /** 冲突 */
  CONFLICT(5),
  /** 过期 */
  EXPIRED(6),
  /** 人为 */
  ARTIFICIAL(7),
  /** 被移除 */
  REMOVED(8),
  /** 损毁 */
  DAMAGE(9);

  private final int status;

  EntityStatus(int status) {
    this.status = status;
  }

  /**
   * 通过身份牌查找
   *
   * @return 返回唯一身份牌
   */
  @Override
  public int identity() {
    return status;
  }
}
