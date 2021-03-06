package cn.itcast.wanxinp2p.gateway.config;

import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.gateway.common.util.HttpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestOAuth2AuthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        RestResponse restResponse = new RestResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
        HttpUtil.writerError(restResponse,response);
    }
}
