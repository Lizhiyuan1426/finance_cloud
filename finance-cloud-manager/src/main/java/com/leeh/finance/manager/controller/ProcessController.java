package com.atguigu.atcrowdfunding.manager.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.common.bean.Page;
import com.atguigu.atcrowdfunding.manager.servcie.ProcessService;

@Controller
@RequestMapping("/process")
public class ProcessController extends BaseController {

	@Autowired
	private ProcessService processService;
	
	@Autowired
	private RestTemplate restTemplate;


	@RequestMapping("/index")
	public String index() {
		return "process/index";
	}
	
	@ResponseBody
	@RequestMapping("/delete/{id}")
	public Object delete(@PathVariable("id") String id) {
		start();
		try {
			processService.delete(id);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	/**
	 * 去显示流程图页面
	 * 
	 * @return
	 */
	@RequestMapping("/showImg")
	public String showImg(String id, Model model) {
		model.addAttribute("pdid", id);
		return "process/show";
	}
	
	/**
	 * 显示流程图, 将从activiti接收的字节数组转换为流再生成文件
	 * 
	 * @param id
	 * @throws IOException 
	 */
	@RequestMapping("/loadImg/{id}")
	public void loadImg(@PathVariable("id") String id, HttpServletResponse resp) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		
		String url = "http://eureka-activiti-service/loadImgById/"+id;
		ResponseEntity<byte[]> response = restTemplate.exchange(
	        url,
	        HttpMethod.POST,
	        new HttpEntity<byte[]>(headers),
	        byte[].class); 
	    byte[] result = response.getBody();

	    InputStream in = new ByteArrayInputStream(result);
		OutputStream out = resp.getOutputStream();
		
		int i = -1;
		while ( (i = in.read()) != -1 ) {
			out.write(i);
		}

	}
	
	/**
	 * 上传流程定义图
	 * 
	 * @param file
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/upload")
	public Object upload(@RequestParam("procDefFile") MultipartFile file) {
		start();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			String uuid = UUID.randomUUID().toString();
			String fileName = file.getOriginalFilename();
			final File tempFile = File.createTempFile(uuid, fileName.substring(fileName.lastIndexOf(".")));
			file.transferTo(tempFile);
			FileSystemResource resource = new FileSystemResource(tempFile);
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
			param.add("pdfile", resource);
			// 发送文件
			restTemplate.postForObject("http://eureka-activiti-service/depolyProcDef", param, String.class);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
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
			List<Map<String, Object>> pdMap = processService.index(map);
			// 总记录数
			int count = processService.pageQueryCount();
			Page<Map<String, Object>> pdPage = new Page<>();
			pdPage.setDatas(pdMap);
			pdPage.setTotalPageSize(count);
			pdPage.setPageNo(pageNo);
			pdPage.setPageSize(pageSize);
			// 放数据
			data(pdPage);
			success();
		} catch (Exception e) {
			fail();
		}
		return end();
	}
}
