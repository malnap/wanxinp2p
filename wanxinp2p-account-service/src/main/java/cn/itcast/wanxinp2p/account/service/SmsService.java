package cn.itcast.wanxinp2p.account.service;

import cn.itcast.wanxinp2p.account.common.AccountErrorCode;
import cn.itcast.wanxinp2p.common.domain.BusinessException;
import cn.itcast.wanxinp2p.common.domain.CommonErrorCode;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.common.util.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Created by maodaiyu on 2021/11/22 下午9:37
 */
@Service
public class SmsService {

    @Value("${sms.url}")
    private String smsURL;

    @Value("${sms.enable}")
    private Boolean smsEnable;

    /**
     * 发送并获取短信验证码
     *
     * @param mobile
     * @return
     */
    public RestResponse getSMSCode(String mobile) {
        if (smsEnable) {
            return OkHttpUtil.post(smsURL + "/generate?effectiveTime=300&name=sms", "{\"mobile\":" + mobile + "}");
        }
        return RestResponse.success();
    }

    /**
     * 校验验证码
     *
     * @param key
     * @param code
     */
    public void verifySMSCode(String key, String code) {
        if (smsEnable) {
            StringBuilder params = new StringBuilder("verify?name=sms");
            params.append("&verificationKey=").append(key).append("&verificationCode=").append(code);

            RestResponse restResponse = OkHttpUtil.post(smsURL + params, "");

            if (restResponse.getCode() != CommonErrorCode.SUCCESS.getCode() ||
                    restResponse.getResult().toString().equalsIgnoreCase("false")) {
                throw new BusinessException(AccountErrorCode.E_140152);
            }
        }
    }

}
