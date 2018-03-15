package com.atguigu.atcrowdfunding.service.member.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.atcrowdfunding.common.bean.Member;

@FeignClient("eureka-activiti-service")
public interface ActiService {

	@RequestMapping("/process/{loginAccount}")
	public void process(@PathVariable("loginAccount") String loginAccount);

	@RequestMapping("/nextProcess/{loginAccount}")
	public void nextProcess(@PathVariable("loginAccount") String loginAccount);

	@RequestMapping("/sendMail")
	public String sendMail(@RequestBody Member loginMember);

	@RequestMapping("/endProcess/{taskid}")
	public void endProcess(@PathVariable("taskid") String taskid);

}
