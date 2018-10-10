package com.dascom.operation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dascom.operation.entity.CollectionPrinterActiveStatistics;
import com.dascom.operation.service.CollectionPrintersService;

@RestController
public class CloudDeviceController {
	
	@Autowired
	private CollectionPrintersService printersService;
	
	@RequestMapping("getAllDevice")
	public int getAll(){
		return printersService.getAllDevice();
	}
	
	//统计每日设备登陆数
	@RequestMapping(value="getOnlineDevice")
	public Map<String,Object> getOnlineDevice(@RequestParam(defaultValue="1")int pageNum){
		return printersService.getOnlineDevice(pageNum);
	}
	
	//统计每月设置登陆数
	@RequestMapping("getOnlinePerMonth")
	public Map<String,Integer> getOnlinePerMonth(){
		return printersService.getOnlinePerMonth();
	}

}
