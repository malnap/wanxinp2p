package cn.itcast.wanxinp2p.api.account;

import cn.itcast.wanxinp2p.common.domain.RestResponse;

public interface AccountAPI {

    /**
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @return 响应包装
     */
    RestResponse getSMSCode(String mobile);


}
