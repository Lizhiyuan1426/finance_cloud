package com.atguigu.atcrowdfunding.service.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;
import com.atguigu.atcrowdfunding.service.member.service.ActiService;
import com.atguigu.atcrowdfunding.service.member.service.MemberService;

@RestController
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private ActiService actiService;
	
	@RequestMapping("/finishAuth")
	void finishAuth(@RequestBody Map<String, Object> map) {
		// 更新实名认证状态
		Integer id = (Integer) map.get("memberid");
		String taskid = (String) map.get("taskid");
		Member member = memberService.queryMemberById(id);
		member.setAuthstatus("2");
		memberService.updateAuthstatus(member);
		
		// 让流程继续执行
		actiService.endProcess(taskid);
		
		// 更新流程单状态
		Ticket ticket = memberService.queryTicketByMemberId(id);
		ticket.setStatus("1");
		memberService.updateTicketStatus(ticket);
		
	}
	
	@RequestMapping("/queryMemberCertByMemberid/{memberid}")
	List<MemberCert> queryMemberCertByMemberid(@PathVariable("memberid") String memberid){
		return memberService.queryMemberCertByMemberid(memberid);
	}
	
	@RequestMapping("/queryMemberByPiid/{piid}")
	public Member queryMemberByPiid(@PathVariable("piid") String piid) {
		return memberService.queryMemberByPiid(piid);
	}
	
	@RequestMapping("/updateAuthstatus")
	public void updateAuthstatus(@RequestBody Member loginMember) {
		// 更新用户实名认证状态
		memberService.updateAuthstatus(loginMember);
		// 流程继续
		actiService.process(loginMember.getLoginAccount());
	}
	
	@RequestMapping("/updateEmail")
	public void updateEmail(@RequestBody Member loginMember) {
		// 流程继续执行
		String authcode = actiService.sendMail(loginMember);
		// 更新流程步骤
		Ticket ticket = memberService.queryTicketByMemberId(loginMember.getId());
		ticket.setPstep("checkcode");
		ticket.setAuthcode(authcode);
		memberService.updateAuthCodeAndPstep(ticket);
	}
	
	@RequestMapping("/updateMemberCert")
	public void updateMemberCert(@RequestBody List<MemberCert> mcs) {
		// 更新会员资质图片信息
		memberService.updateMemberCert(mcs);
		Integer id = mcs.get(0).getMemberid();
		Member loginMember = memberService.queryMemberById(id);
		// 更新流程步骤
		Ticket ticket = memberService.queryTicketByMemberId(loginMember.getId());
		ticket.setPstep("email");
		memberService.updateTicket(ticket);
		// 流程往下执行
		actiService.nextProcess(loginMember.getLoginAccount());
	}
	
	/**
	 * 查询会员资质文件列表
	 * 
	 * @param accttype
	 * @return
	 */
	@RequestMapping("/queryCertsByAccountType/{accttype}")
	public List<Cert> queryCertsByAccountType(@PathVariable("accttype") String accttype){
		return memberService.queryCertsByAccountType(accttype);
	}
	
	/**
	 * 更新基本信息
	 * @param loginMember
	 */
	@RequestMapping("/updateBasicInfo")
	public void updateBasicInfo(@RequestBody Member loginMember) {
		// 更新会员账户类型
		memberService.updateBasicInfo(loginMember);
		// 更新流程步骤
		Ticket ticket = memberService.queryTicketByMemberId(loginMember.getId());
		ticket.setPstep("apply-cert");
		memberService.updateTicket(ticket);
		// 流程往下执行
		actiService.nextProcess(loginMember.getLoginAccount());
	}
	
	/**
	 * 更新账户类型
	 * @param loginMember
	 */
	@RequestMapping("/updateAccountType")
	public void updateAccountType(@RequestBody Member loginMember) {
		// 更新会员账户类型
		memberService.updateAccountType(loginMember);
		// 更新流程步骤
		Ticket ticket = memberService.queryTicketByMemberId(loginMember.getId());
		ticket.setPstep("apply-info");
		memberService.updateTicket(ticket);
		// 流程往下执行
		actiService.process(loginMember.getLoginAccount());
	}
	
	/**
	 * 新建实名认证申请流程
	 * 
	 * @param ticket
	 */
	@RequestMapping("/insertTicket")
	public void insertTicket(@RequestBody Ticket ticket) {
		memberService.insertTicket(ticket);
	}
	
	/**
	 * 查询实名认证申请流程
	 * 
	 * @param id
	 * @param pMap
	 * @return
	 */
	@RequestMapping("/queryTicketByMemberId/{id}")
	public Ticket queryTicketByMemberId(@PathVariable("id") Integer id) {
		Ticket ticket = memberService.queryTicketByMemberId(id);
		return ticket;
	}
	
	/**
	 * 
	 * @param loginAccount
	 * @return
	 */
	@RequestMapping("/login/{loginAccount}")
	public Object login(@PathVariable("loginAccount") String loginAccount) {
		Member member = memberService.queryMemberByLoginAccount(loginAccount);
		return member;
	}
}
