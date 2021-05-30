package pxf.infrastructure.system.basic.message;

import pxf.infrastructure.system.basic.message.entity.MessageSendingLog;
import pxf.infrastructure.system.basic.message.service.MessageSendingLogService;

/**
 * 信息发送器
 *
 * @author potatoxf
 * @date 2021/5/12
 */
public abstract class MessageSender {

  private final MessageSendingLogService messageSendingLogService;

  protected MessageSender(MessageSendingLogService messageSendingLogService) {
    this.messageSendingLogService = messageSendingLogService;
  }

  public final boolean send(MessageContext messageContext) {
    MessageSendingLog messageSendingLog = new MessageSendingLog();
    try {
      handleSendBeforeLog(messageContext, messageSendingLog);
      handleSendBefore(messageContext);
      doSend(messageContext);
      handleSendAfterLog(messageContext, messageSendingLog);
      handleSendAfter(messageContext);
      return true;
    } catch (Throwable e) {
      handleSendExceptionLog(messageContext, e, messageSendingLog);
      handleSendException(messageContext, e);
      return false;
    } finally {
      handleSendFinal(messageContext);
    }
  }

  private void handleSendBeforeLog(
      MessageContext messageContext, MessageSendingLog messageSendingLog) {}

  protected void handleSendBefore(MessageContext messageContext) {}

  private void handleSendAfterLog(
      MessageContext messageContext, MessageSendingLog messageSendingLog) {}

  protected void handleSendAfter(MessageContext messageContext) {}

  private void handleSendExceptionLog(
      MessageContext messageContext, Throwable e, MessageSendingLog messageSendingLog) {}

  protected void handleSendException(MessageContext messageContext, Throwable e) {}

  private void handleSendFinal(MessageContext messageContext) {}

  protected abstract void doSend(MessageContext messageContext) throws Throwable;
}
