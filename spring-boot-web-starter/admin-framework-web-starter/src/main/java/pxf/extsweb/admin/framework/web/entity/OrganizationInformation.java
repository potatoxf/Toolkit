package pxf.extsweb.admin.framework.web.entity;

import java.util.Objects;

/**
 * @author potatoxf
 * @date 2021/4/17
 */
public class OrganizationInformation extends AbstractHierarchicalEntity<OrganizationInformation> {
  public static final String ROOT = "ROOT";
  public static final String DEPARTMENT = "DEPARTMENT";
  public static final String ROLE = "ROLE";
  public static final String USER = "USER";
  private String name;
  private String type;
  private Long employeeId;
  private String description;
  private Integer status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    OrganizationInformation that = (OrganizationInformation) o;
    return Objects.equals(getType(), that.getType())
        && Objects.equals(getEmployeeId(), that.getEmployeeId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getType(), getEmployeeId());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
