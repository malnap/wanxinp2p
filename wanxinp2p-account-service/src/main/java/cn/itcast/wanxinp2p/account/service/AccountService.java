package cn.itcast.wanxinp2p.account.service;

import cn.itcast.wanxinp2p.common.domain.RestResponse;

/**
 * @Created by maodaiyu on 2021/11/22 下午9:35
 */
public interface AccountService {

    /**
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @return
     */
    RestResponse getSMSCode(String mobile);

}
