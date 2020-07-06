package com.oumuv.cas.conf;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Configuration
@Order(value=0)
@WebFilter(filterName = "CorsFilterConfig", urlPatterns = "/*")
public class CorsFilterConfig implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    System.out.println("===============CorsFilterConfig执行=================");
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    HttpServletResponse res = (HttpServletResponse) servletResponse;
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
    res.setHeader("Access-Control-Max-Age", "1728000");
    res.setHeader("Access-Control-Allow-Headers",
            "Authentication, Authorization, content-type, Accept, x-requested-with, Cache-Control");
    filterChain.doFilter(servletRequest, res);
  }

  @Override
  public void destroy() {
  }

}