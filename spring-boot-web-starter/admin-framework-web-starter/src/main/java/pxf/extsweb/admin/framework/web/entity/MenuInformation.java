package pxf.extsweb.admin.framework.web.entity;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
public class MenuInformation extends AbstractHierarchicalEntity<MenuInformation> {

  private String name;
  private String url;
  private String style;

  public MenuInformation() {}

  public MenuInformation(MenuInformation menuInformation) {
    super(menuInformation);
    name = menuInformation.name;
    url = menuInformation.url;
    style = menuInformation.style;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getStyle() {
    return style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  @Override
  public MenuInformation getParent() {
    return null;
  }

  @Override
  public void setParent(MenuInformation parent) {}
}
