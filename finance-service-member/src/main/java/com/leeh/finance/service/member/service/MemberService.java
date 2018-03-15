package com.atguigu.atcrowdfunding.service.member.service;

import java.util.List;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;

public interface MemberService {

	/**
	 * 
	 * @param loginAccount
	 * @return
	 */
	Member queryMemberByLoginAccount(String loginAccount);

	Ticket queryTicketByMemberId(Integer id);

	void insertTicket(Ticket ticket);

	void updateAccountType(Member loginMember);

	void updateTicket(Ticket ticket);

	void updateBasicInfo(Member loginMember);

	List<Cert> queryCertsByAccountType(String accttype);

	void updateMemberCert(List<MemberCert> mcs);

	Member queryMemberById(Integer id);

	void updateAuthCodeAndPstep(Ticket ticket);

	void updateAuthstatus(Member loginMember);

	Member queryMemberByPiid(String piid);

	List<MemberCert> queryMemberCertByMemberid(String memberid);

	void updateTicketStatus(Ticket ticket);

}
