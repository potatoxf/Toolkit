package pxf.infrastructure.system.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pxf.infrastructure.system.template.entity.SystemTemplateContent;

/**
 * 系统模板Mapper
 *
 * @author potatoxf
 * @date 2021/5/12
 */
@Mapper
public interface SystemTemplateContentMapper extends BaseMapper<SystemTemplateContent> {}
