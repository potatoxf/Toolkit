package pxf.extsweb.fileonlinepreview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pxf.toolkit.basic.util.Delete;

@Component
@ConditionalOnExpression("'${cache.clean.enabled:false}'.equals('true')")
public class SchedulerCleanConfig {

  private final Logger logger = LoggerFactory.getLogger(SchedulerCleanConfig.class);

  private final CacheService cacheService;
  private final String fileDir = ConfigConstants.getFileDir();

  public SchedulerCleanConfig(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  // 默认每晚3点执行一次
  @Scheduled(cron = "${cache.clean.cron:0 0 3 * * ?}")
  public void clean() {
    logger.info("Cache clean start");
    cacheService.cleanCache();
    Delete.directorySafely(fileDir);
    logger.info("Cache clean end");
  }
}
