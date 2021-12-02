package cn.itcast.wanxinp2p.consumer.controller;

import cn.itcast.wanxinp2p.api.consumer.ConsumerAPI;
import cn.itcast.wanxinp2p.api.consumer.model.ConsumerDTO;
import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRequest;
import cn.itcast.wanxinp2p.api.depository.GatewayRequest;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.common.util.EncryptUtil;
import cn.itcast.wanxinp2p.consumer.common.SecurityUtil;
import cn.itcast.wanxinp2p.consumer.service.ConsumerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * l开头的请求路径给各微服务用的，my开头的给前端用的，m开头
 */
@RestController
@Api(value = "用户服务的Controller", tags = "Consumer", description = "用户服务API")
public class ConsumerController implements ConsumerAPI {

    @Autowired
    private ConsumerService consumerService;

    @PostMapping(value = "/consumers")
    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "consumerRegisterDTO", value = "注册信息", required = true,
                                        dataType = "AccountRegisterDTO", paramType = "body")
    public RestResponse register(@RequestBody ConsumerRegisterDTO consumerRegisterDTO) {
        consumerService.register(consumerRegisterDTO);
        return RestResponse.success();
    }

    @GetMapping(value = "/m/consumers/test")
    @ApiOperation("过网关受保护资源，进行认证拦截测试")
    @ApiImplicitParam(name = "jsonToken", value = "访问令牌", required = true, dataType = "String")
    public RestResponse<String> testResources(String jsonToken) {
        return RestResponse.success(EncryptUtil.decodeUTF8StringBase64(jsonToken));
    }

    @PostMapping("/my/consumers")
    @ApiOperation("生成开户请求数据")
    @ApiImplicitParam(name = "consumerRequest", value = "开户信息", required = true,
            dataType = "ConsumerRequest", paramType = "body")
    public RestResponse<GatewayRequest> createConsumer(@RequestBody ConsumerRequest consumerRequest) {
        // 由于consumerRequest里没有手机号这些信息，需要从认证授权框架取
        consumerRequest.setMobile(SecurityUtil.getUser().getMobile());
        return consumerService.createConsumer(consumerRequest);
    }

    @GetMapping("/l/currConsumer/{mobile}")
    @ApiOperation("获取登录用户信息")
    public RestResponse<ConsumerDTO> getCurrConsumer(@PathVariable("mobile") String mobile) {
        // 这里有个坑，调用SecurityUtil.getUser().getMobile()为null，因为远程调用交易中心不会携带令牌，把该逻辑移动到service里面去
        // ConsumerDTO consumerDTO = consumerService.getByMobile(SecurityUtil.getUser().getMobile());
        ConsumerDTO consumerDTO = consumerService.getByMobile(mobile);
        return RestResponse.success(consumerDTO);
    }

    @GetMapping("/my/consumers")
    @ApiOperation("获取登录用户信息")
    public RestResponse<ConsumerDTO> getMyConsumer() {
        ConsumerDTO consumerDTO = consumerService.getByMobile(SecurityUtil.getUser().getMobile());
        return RestResponse.success(consumerDTO);
    }
}