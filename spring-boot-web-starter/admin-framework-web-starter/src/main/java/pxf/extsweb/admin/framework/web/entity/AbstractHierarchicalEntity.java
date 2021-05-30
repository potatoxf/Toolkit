package pxf.extsweb.admin.framework.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.util.List;
import java.util.Objects;
import pxf.extsweb.admin.framework.system.Sorted;
import pxf.toolkit.basic.lang.TreeLevelStructure;
import pxf.toolkit.basic.lang.TreeLevelStructureBuilder;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
public abstract class AbstractHierarchicalEntity<ENTITY extends AbstractHierarchicalEntity<ENTITY>>
    extends AbstractEntity<ENTITY> implements Sorted<ENTITY>, TreeLevelStructure<Long, ENTITY> {

  @TableField(exist = false)
  private List<ENTITY> children;

  @TableField(exist = false)
  private ENTITY parent;

  private Long parentId;
  private Integer level;
  private Integer sorted;

  public AbstractHierarchicalEntity() {}

  public AbstractHierarchicalEntity(AbstractHierarchicalEntity<ENTITY> abstractHierarchicalEntity) {
    super(abstractHierarchicalEntity);
    parentId = abstractHierarchicalEntity.parentId;
    level = abstractHierarchicalEntity.level;
    sorted = abstractHierarchicalEntity.sorted;
  }

  public static <E extends AbstractHierarchicalEntity<E>> List<E> buildTree(List<E> data) {
    return buildTree(data, 0);
  }

  public static <E extends AbstractHierarchicalEntity<E>> List<E> buildTree(
      List<E> data, int startLevel) {
    return new TreeLevelStructureBuilder<Long, E>().buildTreeByLevel(data, startLevel);
  }

  @Override
  public ENTITY getParent() {
    return parent;
  }

  @Override
  public void setParent(ENTITY parent) {
    this.parent = parent;
  }

  @Override
  public int sortedValue() {
    if (sorted == null) {
      return 0;
    }
    return sorted;
  }

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
    AbstractHierarchicalEntity<?> that = (AbstractHierarchicalEntity<?>) o;
    return Objects.equals(getParentId(), that.getParentId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getParentId());
  }

  @Override
  public int level() {
    Integer level = getLevel();
    return level == null ? 0 : level;
  }

  @Override
  public Long currentReference() {
    return getId();
  }

  @Override
  public Long parentReference() {
    return getParentId();
  }

  @Override
  public List<ENTITY> getChildren() {
    return children;
  }

  @Override
  public void setChildren(List<ENTITY> children) {
    this.children = children;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Integer getSorted() {
    return sorted;
  }

  public void setSorted(Integer sorted) {
    this.sorted = sorted;
  }
}
