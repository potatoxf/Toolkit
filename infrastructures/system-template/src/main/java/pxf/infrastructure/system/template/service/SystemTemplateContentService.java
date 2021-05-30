package pxf.infrastructure.system.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import pxf.infrastructure.system.template.entity.SystemTemplateContent;

/**
 * 系统模板Mapper
 *
 * @author potatoxf
 * @date 2021/5/12
 */
@Mapper
public interface SystemTemplateContentService extends IService<SystemTemplateContent> {

  /**
   * 获取上次更新时间戳
   *
   * @param id ID
   * @return {@code long}
   */
  long getLastUpdateTimestampById(Long id);
}
