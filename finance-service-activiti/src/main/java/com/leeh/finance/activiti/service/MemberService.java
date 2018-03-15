package com.atguigu.atcrowdfunding.activiti.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.atcrowdfunding.common.bean.Member;

@FeignClient("eureka-member-service")
public interface MemberService {

	@RequestMapping("/queryMemberByPiid/{piid}")
	public Member queryMemberByPiid(@PathVariable("piid") String piid);

}