package pxf.extsweb.admin.framework.web.entity;

import pxf.toolkit.basic.constants.FindableEnumConstant;

/**
 * @author potatoxf
 * @date 2021/4/18
 */
public class OrganizationAuthorityInformation
    extends AbstractHierarchicalEntity<OrganizationAuthorityInformation> {

  public static final String AUTHORITY_TYPE_READ = "READ";
  public static final String AUTHORITY_TYPE_WRITE = "WRIT";
  /**
   * 授权对象ID
   *
   * @see EmployeeInformation#getId()
   */
  private Long authorityId;
  /** 拒绝标识 */
  private Integer reject;
  /** 权限类型 */
  private String authorityType;
  /** 有权限对象的ID */
  private Long targetId;
  /** 有权限对象 {@code ID} */
  private String targetEntityName;
  /** 有权限目标 */
  private String target;

  public enum Type implements FindableEnumConstant<Type> {
    READ,
    WRITE,
    EXPORT;
  }
}
