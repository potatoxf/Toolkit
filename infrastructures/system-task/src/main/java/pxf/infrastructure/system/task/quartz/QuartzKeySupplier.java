package pxf.infrastructure.system.task.quartz;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

/**
 * @author potatoxf
 * @date 2021/5/6
 */
public class QuartzKeySupplier {

  private static final String QUARTZ_PARAMS_KEY = "TASK_PARAMS";
  private static final String JOB_KEY_PREFIX = "TASK_JOB_KEY_";
  private static final String TRIGGER_KEY_PREFIX = "TRIGGER_KEY_";

  public String getQuartzParamsKey() {
    return QUARTZ_PARAMS_KEY;
  }

  public Long getTaskIdByJobKey(JobKey jobKey) {
    String name = jobKey.getName();
    if (name.startsWith(JOB_KEY_PREFIX)) {
      name = name.substring(JOB_KEY_PREFIX.length());
    } else {
      throw new RuntimeException();
    }
    return Long.valueOf(name);
  }

  public Long getTaskIdByTriggerKey(TriggerKey triggerKey) {
    String name = triggerKey.getName();
    if (name.startsWith(TRIGGER_KEY_PREFIX)) {
      name = name.substring(TRIGGER_KEY_PREFIX.length());
    } else {
      throw new RuntimeException();
    }
    return Long.valueOf(name);
  }

  /** 获取触发器key */
  public TriggerKey getTriggerKey(Long taskId) {
    return TriggerKey.triggerKey(TRIGGER_KEY_PREFIX + taskId);
  }

  /** 获取jobKey */
  public JobKey getJobKey(Long taskId) {
    return JobKey.jobKey(JOB_KEY_PREFIX + taskId);
  }
}
