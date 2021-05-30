package pxf.extsweb.fileonlinepreview;

import java.util.HashSet;
import java.util.Set;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("unchecked")
public class FilterConfiguration {

  @Bean
  public FilterRegistrationBean getTrustHostFilter() {
    Set<String> filterUri = new HashSet<>();
    filterUri.add("/onlinePreview");
    filterUri.add("/picturesPreview");
    filterUri.add("/getCorsFile");
    filterUri.add("/addTask");
    TrustHostFilter filter = new TrustHostFilter();
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(filter);
    registrationBean.setUrlPatterns(filterUri);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean getBaseUrlFilter() {
    Set<String> filterUri = new HashSet<>();
    filterUri.add("/index");
    filterUri.add("/onlinePreview");
    filterUri.add("/picturesPreview");
    BaseUrlFilter filter = new BaseUrlFilter();
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(filter);
    registrationBean.setUrlPatterns(filterUri);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean getWatermarkConfigFilter() {
    Set<String> filterUri = new HashSet<>();
    filterUri.add("/onlinePreview");
    filterUri.add("/picturesPreview");
    AttributeSetFilter filter = new AttributeSetFilter();
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(filter);
    registrationBean.setUrlPatterns(filterUri);
    return registrationBean;
  }
}
