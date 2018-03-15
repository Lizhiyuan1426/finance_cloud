package com.atguigu.atcrowdfunding.service.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.atguigu.**.dao")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class AtcrowdfundingServiceMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtcrowdfundingServiceMemberApplication.class, args);
	}
}
