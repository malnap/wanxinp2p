package cn.itcast.wanxinp2p.depository.service;

import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRequest;
import cn.itcast.wanxinp2p.api.depository.GatewayRequest;
import cn.itcast.wanxinp2p.common.domain.StatusCode;
import cn.itcast.wanxinp2p.common.util.EncryptUtil;
import cn.itcast.wanxinp2p.common.util.RSAUtil;
import cn.itcast.wanxinp2p.depository.common.constant.DepositoryRequestTypeCode;
import cn.itcast.wanxinp2p.depository.entity.DepositoryRecord;
import cn.itcast.wanxinp2p.depository.mapper.DepositoryRecordMapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepositoryRecordServiceImpl extends ServiceImpl<DepositoryRecordMapper, DepositoryRecord> implements DepositoryRecordService{

    @Autowired
    private ConfigService configService;

    @Override
    public GatewayRequest createConsumer(ConsumerRequest consumerRequest) {

        //1.保存交易记录
        saveDepositoryRecord(consumerRequest);

        //2.签名数据并返回
        String reqData=JSON.toJSONString(consumerRequest);
        String sign=RSAUtil.sign(reqData,configService.getP2pPrivateKey(),"utf-8");
        GatewayRequest gatewayRequest=new GatewayRequest();
        gatewayRequest.setServiceName("PERSONAL_REGISTER");
        gatewayRequest.setPlatformNo(configService.getP2pCode());
        gatewayRequest.setReqData(EncryptUtil.encodeURL(EncryptUtil
                .encodeUTF8StringBase64(reqData)));
        gatewayRequest.setSignature(EncryptUtil.encodeURL(sign));
        gatewayRequest.setDepositoryUrl(configService.getDepositoryUrl() + "/gateway");
        return gatewayRequest;

    }

    @Override
    public Boolean modifyRequestStatus(String requestNo, Integer requestsStatus) {
        return update(Wrappers.<DepositoryRecord>lambdaUpdate()
                .eq(DepositoryRecord::getRequestNo, requestNo)
                .set(DepositoryRecord::getRequestStatus, requestsStatus)
                .set(DepositoryRecord::getConfirmDate, LocalDateTime.now()));
    }

    private void saveDepositoryRecord(ConsumerRequest consumerRequest){
        DepositoryRecord depositoryRecord=new DepositoryRecord();
        depositoryRecord.setRequestNo(consumerRequest.getRequestNo());
        depositoryRecord.setRequestType(DepositoryRequestTypeCode.CONSUMER_CREATE.getCode());
        depositoryRecord.setObjectType("Consumer");
        depositoryRecord.setObjectId(consumerRequest.getId());
        depositoryRecord.setCreateDate(LocalDateTime.now());
        depositoryRecord.setRequestStatus(StatusCode.STATUS_OUT.getCode());
        save(depositoryRecord);
    }
}
