package pxf.infrastructure.system.template.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import pxf.infrastructure.system.template.entity.SystemTemplateContent;
import pxf.infrastructure.system.template.mapper.SystemTemplateContentMapper;
import pxf.toolkit.basic.util.Cast;

/**
 * @author potatoxf
 * @date 2021/5/12
 */
public class SystemTemplateContentServiceImpl
    extends ServiceImpl<SystemTemplateContentMapper, SystemTemplateContent>
    implements SystemTemplateContentService {

  @Override
  public long getLastUpdateTimestampById(Long id) {
    QueryWrapper<SystemTemplateContent> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("updateTimestamp").eq("id", id);
    List<Map<String, Object>> results = getBaseMapper().selectMaps(queryWrapper);
    if (!results.isEmpty()) {
      Object updateTimestamp = results.get(0).get("updateTimestamp");
      if (updateTimestamp != null) {
        return Cast.longValue(updateTimestamp, 0);
      }
    }
    return 0;
  }
}
