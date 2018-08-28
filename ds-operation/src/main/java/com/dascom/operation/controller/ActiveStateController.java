package com.dascom.operation.controller;

import java.util.ArrayList;
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
	
	@RequestMapping("active")
	public List<ActiveState> getAll(){
		return activeStateService.getAll();
	}
	
	@RequestMapping("getService")
	public void getService() {
		activeStateService.getOnline();
	}
	
	@RequestMapping("getNowDay")
	public List<ActiveState> nowDay(){
		return activeStateService.getByNowDay();
	}
	
	@RequestMapping(value="getActive",method=RequestMethod.GET)
	public Map<String,String> getActive(@RequestParam(defaultValue="0") double time){
		Map<String,String>resultMap = new HashMap<String,String>();
		List<ActiveState> actives = new ArrayList<ActiveState>();
		if(time==0) {
			actives = activeStateService.getByNowDay();
		}else {
			actives = activeStateService.getActiveDevice(time);
		}
		
		for(ActiveState active : actives) {
			String activeId = active.getActiveId();
			long onlineTime = active.getOnlineTime();
			String runTime = FormatDate.formatDuring(onlineTime);
			resultMap.put(activeId, runTime);
		}
		
		return resultMap;
	}

}
