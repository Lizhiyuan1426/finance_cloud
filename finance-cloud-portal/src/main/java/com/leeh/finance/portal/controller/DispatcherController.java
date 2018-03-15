package com.atguigu.atcrowdfunding.portal.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.utils.MD5Util;
import com.atguigu.atcrowdfunding.portal.service.MemberService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Controller
public class DispatcherController extends BaseController {

	@Autowired
	private MemberService memberService;

	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/main")
	public String main() {
		return "main";
	}
	
	/**
	 * 登录
	 * @param member
	 * @return
	 */
	@HystrixCommand(fallbackMethod="loginError")
	@ResponseBody
	@RequestMapping("/checkLogin")
	public Object checkLogin(Member member, HttpSession session) {
		start();
		try {
			// 查询会员信息
			Member dbMember = memberService.login(member.getLoginAccount());
			if (dbMember != null) {
				if (dbMember.getMemberpswd().equals(MD5Util.digest(member.getMemberpswd()))){
					session.setAttribute("member", dbMember);
					success();
				}else {
					fail();
				}
			} else {
				fail();
			}
		} catch (Exception e) {
			fail();
		}
		return end();
	}
	
	/**
	 * Hystrix熔断器方法声明
	 * 
	 * @param member
	 * @return
	 */
	public Object loginError(Member member, HttpSession session) {
		System.out.println("error");
		start();
		fail();
		return end();
	}
}
