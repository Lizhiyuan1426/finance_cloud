package com.atguigu.atcrowdfunding.activiti.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.atcrowdfunding.activiti.service.MemberService;
import com.atguigu.atcrowdfunding.common.bean.Member;

@RestController
public class ActivitiController {

	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private MemberService memberService;

	@RequestMapping("/pageTaskData")
	public List<Map<String, Object>> pageTaskData(@RequestBody Map<String, Object> map){
		int pageNo = (int) map.get("pageNo");
		int pageSize = (int) map.get("pageSize");
		// activiti框架中对象自己关联自己, 生成JSON字符串时会死循环, 因此采用map和对象生成JSON的都是大括号
		// 分别把每一个属性赋给map
		List<Map<String, Object>> list = new ArrayList<>();
		// 查询
		List<Task> tasks = taskService.createTaskQuery()
				.processDefinitionKey("authflow")
				.taskCandidateGroup("manager")
				.listPage((pageNo - 1) * pageSize, pageSize);

		// 代替对象
		for (Task task : tasks) {
			Map<String, Object> taskMap = new HashMap<>();
			taskMap.put("id", task.getId());
			// task -> pd
			String pdid = task.getProcessDefinitionId();
			// 获取processDefinition
			ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(pdid)
					.singleResult();
			taskMap.put("pdname", pd.getName());
			taskMap.put("pdversion", pd.getVersion());
			taskMap.put("name", task.getName());
			// task --> ProcessInstance --> ticket --> member
			String piid = task.getProcessInstanceId();
			Member member = memberService.queryMemberByPiid(piid);
			taskMap.put("memberid", member.getId());
			taskMap.put("membername", member.getMembername());
			// 把每一个map放入list中
			list.add(taskMap);
		}
		return list;
	}
	
	@RequestMapping("/pageTaskCount")
	public int pageTaskCount() {
		int count = (int)taskService.createTaskQuery().processDefinitionKey("authflow")
				.taskCandidateGroup("manager").count();
		return count;
	}
	
	@RequestMapping("/sendMail")
	public String sendMail(@RequestBody Member loginMember) {
		// 查询任务
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey("authflow").taskAssignee(loginMember.getLoginAccount())
				.list();
		Map<String, Object> map = new HashMap<>();
		//map.put("status", "next");
		map.put("userEmail", loginMember.getEmail());
		// 验证码
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			code.append(new Random().nextInt(10));
		}
		map.put("authcode", code.toString());
		for (Task task : tasks) {
			// 完成任务
			taskService.complete(task.getId(), map);
		}
		return code.toString();
	}
	
	@RequestMapping("/endProcess/{taskid}")
	public void endProcess(@PathVariable("taskid") String taskid) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "pass");
		taskService.complete(taskid, map);
	}
	
	@RequestMapping("/nextProcess/{loginAccount}")
	public void nextProcess(@PathVariable("loginAccount") String loginAccount) {
		// 查询任务
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey("authflow").taskAssignee(loginAccount)
				.list();
		Map<String, Object> map = new HashMap<>();
		map.put("status", "next");
		for (Task task : tasks) {
			// 完成任务
			taskService.complete(task.getId(), map);
		}
	}
	
	/**
	 * 流程往下执行, 到基本信息页面
	 * 
	 * @param loginAccount
	 * @return
	 */
	@RequestMapping("/process/{loginAccount}")
	public void process(@PathVariable("loginAccount") String loginAccount) {
		// 查询任务
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey("authflow").taskAssignee(loginAccount)
				.list();
		for (Task task : tasks) {
			// 完成任务
			taskService.complete(task.getId());
		}
	}
	
	/**
	 * 启动流程
	 * @param loginAccount
	 * @return 返回流程实例id
	 */
	@RequestMapping("/getProcessInstanceId/{loginAccount}")
	public String getProcessInstanceId(@PathVariable("loginAccount") String loginAccount) {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		// 查询实名认证流程
		ProcessDefinition pd = query.processDefinitionKey("authflow").latestVersion().singleResult();
		// 启动流程, 设定流程变量
		Map<String, Object> pdMap = new HashMap<>();
		pdMap.put("loginAccount", loginAccount);
		ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId(), pdMap);
		return pi.getId();
	}
	
	@RequestMapping("/delete/{id}")
	public void delete(@PathVariable("id") String id) {
		// 根据id查询流程定义
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd = query.processDefinitionId(id).singleResult();
		// 删除流程定义
		// 级联删除 deploy ==> proDef
		repositoryService.deleteDeployment(pd.getDeploymentId(), true);
	}
	
	/**
	 * 接收文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/depolyProcDef")
	public String depolyProcDef( @RequestParam("pdfile") MultipartFile file ) throws IOException {
		// 部署流程定义
		repositoryService.createDeployment()
		//.addClasspathResource(file.getOriginalFilename())
		.addInputStream(file.getOriginalFilename(), file.getInputStream())
		.deploy();
		return "success";
	}
	
	/**
	 * 返回字节数组
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("loadImgById/{id}")
	public byte[] loadImgById(@PathVariable("id") String id) {
		// 根据id查询流程定义
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd = query.processDefinitionId(id).singleResult();
		// 图形名
		String imgName = pd.getDiagramResourceName();
		// 部署id
		String deploymentId = pd.getDeploymentId();
		// 获取输入流
		InputStream in = repositoryService.getResourceAsStream(deploymentId, imgName);
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
		byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据 
		int rc = 0; 
		try {
			while ((rc = in.read(buff, 0, 100)) > 0) { 
			    swapStream.write(buff, 0, rc); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
		
		return in_b;
	}

	
	@RequestMapping("/pageQuery")
	public List<Map<String, Object>> index(@RequestBody Map<String, Object> map) {
		int pageNo = (int) map.get("pageNo");
		int pageSize = (int) map.get("pageSize");
		// activiti框架中对象自己关联自己, 生成JSON字符串时会死循环, 因此采用map和对象生成JSON的都是大括号
		// 分别把每一个属性赋给map
		List<Map<String, Object>> list = new ArrayList<>();
		// 查询
		List<ProcessDefinition> pages = repositoryService.createProcessDefinitionQuery()
				.listPage((pageNo - 1) * pageSize, pageSize);
		
		// 代替对象
		for (ProcessDefinition page : pages) {
			Map<String, Object> pdMap = new HashMap<>();
			pdMap.put("id", page.getId());
			pdMap.put("name", page.getName());
			pdMap.put("key", page.getKey());
			pdMap.put("version", page.getVersion());
			// 把每一个map放入list中
			list.add(pdMap);
		}
		return list;
	}
	
	@RequestMapping("/pageQueryCount")
	public int pageQueryCount() {
		// 总数
		return (int) repositoryService.createProcessDefinitionQuery().count();
	}

}
