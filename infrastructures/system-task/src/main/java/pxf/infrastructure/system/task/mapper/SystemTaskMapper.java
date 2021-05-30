package pxf.infrastructure.system.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pxf.infrastructure.system.task.entity.SystemTask;

/**
 * @author potatoxf
 * @date 2021/5/6
 */
@Mapper
public interface SystemTaskMapper extends BaseMapper<SystemTask> {

}
