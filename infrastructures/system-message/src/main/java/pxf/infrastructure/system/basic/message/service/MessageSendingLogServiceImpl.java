package pxf.infrastructure.system.basic.message.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pxf.infrastructure.system.basic.message.entity.MessageSendingLog;
import pxf.infrastructure.system.basic.message.mapper.MessageSendingLogMapper;

/**
 * @author potatoxf
 * @date 2021/5/12
 */
public class MessageSendingLogServiceImpl
    extends ServiceImpl<MessageSendingLogMapper, MessageSendingLog>
    implements MessageSendingLogService {

  private final MessageSendingLogMapper messageSendingLogMapper;

  public MessageSendingLogServiceImpl(MessageSendingLogMapper messageSendingLogMapper) {
    this.messageSendingLogMapper = messageSendingLogMapper;
  }
}
