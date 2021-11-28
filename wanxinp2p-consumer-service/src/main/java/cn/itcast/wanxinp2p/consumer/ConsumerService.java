package cn.itcast.wanxinp2p.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@MapperScan("cn.itcast.wanxinp2p.consumer.mapper")
@EnableFeignClients(basePackages = {"cn.itcast.wanxinp2p.consumer.agent"})
@SpringBootApplication(scanBasePackages = {"org.dromara.hmily", "cn.itcast.wanxinp2p.consumer"})
public class  ConsumerService {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerService.class, args);
    }

}