package pxf.infrastructure.system.basic.dbentity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 检测数据变化实体，一般用于较大的字段检测
 *
 * @author potatoxf
 * @date 2021/3/21
 */
public abstract class DataVerifyEntity<
        SUB extends DataVerifyEntity<SUB, ID>, ID extends Number & Comparable<ID>>
    extends DataChangedEntity<SUB, ID> implements VerifyEntity {

  /** 内容 */
  @TableField(
      condition = SqlCondition.LIKE,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String content;

  /** 信息摘要 */
  @TableField(
      fill = FieldFill.INSERT_UPDATE,
      condition = SqlCondition.EQUAL,
      insertStrategy = FieldStrategy.NOT_EMPTY,
      updateStrategy = FieldStrategy.NOT_EMPTY)
  private String digest;

  @Override
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String getDigest() {
    return digest;
  }

  public void setDigest(String digest) {
    this.digest = digest;
  }

  public static class Query {

    /** 内容 */
    private String content;

    /** 信息摘要 */
    private String digest;

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getDigest() {
      return digest;
    }

    public void setDigest(String digest) {
      this.digest = digest;
    }
  }
}
