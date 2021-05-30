package pxf.infrastructure.system.basic.dbentity;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.infrastructure.system.basic.constant.EntityStatus;
import pxf.toolkit.basic.lang.CategorySelector;
import pxf.toolkit.basic.secret.DigestAlgorithm;
import pxf.toolkit.basic.secret.FingerprintHandler;
import pxf.toolkit.basic.secret.coder.EncoderException;
import pxf.toolkit.basic.util.Empty;
import pxf.toolkit.basic.util.Is;

/**
 * 实体处理器
 *
 * <p>该类用于自动化处理实体的公共字段
 *
 * <p>可以通过实现 {@link EntityHandler.FieldSetter}接口来扩展实现域值设置
 *
 * @author potatoxf
 * @date 2021/5/12
 */
public class EntityHandler implements MetaObjectHandler {

  private static final Logger LOG = LoggerFactory.getLogger(EntityHandler.class);
  private static final String VERSION = "version";
  private static final String STATUS = "status";
  private static final String UPDATE_TIMESTAMP = "updateTimestamp";
  private static final String CREATE_DATETIME = "createDatetime";
  private static final String CREATOR = "creator";
  private static final String UPDATE_DATETIME = "updateDatetime";
  private static final String UPDATER = "updater";
  /** 默认创建者和更新者 */
  private static final String DEFAULT_CU = "SYSTEM";
  /** 默认创建者和更新者提供器 */
  private static final OperatorSupplier DEFAULT_OPERATOR_SUPPLIER = () -> DEFAULT_CU;

  /** 域值设置选择器 */
  private final CategorySelector<FieldSetter> fieldSetterCategorySelector;

  public EntityHandler() {
    this(null);
  }

  public EntityHandler(OperatorSupplier operatorSupplier) {
    this(operatorSupplier, null);
  }

  public EntityHandler(OperatorSupplier operatorSupplier, DigestAlgorithm digestAlgorithm) {
    this(operatorSupplier, digestAlgorithm, new FieldSetter[0]);
  }

  /**
   * 构造器
   *
   * @param operatorSupplier 操作人提供器
   * @param digestAlgorithm 信息摘要算法
   * @param fieldSetters 域设置器
   */
  public EntityHandler(
      OperatorSupplier operatorSupplier,
      DigestAlgorithm digestAlgorithm,
      FieldSetter... fieldSetters) {
    FieldSetter[] setters = new FieldSetter[fieldSetters.length + 2];
    setters[0] = new DataEntityFieldSetter(operatorSupplier, digestAlgorithm);
    setters[1] = new BigDataEntityFieldSetter(operatorSupplier, digestAlgorithm);
    fieldSetterCategorySelector = CategorySelector.of(true, true, setters);
  }

  /**
   * 获取操作者
   *
   * @param operatorSupplier 操作者提供器
   * @return {@code String}
   */
  @Nonnull
  private static String getOperator(OperatorSupplier operatorSupplier) {
    if (operatorSupplier != null) {
      String operator = operatorSupplier.getOperator();
      if (operator != null) {
        return operator;
      }
    }
    return DEFAULT_OPERATOR_SUPPLIER.getOperator();
  }

  /**
   * 获取指纹
   *
   * @param content 内容
   * @param digestAlgorithm 数字算法
   * @return 返回指纹
   */
  @Nonnull
  private static String getStringFingerprint(String content, DigestAlgorithm digestAlgorithm) {
    if (content != null) {
      if (digestAlgorithm == null) {
        digestAlgorithm = DigestAlgorithm.MD5;
      }
      FingerprintHandler fingerprintHandler = digestAlgorithm.createHandler();
      try {
        return fingerprintHandler.signHex(content);
      } catch (EncoderException e) {
        throw new RuntimeException(e);
      }
    }
    return Empty.STRING;
  }

  /**
   * 初始化验证实体
   *
   * @param container 容器
   * @param originObject 原对象
   * @param digestAlgorithm 信息摘要算法
   */
  private static void initVerifyEntity(
      @Nonnull Map<String, Object> container,
      @Nonnull VerifyEntity originObject,
      DigestAlgorithm digestAlgorithm) {
    String content = originObject.getContent();
    String digest = originObject.getDigest();
    String fingerprint = getStringFingerprint(content, digestAlgorithm);
    String simpleName = originObject.getClass().getSimpleName();
    if (Is.empty(digest)) {
      // insert
      if (LOG.isTraceEnabled()) {
        LOG.trace(String.format("[E-I][INSERT][%-40s]%s", simpleName, fingerprint));
      }
      container.put("digest", fingerprint);
    } else {
      // update
      if (!fingerprint.equalsIgnoreCase(digest)) {
        if (LOG.isTraceEnabled()) {
          LOG.trace(String.format("[E-U][%-40s]<old>%s-<new>%s", simpleName, digest, fingerprint));
        }
        container.put("digest", fingerprint);
      } else {
        if (LOG.isTraceEnabled()) {
          LOG.trace(String.format("[E-N][%-40s][%s]", simpleName, digest));
        }
      }
    }
  }

  @Override
  public void insertFill(MetaObject metaObject) {
    Object originalObject = metaObject.getOriginalObject();
    if (originalObject != null) {
      Class<?> clz = lookupClass(originalObject);
      FieldSetter fieldSetter = fieldSetterCategorySelector.selectAction(clz);
      if (fieldSetter != null) {
        Map<String, Object> container = new HashMap<>(4, 1);
        fieldSetter.initInsertValueMapping(container, originalObject);
        fieldSetter.initUpdateValueMapping(container, originalObject);
        container.forEach((k, v) -> setFieldValByName(k, v, metaObject));
      }
    }
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    Object originalObject = metaObject.getOriginalObject();
    if (originalObject != null) {
      Class<?> clz = lookupClass(originalObject);
      FieldSetter fieldSetter = fieldSetterCategorySelector.selectAction(clz);
      if (fieldSetter != null) {
        Map<String, Object> container = new HashMap<>(4, 1);
        fieldSetter.initUpdateValueMapping(container, clz);
        container.forEach((k, v) -> setFieldValByName(k, v, metaObject));
      }
    }
  }

  private Class<?> lookupClass(Object object) {
    Class<?> result = object.getClass();
    if (DataEntity.class.isAssignableFrom(result)) {
      return DataEntity.class;
    } else if (BigDataEntity.class.isAssignableFrom(result)) {
      return BigDataEntity.class;
    }
    return result;
  }

  /** 域设置器 */
  public interface FieldSetter extends CategorySelector.Category {
    /**
     * 分类，类别需要唯一
     *
     * @return {@code Class<?>}
     */
    @Nonnull
    @Override
    Class<?> category();

    /**
     * 初始化插入值映射
     *
     * @param container {@code Map<String, Object>}设置值容器
     * @param originObject {@code 原对象}
     */
    void initInsertValueMapping(
        @Nonnull Map<String, Object> container, @Nonnull Object originObject);
    /**
     * 初始化更新值映射
     *
     * @param container {@code Map<String, Object>}设置值容器
     * @param originObject {@code 原对象}
     */
    void initUpdateValueMapping(
        @Nonnull Map<String, Object> container, @Nonnull Object originObject);
  }

  /** {@code DataEntity} 域设置器 */
  private static class DataEntityFieldSetter implements FieldSetter {

    /** 操作人员提供器 */
    private final OperatorSupplier operatorSupplier;
    /** 信息摘要算法 */
    private final DigestAlgorithm digestAlgorithm;

    private DataEntityFieldSetter(
        OperatorSupplier operatorSupplier, DigestAlgorithm digestAlgorithm) {
      this.operatorSupplier = operatorSupplier;
      this.digestAlgorithm = digestAlgorithm;
    }

    @Nonnull
    @Override
    public Class<?> category() {
      return DataEntity.class;
    }

    @Override
    public void initInsertValueMapping(
        @Nonnull Map<String, Object> container, @Nonnull Object originObject) {
      container.put(VERSION, 0);
      container.put(STATUS, EntityStatus.NORMAL.identity());
      if (originObject instanceof DataRecordEntity) {
        container.put(CREATE_DATETIME, new Date());
        container.put(CREATOR, getOperator(operatorSupplier));
      } else if (originObject instanceof DataChangedEntity) {
        container.put(UPDATE_TIMESTAMP, System.currentTimeMillis());
        if (originObject instanceof DataVerifyEntity) {
          initVerifyEntity(container, (DataVerifyEntity) originObject, digestAlgorithm);
        }
      }
    }

    @Override
    public void initUpdateValueMapping(
        @Nonnull Map<String, Object> container, @Nonnull Object originObject) {
      if (originObject instanceof DataRecordEntity) {
        container.put(UPDATE_DATETIME, new Date());
        container.put(UPDATER, getOperator(operatorSupplier));
      } else if (originObject instanceof DataChangedEntity) {
        container.put(UPDATE_TIMESTAMP, System.currentTimeMillis());
        if (originObject instanceof DataVerifyEntity) {
          initVerifyEntity(container, (DataVerifyEntity) originObject, digestAlgorithm);
        }
      }
    }
  }
  /** {@code BigDataEntity} 域设置器 */
  private static class BigDataEntityFieldSetter implements FieldSetter {

    /** 操作人员提供器 */
    private final OperatorSupplier operatorSupplier;
    /** 信息摘要算法 */
    private final DigestAlgorithm digestAlgorithm;

    private BigDataEntityFieldSetter(
        OperatorSupplier operatorSupplier, DigestAlgorithm digestAlgorithm) {
      this.operatorSupplier = operatorSupplier;
      this.digestAlgorithm = digestAlgorithm;
    }

    @Nonnull
    @Override
    public Class<?> category() {
      return BigDataEntity.class;
    }

    @Override
    public void initInsertValueMapping(
        @Nonnull Map<String, Object> container, @Nonnull Object originObject) {
      container.put(VERSION, 0);
      container.put(STATUS, EntityStatus.NORMAL.identity());
      if (originObject instanceof BigDataRecordEntity) {
        container.put(CREATE_DATETIME, new Date());
        container.put(CREATOR, getOperator(operatorSupplier));
      } else if (originObject instanceof BigDataChangedEntity) {
        container.put(UPDATE_TIMESTAMP, System.currentTimeMillis());
        if (originObject instanceof BigDataVerifyEntity) {
          initVerifyEntity(container, (BigDataVerifyEntity) originObject, digestAlgorithm);
        }
      }
    }

    @Override
    public void initUpdateValueMapping(
        @Nonnull Map<String, Object> container, @Nonnull Object originObject) {
      if (originObject instanceof BigDataRecordEntity) {
        container.put(UPDATE_DATETIME, new Date());
        container.put(UPDATER, getOperator(operatorSupplier));
      } else if (originObject instanceof BigDataChangedEntity) {
        container.put(UPDATE_TIMESTAMP, System.currentTimeMillis());
        if (originObject instanceof BigDataVerifyEntity) {
          initVerifyEntity(container, (BigDataVerifyEntity) originObject, digestAlgorithm);
        }
      }
    }
  }
}
