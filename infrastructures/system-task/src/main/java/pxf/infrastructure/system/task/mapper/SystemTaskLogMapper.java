package pxf.infrastructure.system.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pxf.infrastructure.system.task.entity.SystemTaskLog;

/**
 * @author potatoxf
 * @date 2021/5/6
 */
@Mapper
public interface SystemTaskLogMapper extends BaseMapper<SystemTaskLog> {
}
