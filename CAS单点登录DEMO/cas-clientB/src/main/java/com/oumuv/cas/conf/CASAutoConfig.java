package com.oumuv.cas.conf;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.net.UnknownHostException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 *
 * @Author Oumuv
 * @Date 2018/10/23 16:59
 **/
@Configuration
public class CASAutoConfig {
    @Value("${cas.server-url-prefix}")
    private String serverUrlPrefix;
    @Value("${cas.server-login-url}")
    private String serverLoginUrl;
    @Value("${cas.client-host-url}")
    private String clientHostUrl;


    @Bean
    public FilterRegistrationBean registSingleSignOutFilter() throws UnknownHostException {
        FilterRegistrationBean<SingleSignOutFilter> registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", serverUrlPrefix);
        registration.setInitParameters(initParameters);
        //set mapping url
        registration.addUrlPatterns("/*");
        //set loading sequence
        registration.setOrder(1);
        return registration;
    }

    /**
     * 添加监听器
     *
     * @return    
     */
    @Bean
    public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration() {
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * 授权过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> filterAuthenticationRegistration() {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", serverUrlPrefix);
        initParameters.put("serverName", clientHostUrl);
        //忽略的url，"|"分隔多个url
        initParameters.put("ignorePattern", "/logout/success|/index");

        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.addListener(new SingleSignOutHttpSessionListener());
            }
        };
    }

    @Bean
    public FilterRegistrationBean registAuthFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        registration.addUrlPatterns("/cas/*", "/we/*");
        registration.addInitParameter("casServerLoginUrl", serverUrlPrefix);
        registration.addInitParameter("serverName", clientHostUrl);
        registration.setName("registAuthFilter");
        return registration;
    }


    /**
     * wraper过滤器
     *
     * @return    
     */
    @Bean
    public FilterRegistrationBean<HttpServletRequestWrapperFilter> filterWrapperRegistration() {
        FilterRegistrationBean<HttpServletRequestWrapperFilter> registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestWrapperFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        // 设定加载的顺序
        registration.setOrder(1);
        return registration;
    }

    /**
     * CAS Validation Filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> filterValidationRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        //set mapping url
        registration.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap();
        initParameters.put("casServerUrlPrefix", serverUrlPrefix);
        initParameters.put("serverName", clientHostUrl);
        initParameters.put("useSession", "true");
        registration.setInitParameters(initParameters);
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("src/main/webapp/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }
}
