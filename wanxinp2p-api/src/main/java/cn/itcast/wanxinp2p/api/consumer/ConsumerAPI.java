package cn.itcast.wanxinp2p.api.consumer;

import cn.itcast.wanxinp2p.api.consumer.model.ConsumerDTO;
import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRequest;
import cn.itcast.wanxinp2p.api.depository.GatewayRequest;
import cn.itcast.wanxinp2p.common.domain.RestResponse;

/**
 * 用户中心接口API
 */
public interface ConsumerAPI {

    /**
     * 用户注册
     *
     * @param consumerRegisterDTO
     * @return
     */
   RestResponse register(ConsumerRegisterDTO consumerRegisterDTO);

    /**
     * 生成开户请求数据
     *
     * @param consumerRequest 开户信息
     * @return
     */
    RestResponse<GatewayRequest> createConsumer(ConsumerRequest consumerRequest);

    /**
     * 保存标的时，需要用到当前登录用户的一些信息
     *
     * @return
     */
    RestResponse<ConsumerDTO> getCurrConsumer(String mobile);

    /**
     * 发标时，前端需要判断当前登录用户是否已经开户，借款金额是否超标等信息
     *
     * @return
     */
    RestResponse<ConsumerDTO> getMyConsumer();
}
