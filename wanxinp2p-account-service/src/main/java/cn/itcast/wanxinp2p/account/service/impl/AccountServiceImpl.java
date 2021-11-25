package cn.itcast.wanxinp2p.account.service.impl;

import cn.itcast.wanxinp2p.account.common.AccountErrorCode;
import cn.itcast.wanxinp2p.account.entity.Account;
import cn.itcast.wanxinp2p.account.mapper.AccountMapper;
import cn.itcast.wanxinp2p.account.service.AccountService;
import cn.itcast.wanxinp2p.account.service.SmsService;
import cn.itcast.wanxinp2p.api.account.model.AccountDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountLoginDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountRegisterDTO;
import cn.itcast.wanxinp2p.common.domain.BusinessException;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.common.util.PasswordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Created by maodaiyu on 2021/11/22 下午9:35
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private SmsService smsService;

    @Value("${sms.enable}")
    private Boolean smsEnable;

    @Override
    public RestResponse getSMSCode(String mobile) {
        return smsService.getSMSCode(mobile);
    }

    @Override
    public Integer checkMobile(String mobile, String key, String code) {
        smsService.verifySMSCode(key, code);

        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Account::getMobile, mobile);
        int res = count(queryWrapper);
        return res > 0 ? 1 : 0;
    }

    @Override
    public AccountDTO register(AccountRegisterDTO accountRegisterDTO) {
        Account account = new Account();
        account.setUsername(accountRegisterDTO.getUsername());
        account.setMobile(accountRegisterDTO.getMobile());
        account.setPassword(PasswordUtil.generate(accountRegisterDTO.getPassword()));
        if (!smsEnable) {
            account.setPassword(PasswordUtil.generate(accountRegisterDTO.getMobile()));
        }
        account.setDomain("c");
        save(account);
        return convert(account);
    }

    @Override
    public AccountDTO login(AccountLoginDTO accountLoginDTO) {
        //1.根据用户名和密码进行一次查询
        //2.先根据用户名进行查询，然后再比对密码
        Account account = null;
        if (accountLoginDTO.getDomain().equalsIgnoreCase("c")) {
            //如果是c端用户，用户名就是手机号
            account = getAccountByMobile(accountLoginDTO.getMobile());
        } else {
            //如果是b端用户，用户名就是账号
            account = getAccountByUsername(accountLoginDTO.getUsername());
        }
        if (account == null) {
            throw new BusinessException(AccountErrorCode.E_130104);
        }

        AccountDTO accountDTO = convert(account);
        if(smsEnable){ //如果为true,表示采用短信验证码登录，无需比较密码
            return accountDTO;
        }

        if (PasswordUtil.verify(accountLoginDTO.getPassword(), account.getPassword())) {
            return accountDTO;
        }

        throw new BusinessException(AccountErrorCode.E_130105);
    }

    private Account getAccountByMobile(String mobile){
        return getOne(new QueryWrapper<Account>().lambda().eq(Account::getMobile,mobile));
    }

    private Account getAccountByUsername(String username){
        return getOne(new QueryWrapper<Account>().lambda().eq(Account::getUsername,username));
    }

    /**
     * entity转为dto
     *
     * @param entity
     * @return
     */
    private AccountDTO convert(Account entity) {
        if (entity == null) {
            return null;
        }
        AccountDTO dto = new AccountDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}

