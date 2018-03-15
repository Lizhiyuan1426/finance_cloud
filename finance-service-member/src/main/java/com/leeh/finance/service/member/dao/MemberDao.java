package com.atguigu.atcrowdfunding.service.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;

public interface MemberDao {

	@Select("select * from t_member where loginAccount=#{loginAccount}")
	Member queryMemberByLoginAccount(String loginAccount);

	@Select("select * from t_ticket where memberid=#{id}")
	Ticket queryTicketByMemberId(Integer id);

	void insertTicket(Ticket ticket);

	void updateAccountType(Member loginMember);

	void updateTicket(Ticket ticket);

	void updateBasicInfo(Member loginMember);

	List<Cert> queryCertsByAccountType(String accttype);

	void updateMemberCert(@Param("mcs") List<MemberCert> mcs);

	@Select("select * from t_member where id=#{id}")
	Member queryMemberById(Integer id);

	void updateAuthCodeAndPstep(Ticket ticket);

	@Update("update t_member set authstatus=#{authstatus} where id=#{id}")
	void updateAuthstatus(Member loginMember);

	Member queryMemberByPiid(String piid);

	List<MemberCert> queryMemberCertByMemberid(String memberid);

	@Update("update t_ticket set status=#{status} where id=#{id}")
	void updateTicketStatus(Ticket ticket);

}
