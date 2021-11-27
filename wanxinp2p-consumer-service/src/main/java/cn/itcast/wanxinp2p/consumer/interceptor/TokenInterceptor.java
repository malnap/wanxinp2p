package cn.itcast.wanxinp2p.consumer.interceptor;

import cn.itcast.wanxinp2p.api.account.model.LoginUser;
import cn.itcast.wanxinp2p.common.util.EncryptUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在认证拦截功能中，如果网关校验令牌成功，就会把当前登录用户的一些信息存放到请
 * 求中，然后转发给各个微服务，微服务采用SpringMVC的拦截器实现Token的拦截处理
 */
public class TokenInterceptor implements HandlerInterceptor {

	/**
	 * 前置拦截器，网关转发到微服务的请求会被该拦截器接收，在该拦截器中取出数据并做相应处理
	 *
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param o
	 * @return
	 */
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
		String jsonToken = httpServletRequest.getParameter("jsonToken");
		if (StringUtils.isNotBlank(jsonToken)) {
			LoginUser loginUser = JSON.parseObject(EncryptUtil.decodeUTF8StringBase64(jsonToken),
														new TypeReference<LoginUser>() {});
			httpServletRequest.setAttribute("jsonToken", loginUser);
		}
		return true;
	}

}
