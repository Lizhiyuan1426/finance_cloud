package com.atguigu.atcrowdfunding.portal.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Ticket;
import com.atguigu.atcrowdfunding.portal.service.ActiService;
import com.atguigu.atcrowdfunding.portal.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private ActiService actiService;
	
	/**
	 * 申请完成
	 * 
	 * @param authcode
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/finishApply")
	public Object finishApply(String authcode, HttpSession session) {
		start();
		try {
			Member loginMember = (Member) session.getAttribute("member");
			// 验证码是否正确
			Ticket ticket = memberService.queryTicketByMemberId(loginMember.getId());
			if(authcode.equals(ticket.getAuthcode())) {
				// 更新用户申请状态
				loginMember.setAuthstatus("1");
				session.setAttribute("member", loginMember);
				memberService.updateAuthstatus(loginMember);
				success();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	/**
	 * 发送邮件
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendMail")
	public Object sendMail(HttpSession session, String email) {
		start();
		try {
			Member loginMember = (Member) session.getAttribute("member");
			loginMember.setEmail(email);
			session.setAttribute("member", loginMember);
			// 更新邮箱和验证码
			memberService.updateEmail(loginMember);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	/**
	 * 验证邮箱
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/uploadcert")
	public Object uploadcert(HttpSession session, Datas mcs) {
		start();
		try {
			// 获取session域的会员
			Member loginMember = (Member) session.getAttribute("member");
			for (MemberCert mc : mcs.getMcs()) {
				mc.setMemberid(loginMember.getId());
				
				// 保存图片
				MultipartFile file = mc.getFile();
				String filename = file.getOriginalFilename();
				String substring = filename.substring(filename.lastIndexOf("."));
				String imgName = UUID.randomUUID().toString() + substring;
				File dest = new File("D:\\Study\\resources\\img\\cert\\" + imgName);
				file.transferTo(dest);
				
				mc.setIconpath(imgName);
				mc.setFile(null);
			}
			// 更新
			memberService.updateMemberCert(mcs.getMcs());
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/updateBasicInfo")
	public Object updateBasicInfo(HttpSession session, Member member) {
		start();
		try {
			// 获取session域的会员
			Member loginMember = (Member) session.getAttribute("member");
			// 设置基本信息
			loginMember.setRealname(member.getRealname());
			loginMember.setCardnum(member.getCardnum());
			loginMember.setTel(member.getTel());
			
			// 更新会员账户
			memberService.updateBasicInfo(loginMember);
			session.setAttribute("member", loginMember);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	/**
	 * 更新账户类型
	 * @param session
	 * @param member
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAccountType")
	public Object updateAccountType(HttpSession session, Member member) {
		start();
		try {
			// 获取session域的会员
			Member loginMember = (Member) session.getAttribute("member");
			// 给登录会员设置账户类型
			loginMember.setAccttype(member.getAccttype());
			// 更新会员账户
			memberService.updateAccountType(loginMember);
			// 分布式环境中的session数据保存在缓存服务器中, 当修改了session的数据时,
			// 应当调用setAttribute()方法更新缓存服务器中的数据
			session.setAttribute("member", loginMember);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	@RequestMapping("/apply")
	public String apply(HttpSession session, Model model) {
		// 获取session域的会员
		Member member = (Member) session.getAttribute("member");
		
		// 查询申请流程ticket
		Ticket ticket = memberService.queryTicketByMemberId(member.getId());
		if(ticket == null) {
			// 启动流程, 并获取流程实例ID
			String piid = actiService.getProcessInstanceId(member.getLoginAccount());
			
			// 账户类型选择页面
			ticket = new Ticket();
			ticket.setPstep("apply-accttype");
			ticket.setStatus("0");
			ticket.setPiid(piid);
			ticket.setMemberid(member.getId());
			
			memberService.insertTicket(ticket);
			
			return "member/apply-accttype";
		}else {
			if("apply-info".equals(ticket.getPstep())) {
				return "member/apply-info";
			}else if("apply-cert".equals(ticket.getPstep())) {
				// 查询当前会员需要提供的资质文件列表
				List<Cert> certs = memberService.queryCertsByAccountType(member.getAccttype());
				// 放入model中
				model.addAttribute("certs", certs);
				return "member/apply-cert";
			}else if("email".equals(ticket.getPstep())) {
				return "member/apply-email";
			}else if("checkcode".equals(ticket.getPstep())) {
				return "member/apply-checkcode";
			}else {
				return "member/apply-accttype";
			}
		}
	}
}
