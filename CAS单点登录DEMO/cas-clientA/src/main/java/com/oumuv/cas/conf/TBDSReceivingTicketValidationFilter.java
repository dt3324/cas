package com.oumuv.cas.conf;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TBDSReceivingTicketValidationFilter extends Cas20ProxyReceivingTicketValidationFilter {
    @Override
    public void onSuccessfulValidation(HttpServletRequest request,
                                       HttpServletResponse response, Assertion assertion)
    {
        AttributePrincipal principal = assertion.getPrincipal();
        logger.info(principal.getName()+"--------------------------------");
        AssertionHolder.setAssertion(assertion);//获取用户信息
    }
    @Override
    public void onFailedValidation(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Failed to validate cas ticket");
    }
}