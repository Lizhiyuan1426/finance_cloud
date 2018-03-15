package com.atguigu.atcrowdfunding.manager.servcie;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.atcrowdfunding.common.bean.MemberCert;

@FeignClient("eureka-member-service")
public interface MemberService {

	@RequestMapping("/queryMemberCertByMemberid/{memberid}")
	List<MemberCert> queryMemberCertByMemberid(@PathVariable("memberid") String memberid);

	@RequestMapping("/finishAuth")
	void finishAuth(@RequestBody Map<String, Object> map);

}