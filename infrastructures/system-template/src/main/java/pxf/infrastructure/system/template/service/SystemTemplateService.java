package pxf.infrastructure.system.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import pxf.infrastructure.system.template.entity.SystemTemplate;

/**
 * 系统模板Mapper
 *
 * @author potatoxf
 * @date 2021/5/12
 */
@Mapper
public interface SystemTemplateService extends IService<SystemTemplate> {

  /**
   * 是否存在模板
   *
   * @param code 模板代码
   * @return 如果存在返回 {@code true}，否则 {@code false}
   */
  boolean isExistTemplate(@Nullable String code);
  /**
   * 获取模板通过代码
   *
   * @param code 代码
   * @return {@code Clob}
   */
  @Nullable
  SystemTemplate searchByCode(@Nonnull String code);
}
