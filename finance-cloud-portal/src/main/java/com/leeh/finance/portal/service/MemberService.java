package com.atguigu.atcrowdfunding.portal.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;

@FeignClient("eureka-member-service")
public interface MemberService {

	@ResponseBody
	@RequestMapping("/login/{loginAccount}")
	public Member login(@PathVariable("loginAccount") String loginAccount);

	@ResponseBody
	@RequestMapping("/queryTicketByMemberId/{id}")
	public Ticket queryTicketByMemberId(@PathVariable("id") Integer id);

	@RequestMapping("/insertTicket")
	public void insertTicket(@RequestBody Ticket ticket);

	@RequestMapping("/updateAccountType")
	public void updateAccountType(@RequestBody Member loginMember);

	@RequestMapping("/updateBasicInfo")
	public void updateBasicInfo(@RequestBody Member loginMember);

	@RequestMapping("/queryCertsByAccountType/{accttype}")
	public List<Cert> queryCertsByAccountType(@PathVariable("accttype") String accttype);

	@RequestMapping("/updateMemberCert")
	public void updateMemberCert(@RequestBody List<MemberCert> mcs);

	@RequestMapping("/updateEmail")
	public void updateEmail(@RequestBody Member loginMember);

	@RequestMapping("/updateAuthstatus")
	public void updateAuthstatus(@RequestBody Member loginMember);
}