package com.dascom.operation.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dascom.operation.entity.ActiveState;
import com.dascom.operation.service.ActiveStateService;
import com.dascom.operation.utils.FormatDate;

@RestController
public class ActiveStateController {
	
	@Autowired
	private ActiveStateService activeStateService;
	
	
	
	//获取活跃设备（可按时长查询）
	@RequestMapping(value="getActiveDevice",method=RequestMethod.GET)
	public Map<String,Object> getActive(@RequestParam(defaultValue="0") double time,@RequestParam(defaultValue="1") int pageNum){
		//查询设备在线时长
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		Map<String,Object>resultMap = new HashMap<String,Object>();
		if(time==0) {
			resultMap = activeStateService.getByNowDay(pageNum);
		}else {
			resultMap = activeStateService.getActiveDevice(time,pageNum);
		}
		
		
		return resultMap;
	}

}
