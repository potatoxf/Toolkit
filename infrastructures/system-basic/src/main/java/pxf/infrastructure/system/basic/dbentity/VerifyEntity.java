package pxf.infrastructure.system.basic.dbentity;

/**
 * 验证实体类
 *
 * @author potatoxf
 * @date 2021/5/14
 */
interface VerifyEntity {

  /**
   * 获取内容
   *
   * @return 内容
   */
  String getContent();

  /**
   * 获取摘要
   *
   * @return 摘要
   */
  String getDigest();
}
