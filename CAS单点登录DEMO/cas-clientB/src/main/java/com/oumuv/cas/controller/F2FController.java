package com.oumuv.cas.controller;

import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@RestController
public class F2FController  {


    @RequestMapping(value = "/userdetail", produces = {"application/json;charset=UTF-8"})
    public JsonResult login(HttpServletResponse response, HttpServletRequest request) {
        Object object = request.getSession().getAttribute("_const_cas_assertion_");
        String policeNumber = "";
        if (null != object) {
            Assertion assertion = (Assertion) object;
            policeNumber = assertion.getPrincipal().getName();
            System.out.println("登录用户名：" + policeNumber);
        }

        return JsonResult.get().setData("policeNumber---------------------->"+policeNumber);
    }


	@RequestMapping(value = "/userlogin" ,method = RequestMethod.GET)
	public void testF2F(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Object object = request.getSession().getAttribute("_const_cas_assertion_");
        String policeNumber ="";
        if(null!=object){
            Assertion assertion = (Assertion)object;
            policeNumber = assertion.getPrincipal().getName();
            Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
            System.out.println("/userlogin/登录用户名："+policeNumber);
            System.out.println("登录返回的属性："+attributes);
        }

        boolean flag =  new SecurityInterceptor().preHandle(request,response,handler);
        HttpSession session = request.getSession(flag);
        session.setAttribute("policeNumber",policeNumber);
        String id = session.getId();
        System.out.println(id);
        response.sendRedirect("http://192.168.99.115:8360/honeycomb/Home/Homepage?jsessionid="+id);

    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession(false);
            if (session != null) {
                System.out.println("requst path " + request.getServletPath());

                Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

                if (assertion != null) {
                    System.out.println("cas user ---------> " + assertion.getPrincipal().getName());
                }
                Object policeNumber = session.getAttribute("policeNumber");
                if (policeNumber != null) {
                    return true;
                }
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}