package com.atguigu.atcrowdfunding.manager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.common.bean.MemberCert;
import com.atguigu.atcrowdfunding.common.bean.Page;
import com.atguigu.atcrowdfunding.manager.servcie.MemberService;
import com.atguigu.atcrowdfunding.manager.servcie.ProcessService;

@RequestMapping("/auth")
@Controller
public class AuthController extends BaseController {

	@Autowired
	private ProcessService processService;
	
	@Autowired
	private MemberService memberService;
	
	@ResponseBody
	@RequestMapping("/finishAuth")
	public Object finishAuth(Integer memberid, String taskid) {
		start();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("memberid", memberid);
			map.put("taskid", taskid);
			memberService.finishAuth(map);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	@RequestMapping("/detail")
	public String detail(String memberid, String taskid, Model model) {
		List<MemberCert> mcs = memberService.queryMemberCertByMemberid(memberid);
		model.addAttribute("mcs", mcs);
		model.addAttribute("memberid", memberid);
		model.addAttribute("taskid", taskid);
		return "auth/detail";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "auth/index";
	}
	
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(Integer pageNo, Integer pageSize) {
		start();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("pageNo", pageNo);
			map.put("pageSize", pageSize);
			// 查询出的数据
			List<Map<String, Object>> pdMap = processService.pageTaskData(map);
			// 总记录数
			int count = processService.pageTaskCount();
			Page<Map<String, Object>> taskPage = new Page<>();
			taskPage.setDatas(pdMap);
			taskPage.setTotalPageSize(count);
			taskPage.setPageNo(pageNo);
			taskPage.setPageSize(pageSize);
			// 放数据
			data(taskPage);
			success();
		} catch (Exception e) {
			fail();
		}
		return end();
	}
}
