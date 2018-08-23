package com.dascom.operation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dascom.operation.entity.CollectionInterface;
import com.dascom.operation.service.InterfaceService;
import com.dascom.operation.service.RedisService;
import com.dascom.operation.utils.MongoJoinPara;
import com.dascom.operation.utils.RedisJoinPara;


@RestController
public class InterfaceController {
	
	private static final Logger logger = LogManager.getLogger(InterfaceController.class);
	
	@Autowired
	private InterfaceService interfaceService;
	
	@Autowired
	private RedisService redisService;
	
	@Value("${domainName}")
	String domain;
	
	@RequestMapping("localInterface")
	public List<CollectionInterface> getLocalInterface(){
		return interfaceService.getAllInterface();
	}
	
	
	@RequestMapping("getData")
	public List<Map<String,Object>> save() throws Exception{
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		long start = System.currentTimeMillis();
		//判断redis：db15是否为空
		int keysSize = redisService.getAllkey().size();
		if(keysSize==0){
			logger.info("------从mongodb中读取数据------");
			List<CollectionInterface> interfaceLists = interfaceService.getAllInterface();
			for(CollectionInterface inter : interfaceLists){
				MongoJoinPara mongoJoinPara = new MongoJoinPara();
				resultList.add(mongoJoinPara.mongoToHttpClient(inter, domain));
				logger.info("redis没有数据，从mongodb中导入");
				redisService.copyData(inter);
			}
			logger.info("存入redis成功");
		}else{
			logger.info("从redis中读取数据！");
			Map<String,Map<Object,Object>> resultMap = redisService.getAllHash();
			//Map<String,Map<Object,Object>> resultMap = redisService.testRedis();
			for(String key:resultMap.keySet()){
				Map<Object,Object> interMap = resultMap.get(key);
				RedisJoinPara redisJoinPara = new RedisJoinPara();
				resultList.add(redisJoinPara.redisToHttpClient(interMap, domain));
			}
		}
		long end = System.currentTimeMillis();
		logger.info("总耗时："+(end-start));
		
		return resultList;
	}
	
	
	
	
	

}
