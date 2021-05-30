package pxf.infrastructure.system.template.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Clob;
import pxf.infrastructure.system.basic.dbentity.DataChangedEntity;

/**
 * @author potatoxf
 * @date 2021/5/12
 */
@TableName("pxf_sys_template_context")
public class SystemTemplateContent extends DataChangedEntity<SystemTemplateContent, Long> {

  /** 模板内容 */
  private Clob content;

  public Clob getContent() {
    return content;
  }

  public void setContent(Clob content) {
    this.content = content;
  }
}
