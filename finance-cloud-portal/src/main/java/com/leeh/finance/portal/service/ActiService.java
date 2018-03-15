package com.atguigu.atcrowdfunding.portal.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("eureka-activiti-service")
public interface ActiService {

	@RequestMapping("/getProcessInstanceId/{loginAccount}")
	public String getProcessInstanceId(@PathVariable("loginAccount") String loginAccount);

}
