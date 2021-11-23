package cn.itcast.wanxinp2p.account.service;

import cn.itcast.wanxinp2p.api.account.model.AccountDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountLoginDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountRegisterDTO;
import cn.itcast.wanxinp2p.common.domain.RestResponse;

/**
 * @Created by maodaiyu on 2021/11/23 下午3:03
 */
public interface AccountService {

    /**
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @return
     */
    RestResponse getSMSCode(String mobile);

    /**
     * 校验手机号
     *
     * @param mobile
     * @param key
     * @param code
     * @return
     */
    Integer checkMobile(String mobile, String key, String code);

    /**
     * 注册
     *
     * @param accountRegisterDTO
     * @return
     */
    AccountDTO register(AccountRegisterDTO accountRegisterDTO);

    /**
     * 用户登录
     *
     * @param accountLoginDTO
     * @return
     */
    AccountDTO login(AccountLoginDTO accountLoginDTO);

}
