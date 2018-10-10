package com.dascom.operation.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dascom.operation.service.OpenidStatisticsService;

@RestController
public class OpenidController {
	
	@Autowired
	private OpenidStatisticsService openidStatisticsService;
	
	@RequestMapping("findOpenidByDay")
	public Map<String,Object> findByDay(@RequestParam(defaultValue="1")int pageNum){
		return openidStatisticsService.getOpenidStatistics(pageNum);
	}
	
	@RequestMapping("findOpenidByMonth")
	public Map<Object,Object> findByMonth(){
		return openidStatisticsService.getOpenidStatisticsPerMonth();
		
	}

}
