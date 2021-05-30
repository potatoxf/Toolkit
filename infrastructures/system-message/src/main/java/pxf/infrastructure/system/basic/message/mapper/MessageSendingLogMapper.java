package pxf.infrastructure.system.basic.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pxf.infrastructure.system.basic.message.entity.MessageSendingLog;

/**
 * @author potatoxf
 * @date 2021/5/12
 */
@Mapper
public interface MessageSendingLogMapper extends BaseMapper<MessageSendingLog> {}
