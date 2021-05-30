package pxf.extsweb.admin.framework.web.entity;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;
import pxf.toolkit.basic.util.Compare;
import pxf.toolkit.basic.util.Is;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
public abstract class AbstractEntity<ENTITY extends AbstractEntity<ENTITY>>
    implements Comparable<ENTITY> {

  private static final String ENTITY_NAME_REF = "ENTITY_NAME";
  private Long id;
  private Integer flag;
  private String updater;
  private Date updateDatetime;
  private String creator;
  private String createDatetime;

  public AbstractEntity() {}

  public AbstractEntity(AbstractEntity<ENTITY> abstractEntity) {
    id = abstractEntity.id;
    flag = abstractEntity.flag;
    updater = abstractEntity.updater;
    updateDatetime = abstractEntity.updateDatetime;
    creator = abstractEntity.creator;
    createDatetime = abstractEntity.createDatetime;
  }

  /**
   * 获取实体名
   *
   * @return 如果不存在返回 {@code null}
   */
  public final String getEntityName() {
    try {
      Field field = getClass().getDeclaredField(ENTITY_NAME_REF);
      if (Is.publicStaticConstants(field) && String.class.equals(field.getType())) {
        return (String) field.get(null);
      }
    } catch (Exception ignored) {
    }
    return null;
  }

  @Override
  public int compareTo(ENTITY o) {
    Integer compareToValue =
        Compare.objectOrComparableForPriorityNull(getUpdateDatetime(), o.getUpdateDatetime());
    if (compareToValue == 0) {
      compareToValue =
          Compare.objectOrComparableForPriorityNull(getCreateDatetime(), o.getCreateDatetime());
    }
    if (compareToValue == 0) {
      compareToValue = Compare.objectOrComparableForPriorityNull(getId(), o.getId());
    }
    return compareToValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractEntity<?> that = (AbstractEntity<?>) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public String getUpdater() {
    return updater;
  }

  public void setUpdater(String updater) {
    this.updater = updater;
  }

  public Date getUpdateDatetime() {
    return updateDatetime;
  }

  public void setUpdateDatetime(Date updateDatetime) {
    this.updateDatetime = updateDatetime;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getCreateDatetime() {
    return createDatetime;
  }

  public void setCreateDatetime(String createDatetime) {
    this.createDatetime = createDatetime;
  }
}
