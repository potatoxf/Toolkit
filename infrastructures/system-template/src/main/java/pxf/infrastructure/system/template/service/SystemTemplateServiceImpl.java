package pxf.infrastructure.system.template.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import pxf.infrastructure.system.template.entity.SystemTemplate;
import pxf.infrastructure.system.template.mapper.SystemTemplateMapper;
import pxf.toolkit.basic.util.Is;

/**
 * @author potatoxf
 * @date 2021/5/12
 */
public class SystemTemplateServiceImpl extends ServiceImpl<SystemTemplateMapper, SystemTemplate>
    implements SystemTemplateService {

  private final SystemTemplateContentService systemTemplateContentService;

  public SystemTemplateServiceImpl(SystemTemplateContentService systemTemplateContentService) {
    this.systemTemplateContentService = systemTemplateContentService;
  }

  @Override
  public boolean isExistTemplate(@Nullable String code) {
    if (Is.empty(code)) {
      return false;
    }
    QueryWrapper<SystemTemplate> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("code", code);
    return getBaseMapper().selectCount(queryWrapper) > 0;
  }

  @Nullable
  @Override
  public SystemTemplate searchByCode(@Nonnull String code) {
    QueryWrapper<SystemTemplate> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("code", code);
    return getOne(queryWrapper);
  }

  public boolean remoteTemplate(Long id) {
    if (id == null) {
      return true;
    }
    SystemTemplate systemTemplate = getById(id);
    if (systemTemplate == null) {
      return true;
    }
    Long contextId = systemTemplate.getContextId();
    if (contextId != null) {
      systemTemplateContentService.removeById(contextId);
    }
    return removeById(id);
  }
}
