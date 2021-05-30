package pxf.extsweb.fileonlinepreview;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;

public class BaseUrlFilter implements Filter {

  private static String BASE_URL;

  public static String getBaseUrl() {
    String baseUrl;
    try {
      baseUrl = (String) RequestContextHolder.currentRequestAttributes().getAttribute("baseUrl", 0);
    } catch (Exception e) {
      baseUrl = BASE_URL;
    }
    return baseUrl;
  }

  @Override
  public void init(FilterConfig filterConfig) {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    String baseUrl;
    StringBuilder pathBuilder = new StringBuilder();
    pathBuilder
        .append(request.getScheme())
        .append("://")
        .append(request.getServerName())
        .append(":")
        .append(request.getServerPort())
        .append(((HttpServletRequest) request).getContextPath())
        .append("/");
    String baseUrlTmp = ConfigConstants.getBaseUrl();
    if (baseUrlTmp != null && !ConfigConstants.DEFAULT_BASE_URL.equalsIgnoreCase(baseUrlTmp)) {
      if (!baseUrlTmp.endsWith("/")) {
        baseUrlTmp = baseUrlTmp.concat("/");
      }
      baseUrl = baseUrlTmp;
    } else {
      baseUrl = pathBuilder.toString();
    }
    BASE_URL = baseUrl;
    request.setAttribute("baseUrl", baseUrl);
    filterChain.doFilter(request, response);
  }

  @Override
  public void destroy() {}
}
