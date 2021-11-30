package cn.itcast.wanxinp2p.depository.service;

import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRequest;
import cn.itcast.wanxinp2p.api.depository.GatewayRequest;
import cn.itcast.wanxinp2p.depository.entity.DepositoryRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DepositoryRecordService extends IService<DepositoryRecord> {
    /**
     * 开通存管账户
     * @param consumerRequest 开户信息
     * @return
     */
    GatewayRequest createConsumer(ConsumerRequest consumerRequest);

    /**
     * 根据请求流水号更新请求状态
    * @param requestNo
    * @param requestsStatus
    * @return
        */
    Boolean modifyRequestStatus(String requestNo, Integer requestsStatus);
}
