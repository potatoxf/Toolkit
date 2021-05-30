package pxf.springboot.starter.task;

import org.mybatis.spring.annotation.MapperScan;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pxf.infrastructure.system.task.SystemTaskManagerService;
import pxf.infrastructure.system.task.mapper.SystemTaskMapper;
import pxf.infrastructure.system.task.quartz.QuartzDispatcherJob;
import pxf.infrastructure.system.task.quartz.QuartzKeySupplier;
import pxf.infrastructure.system.task.quartz.QuartzSystemTaskManagerService;
import pxf.infrastructure.system.task.service.SystemTaskLogService;
import pxf.infrastructure.system.task.service.SystemTaskLogServiceImpl;
import pxf.infrastructure.system.task.service.SystemTaskService;
import pxf.infrastructure.system.task.service.SystemTaskServiceImpl;

/**
 * 任务管理中心
 *
 * @author potatoxf
 * @date 2021/5/8
 */
@Configuration
@MapperScan("pxf.infrastructure.system.task.mapper")
public class TaskManagerCenter {

  @Bean
  public SystemTaskService systemTaskService() {
    return new SystemTaskServiceImpl();
  }

  @Bean
  public SystemTaskLogService systemTaskLogService() {
    return new SystemTaskLogServiceImpl();
  }

  @Bean
  @ConditionalOnBean({Scheduler.class, SystemTaskService.class, SystemTaskLogService.class})
  @ConditionalOnMissingBean(SystemTaskManagerService.class)
  public QuartzSystemTaskManagerService quartzSystemTaskManagerService(
      Scheduler scheduler,
      SystemTaskService systemTaskService,
      SystemTaskLogService systemTaskLogService) {
    QuartzSystemTaskManagerService quartzSystemTaskManagerService =
        new QuartzSystemTaskManagerService(
            scheduler, systemTaskService, systemTaskLogService, new QuartzKeySupplier());
    QuartzDispatcherJob.setQuartzSystemTaskManagerService(quartzSystemTaskManagerService);
    return quartzSystemTaskManagerService;
  }
}
