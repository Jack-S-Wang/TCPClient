package com.dascom.operation.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dascom.operation.service.RedisService;

@RestController
public class RedisController {
	
	@Autowired
	private RedisService redisService;
	
	@RequestMapping("getAllHash")
	public Map<String,Map<Object,Object>> getAll(){
		return redisService.getAllHash();
	}
	
	@RequestMapping("getPrinterStatus")
	public Map<String, String> getPrinterState(){
		return redisService.getPrinterState();
	}
	
}
