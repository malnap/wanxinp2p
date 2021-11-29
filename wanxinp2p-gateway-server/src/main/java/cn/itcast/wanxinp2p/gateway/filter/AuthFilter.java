package cn.itcast.wanxinp2p.gateway.filter;

import cn.itcast.wanxinp2p.common.util.EncryptUtil;
import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在过滤器中把令牌中的用户信息取出来并转发给微服务使用
 */
@Component
public class AuthFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 前置过滤器，可以在请求被路由之前调用
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 组装明文token，转发给下游微服务
     *
     * @return
     */
    @Override
    public Object run() {
        // 1.获取Spring Security OAuth2的认证信息对象
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null || !(authentication instanceof OAuth2Authentication)) {
            return null;// 无token访问网关内资源，直接返回null
        }
        // 2.将当前登录的用户以及接入客户端的信息放入Map中
        OAuth2Authentication oauth2Authentication=(OAuth2Authentication)authentication;

        Map<String,String> jsonToken = new HashMap<>
                (oauth2Authentication.getOAuth2Request().getRequestParameters());
        // 3.将jsonToken写入转发微服务的request中，这样微服务就能通过request.getParams("jsonToken")获取到了
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // 关键步骤，一定要get一下，下面这行代码才能取到值
        request.getParameterMap();
        Map<String,List<String>> requestQueryParams = ctx.getRequestQueryParams();
        if (requestQueryParams == null) {
            requestQueryParams = new HashMap<>();
        }
        List<String> arrayList = new ArrayList<>();
        arrayList.add(EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));
        requestQueryParams.put("jsonToken", arrayList);
        ctx.setRequestQueryParams(requestQueryParams);
        return null;
    }
}