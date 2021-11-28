package cn.itcast.wanxinp2p.api.depository;

import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRequest;
import cn.itcast.wanxinp2p.common.domain.RestResponse;

/**
 * @Created by maodaiyu on 2021/11/28 下午5:05
 */
public interface DepositoryAgentApi {

    /**
     * 开通存管账户
     *
     * @param consumerRequest 开户信息
     * @return
     */
    RestResponse<GatewayRequest> createConsumer(ConsumerRequest consumerRequest);
}
