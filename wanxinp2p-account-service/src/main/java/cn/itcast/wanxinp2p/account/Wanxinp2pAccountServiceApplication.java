package cn.itcast.wanxinp2p.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("cn.itcast.wanxinp2p.account.mapper")
@SpringBootApplication(scanBasePackages = {"org.dromara.hmily","cn.itcast.wanxinp2p.account"})
public class Wanxinp2pAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Wanxinp2pAccountServiceApplication.class, args);
    }

}
