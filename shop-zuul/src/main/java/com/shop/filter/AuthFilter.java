package com.shop.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.shop.bean.ResponseBody;
import com.shop.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class AuthFilter extends ZuulFilter {
    private final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);
    private static final String[] EXCLUDE_URI = {
            "/commodity",
            "/provider01/login",
            "/provider01/register",
            "/provider01/user/code",
            "/provider01/is_login",
            "/provider01/user/resetpassword",
            "/provider01/spendrank",
            "/provider01/orderquantityrank",
            "/provider01/admin"
    };

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        for (String exc : EXCLUDE_URI)
            if (request.getRequestURI().contains(exc))
                return false;
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        Map<String, List<String>> requestQueryParams = requestContext.getRequestQueryParams();
        if (requestQueryParams == null)
            requestQueryParams = new HashMap<>();
        HttpServletRequest request = requestContext.getRequest();
        String token = request.getHeader("token");
        if (TokenUtil.verify(token)) {
            List<String> userId = new ArrayList<>(1);
            userId.add(TokenUtil.getId(token).toString());
            requestQueryParams.put("userId", userId);
            requestContext.setRequestQueryParams(requestQueryParams);
            LOG.info("登录验证通过");
        } else {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseBody(JSONObject.toJSONString(ResponseBody.notLogin()));
        }
        return null;
    }
}
