package pxf.toolkit.extension.poi.excel.decorator;

import java.util.Objects;
import pxf.toolkit.components.model.decorator.TableDecoratorConfiguration;
import pxf.toolkit.components.model.table.GridEnvironment;
import pxf.toolkit.extension.poi.excel.ExcelCellType;

/**
 * Excel表格装饰器配置
 *
 * @author potatoxf
 * @date 2021/4/3
 */
public class ExcelTableDecoratorConfiguration extends TableDecoratorConfiguration
    implements ExcelTableDecorator {

  /** 是否锁住单元格 */
  private Boolean locked;
  /** 是否隐藏单元格 */
  private Boolean hidden;
  /** 单元格公式 */
  private String formula;
  /** 单元格数据格式 */
  private String dataFormat;
  /** 单元格填充模式 */
  private ExcelDecoratorStyle fillPattern;

  /**
   * 构建 {@code ExcelTableDecoratorConfiguration}
   *
   * @param decorator 装饰器
   * @param gridEnvironment 表格环境
   * @return {@code ExcelTableDecoratorConfiguration}
   */
  public static ExcelTableDecoratorConfiguration of(
      ExcelTableDecorator decorator, GridEnvironment gridEnvironment) {
    if (decorator == null) {
      return null;
    }
    ExcelTableDecoratorConfiguration configuration = new ExcelTableDecoratorConfiguration();
    setDecoratorConfiguration(configuration, decorator, gridEnvironment);
    return configuration;
  }

  /**
   * 将装饰器设置到装饰器配置
   *
   * @param configuration 表格装饰器配置
   * @param decorator 装饰器
   * @param gridEnvironment 表格环境
   */
  public static void setExcelDecoratorConfiguration(
      ExcelTableDecoratorConfiguration configuration,
      ExcelTableDecorator decorator,
      GridEnvironment gridEnvironment) {
    setDecoratorConfiguration(configuration, decorator, gridEnvironment);

    configuration.setLocked(decorator.handleLocked(gridEnvironment));
    configuration.setHidden(decorator.handleHidden(gridEnvironment));
    configuration.setFormula(decorator.handleFormula(gridEnvironment));

    configuration.setDataFormat(decorator.handleDataFormat(gridEnvironment));
    configuration.setFillPattern(decorator.handleFillPattern(gridEnvironment));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ExcelTableDecoratorConfiguration that = (ExcelTableDecoratorConfiguration) o;
    return Objects.equals(getLocked(), that.getLocked())
        && Objects.equals(getHidden(), that.getHidden())
        && Objects.equals(getFormula(), that.getFormula())
        && Objects.equals(getDataFormat(), that.getDataFormat())
        && getFillPattern() == that.getFillPattern();
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(),
        getLocked(),
        getHidden(),
        getFormula(),
        getDataFormat(),
        getFillPattern());
  }

  @Override
  public Boolean handleLocked(GridEnvironment gridEnvironment) {
    return getLocked();
  }

  @Override
  public Boolean handleHidden(GridEnvironment gridEnvironment) {
    return getHidden();
  }

  @Override
  public String handleFormula(GridEnvironment gridEnvironment) {
    return getFormula();
  }

  @Override
  public ExcelCellType handleCellType(GridEnvironment gridEnvironment) {
    return null;
  }

  @Override
  public String handleDataFormat(GridEnvironment gridEnvironment) {
    return getDataFormat();
  }

  @Override
  public ExcelDecoratorStyle handleFillPattern(GridEnvironment gridEnvironment) {
    return getFillPattern();
  }

  // --------------------------------------------------------------------------- get set

  public Boolean getLocked() {
    return locked;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public String getFormula() {
    return formula;
  }

  public void setFormula(String formula) {
    this.formula = formula;
  }

  public String getDataFormat() {
    return dataFormat;
  }

  public void setDataFormat(String dataFormat) {
    this.dataFormat = dataFormat;
  }

  public ExcelDecoratorStyle getFillPattern() {
    return fillPattern;
  }

  public void setFillPattern(ExcelDecoratorStyle fillPattern) {
    this.fillPattern = fillPattern;
  }
}
