package com.atguigu.atcrowdfunding.manager.servcie;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("eureka-activiti-service")
public interface ProcessService {

	/**
	 * @RequestBody 放在请求体中传对象
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/pageQuery")
	public List<Map<String, Object>> index(@RequestBody Map<String, Object> map);
	
	@RequestMapping("/pageQueryCount")
	public int pageQueryCount();
	
	@RequestMapping("/delete/{id}")
	public void delete(@PathVariable("id") String id);

	@RequestMapping("/pageTaskData")
	public List<Map<String, Object>> pageTaskData(@RequestBody Map<String, Object> map);

	@RequestMapping("/pageTaskCount")
	public int pageTaskCount();
}
