package cn.itcast.wanxinp2p.account.service.impl;

import cn.itcast.wanxinp2p.account.service.AccountService;
import cn.itcast.wanxinp2p.account.service.SmsService;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Created by maodaiyu on 2021/11/22 下午9:35
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    SmsService smsService;

    @Override
    public RestResponse getSMSCode(String mobile) {
        return smsService.getSMSCode(mobile);
    }
}
