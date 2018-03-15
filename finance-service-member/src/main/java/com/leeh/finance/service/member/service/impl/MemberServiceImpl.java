package com.atguigu.atcrowdfunding.service.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;
import com.atguigu.atcrowdfunding.service.member.dao.MemberDao;
import com.atguigu.atcrowdfunding.service.member.service.MemberService;

@Service
@Transactional(readOnly=true)
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Override
	public Member queryMemberByLoginAccount(String loginAccount) {
		return memberDao.queryMemberByLoginAccount(loginAccount);
	}

	@Override
	public Ticket queryTicketByMemberId(Integer id) {
		Ticket ticket =  memberDao.queryTicketByMemberId(id);
		return ticket;
	}

	@Transactional
	@Override
	public void insertTicket(Ticket ticket) {
		memberDao.insertTicket(ticket);
	}

	@Transactional
	@Override
	public void updateAccountType(Member loginMember) {
		memberDao.updateAccountType(loginMember);
	}

	@Transactional
	@Override
	public void updateTicket(Ticket ticket) {
		memberDao.updateTicket(ticket);
	}

	@Transactional
	@Override
	public void updateBasicInfo(Member loginMember) {
		memberDao.updateBasicInfo(loginMember);
	}

	@Override
	public List<Cert> queryCertsByAccountType(String accttype) {
		return memberDao.queryCertsByAccountType(accttype);
	}

	@Transactional
	@Override
	public void updateMemberCert(List<MemberCert> mcs) {
		memberDao.updateMemberCert(mcs);
	}

	@Override
	public Member queryMemberById(Integer id) {
		return memberDao.queryMemberById(id);
	}

	@Transactional
	@Override
	public void updateAuthCodeAndPstep(Ticket ticket) {
		memberDao.updateAuthCodeAndPstep(ticket);
	}

	@Transactional
	@Override
	public void updateAuthstatus(Member loginMember) {
		memberDao.updateAuthstatus(loginMember);
	}

	@Override
	public Member queryMemberByPiid(String piid) {
		return memberDao.queryMemberByPiid(piid);
	}

	@Override
	public List<MemberCert> queryMemberCertByMemberid(String memberid) {
		return memberDao.queryMemberCertByMemberid(memberid);
	}

	@Transactional
	@Override
	public void updateTicketStatus(Ticket ticket) {
		memberDao.updateTicketStatus(ticket);
	}
}
