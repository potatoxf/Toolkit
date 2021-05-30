package pxf.infrastructure.system.setting;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.infrastructure.system.basic.constant.EntityStatus;
import pxf.infrastructure.system.basic.service.Manager;
import pxf.infrastructure.system.setting.entity.SystemSetting;
import pxf.infrastructure.system.setting.entity.SystemSettingIndex;
import pxf.infrastructure.system.setting.service.SystemSettingService;
import pxf.toolkit.basic.annotation.BuiltIn;
import pxf.toolkit.basic.function.Refreshable;
import pxf.toolkit.basic.reflect.ReflectHelper;
import pxf.toolkit.basic.util.Is;

/**
 * @author potatoxf
 * @date 2021/5/28
 */
public class SystemSettingManagerService extends Manager<SystemSetting, SystemSettingService>
    implements Refreshable {
  private static final Logger LOG = LoggerFactory.getLogger(SystemSettingManagerService.class);
  private static SystemSettingManagerService instanceHolder;

  /** 扫描包 */
  private final SystemSettingConfig systemSettingConfig;

  public SystemSettingManagerService(
      SystemSettingService service, SystemSettingConfig systemSettingConfig) {
    super(service);
    this.systemSettingConfig = systemSettingConfig;
    instanceHolder = this;
  }

  /**
   * 获取全局系统设置管理服务器
   *
   * @return {@code SystemSettingManagerService}
   */
  public static synchronized SystemSettingManagerService getGlobalInstanceHolder() {
    if (instanceHolder == null) {
      throw new IllegalStateException("The system settings management service is not initialized");
    }
    return instanceHolder;
  }
  /**
   * 设置全局系统设置管理服务器
   *
   * @param instanceHolder {@code SystemSettingManagerService}
   */
  public static synchronized void setGlobalInstanceHolder(
      SystemSettingManagerService instanceHolder) {
    SystemSettingManagerService.instanceHolder =
        Objects.requireNonNull(
            instanceHolder, "The system settings management service is not null");
  }

  public String getValue(AbstractSystemSettingConstant abstractSystemSettingConstant) {
    SystemSetting cacheSystemSetting = abstractSystemSettingConstant.getCacheSystemSetting();
    if (cacheSystemSetting != null) {
      return cacheSystemSetting.getValue();
    }
    QueryWrapper<SystemSetting> queryWrapper =
        buildIndexQueryWrapper(abstractSystemSettingConstant);
    cacheSystemSetting = getService().getOne(queryWrapper, true);
    if (cacheSystemSetting != null) {
      abstractSystemSettingConstant.setCacheSystemSetting(cacheSystemSetting);
      return cacheSystemSetting.getValue();
    }
    return null;
  }

  public boolean hasValue(AbstractSystemSettingConstant abstractSystemSettingConstant) {
    SystemSetting cacheSystemSetting = abstractSystemSettingConstant.getCacheSystemSetting();
    if (cacheSystemSetting != null) {
      return Is.noBlank(cacheSystemSetting.getValue());
    }
    QueryWrapper<SystemSetting> queryWrapper =
        buildIndexQueryWrapper(abstractSystemSettingConstant);
    cacheSystemSetting = getService().getOne(queryWrapper, true);
    if (cacheSystemSetting != null) {
      abstractSystemSettingConstant.setCacheSystemSetting(cacheSystemSetting);
      return Is.noBlank(cacheSystemSetting.getValue());
    }
    return false;
  }

  public boolean setValue(
      AbstractSystemSettingConstant abstractSystemSettingConstant, Object value) {
    String valueString = String.valueOf(value);
    QueryWrapper<SystemSetting> queryWrapper =
        buildIndexQueryWrapper(abstractSystemSettingConstant);
    SystemSetting cacheSystemSetting = getService().getOne(queryWrapper, true);
    if (cacheSystemSetting != null) {
      UpdateWrapper<SystemSetting> updateWrapper = new UpdateWrapper<>();
      updateWrapper.set("value", valueString);
      if (getService().update(updateWrapper)) {
        abstractSystemSettingConstant.refresh();
        cacheSystemSetting.setValue(valueString);
        abstractSystemSettingConstant.setCacheSystemSetting(cacheSystemSetting);
        return Is.noBlank(cacheSystemSetting.getValue());
      }
    }
    return false;
  }

  protected void init(String... packages) {
    List<URL> urls =
        Arrays.stream(packages)
            .filter(Is::noEmpty)
            .map(p -> ClasspathHelper.forPackage(p))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    ConfigurationBuilder configurationBuilder =
        new ConfigurationBuilder()
            .setUrls(urls)
            .useParallelExecutor()
            .setScanners(new SubTypesScanner());
    Reflections reflections = new Reflections(configurationBuilder);

    Map<SystemSettingIndex, SystemSettingConstant> already =
        reflections.getSubTypesOf(SystemSettingConstant.class).stream()
            .filter(
                c -> {
                  Optional<Annotation> any =
                      Arrays.stream(c.getDeclaredAnnotations())
                          .filter(annotation -> annotation.annotationType().equals(BuiltIn.class))
                          .findAny();
                  if (any.isPresent()) {
                    return false;
                  }
                  if (Is.publicClass(c)) {
                    return SystemSettingEnumConstant.class.isAssignableFrom(c)
                        || AbstractSystemSettingConstant.class.isAssignableFrom(c);
                  }
                  return false;
                })
            .flatMap(
                c -> {
                  if (AbstractSystemSettingConstant.class.isAssignableFrom(c)) {
                    return Arrays.stream(ReflectHelper.getStaticFields(c))
                        .filter(Is::publicStaticConstants)
                        .map(
                            field -> {
                              try {
                                return (SystemSettingConstant)
                                    ReflectHelper.getStaticFieldValue(c, field.getName());
                              } catch (NoSuchFieldException e) {
                                throw new RuntimeException(e);
                              }
                            });
                  }
                  if (SystemSettingEnumConstant.class.isAssignableFrom(c)) {
                    return Arrays.stream((SystemSettingConstant[]) c.getEnumConstants());
                  }
                  throw new RuntimeException();
                })
            .collect(
                Collectors.toMap(
                    SystemSettingConstant::toIndex,
                    systemSettingConstant -> systemSettingConstant));

    Map<SystemSettingIndex, SystemSetting> db =
        getService().list().stream()
            .collect(Collectors.toMap(SystemSetting::toIndex, systemSetting -> systemSetting));

    // already > db
    Set<SystemSettingIndex> alreadyIndex = already.keySet();
    Set<SystemSettingIndex> dbIndex = db.keySet();
    SetView<SystemSettingIndex> differenceInsert = Sets.difference(alreadyIndex, dbIndex);
    if (Is.noEmpty(differenceInsert)) {
      List<SystemSetting> insert = new ArrayList<>(differenceInsert.size());
      for (SystemSettingIndex systemSettingIndex : differenceInsert) {
        SystemSettingConstant systemSettingConstant = already.get(systemSettingIndex);
        SystemSetting systemSetting = SystemSetting.from(systemSettingConstant);
        insert.add(systemSetting);
      }
      getService().saveBatch(insert);
    }

    SetView<SystemSettingIndex> differenceUpdate = Sets.difference(dbIndex, alreadyIndex);
    if (Is.noEmpty(differenceUpdate)) {
      List<SystemSetting> update = new ArrayList<>(differenceUpdate.size());
      for (SystemSettingIndex systemSettingIndex : differenceUpdate) {
        SystemSetting systemSetting = db.get(systemSettingIndex);
        systemSetting.setEntityStatus(EntityStatus.ARTIFICIAL);
        update.add(systemSetting);
      }
      getService().updateBatchById(update);
    }
  }

  private QueryWrapper<SystemSetting> buildIndexQueryWrapper(
      AbstractSystemSettingConstant abstractSystemSettingConstant) {
    QueryWrapper<SystemSetting> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("module", abstractSystemSettingConstant.getModule());
    queryWrapper.eq("clazz", abstractSystemSettingConstant.getClazz());
    queryWrapper.eq("catalog", abstractSystemSettingConstant.getCatalog());
    queryWrapper.eq("name", abstractSystemSettingConstant.getName());
    return queryWrapper;
  }

  /** 将缓存里的内容重刷新 */
  @Override
  public void refresh() {
    if (LOG.isInfoEnabled()) {
      LOG.info("Start to refresh the system parameter settings");
    }
    String[] scanningPackages = systemSettingConfig.getScanningPackages();
    if (Is.empty(scanningPackages)) {
      if (LOG.isWarnEnabled()) {
        LOG.warn(
            "The system parameter is not configured, the scan package path will skip the scan");
      }
    } else {
      init(scanningPackages);
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("End refreshing system parameter settings");
    }
  }
}
