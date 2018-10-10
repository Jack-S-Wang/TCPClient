package com.dascom.operation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dascom.operation.entity.CollectionInterface;
import com.dascom.operation.service.InterfaceService;
import com.dascom.operation.service.RedisService;
import com.dascom.operation.utils.HttpClientUtils;
import com.dascom.operation.utils.MongoJoinPara;
import com.dascom.operation.utils.RedisJoinPara;
import com.dascom.operation.utils.ResultVOUtil;
import com.dascom.operation.vo.ResultVO;


@RestController
public class OperationController {
	
	private static final Logger logger = LogManager.getLogger(OperationController.class);
	
	@Autowired
	private InterfaceService interfaceService;
	
	@Autowired
	private RedisService redisService;

	@Value("${dingdingUrl}")
	private  String dingdingUrl;
	
	
	
	
	/**
	 * 获取接口返回信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getInterfaceData")
	public List<Map<String,Object>> getData() throws Exception{
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		long start = System.currentTimeMillis();
		//判断redis：db15是否为空
		int keysSize = redisService.getAllkey().size();
		if(keysSize==0){
			logger.info("------从mongodb中读取数据------");
			
			List<CollectionInterface> interfaceLists = interfaceService.getAllInterface();
			logger.info("------redis没有数据，从mongodb中导入------");
			for(CollectionInterface inter : interfaceLists){
				MongoJoinPara mongoJoinPara = new MongoJoinPara();
				Map<String,Object> mongoResult = mongoJoinPara.mongoToHttpClient(inter);
				resultList.add(mongoResult);
				redisService.copyData(inter);
				int resultCode = (int)mongoResult.get("statusCode");
				if(resultCode>=400) {
					String requestUrl = inter.getRequestUrl();
					String resultLine = mongoResult.get("resultLine").toString();
					
					logger.info("----接口报错：发送钉钉报警----");
					HttpClientUtils.sendDingding(dingdingUrl,mongoResult, requestUrl);
					logger.info("----错误接口地址----"+requestUrl);
				}
			}
			logger.info("------存入redis成功------");
		}else{
			logger.info("------从redis中读取数据！------");
			Map<String,Map<String,String>> resultMap = redisService.hgetByPipeline();
			for(String key:resultMap.keySet()){
				Map<String,String> interMap = resultMap.get(key);
				RedisJoinPara redisJoinPara = new RedisJoinPara();
				Map<String,Object> resultRedis = redisJoinPara.redisToHttpClient(interMap);
				resultList.add(resultRedis);
				int resultCode = (int)resultRedis.get("statusCode");
				if(resultCode>=400) {
					String requestUrl = interMap.get("requestUrl");
					String resultLine = resultRedis.get("resultLine").toString();
					logger.info("----接口报错：发送钉钉报警----");
					HttpClientUtils.sendDingding(dingdingUrl,resultRedis, requestUrl);
					logger.info("----错误接口地址----"+requestUrl);
				}
			}
		}
		long end = System.currentTimeMillis();
		logger.info("总耗时："+(end-start));
		return resultList;
	}
	
	
	/**
	 * 新增接口
	 * @param inter
	 * @throws Exception
	 */
	@RequestMapping(value="addInter",method=RequestMethod.POST)
	public ResultVO add(@RequestBody String inter) throws Exception {
		JSONObject interObj = JSONObject.parseObject(inter);
		
		if(!interObj.containsKey("interfaceName")) {
			logger.info("缺少接口名称！");
			return ResultVOUtil.error(1301,null);
		}
		
		if(!interObj.containsKey("requestUrl")||!interObj.containsKey("method")) {
			logger.info("缺少参数");
			return ResultVOUtil.error(1301,null);
		}
		
		
		String requestUrl = interObj.get("requestUrl").toString();
		String method = interObj.get("method").toString();
		
		String headerPara = interObj.containsKey("headerParameter")?interObj.get("headerParameter").toString():null;
		String jsonPara = interObj.containsKey("jsonParameter")?interObj.get("headerParameter").toString():null;
		String interfaceName = interObj.get("interfaceName").toString();
		String note = interObj.containsKey("note")?interObj.get("headerParameter").toString():null;
		
		CollectionInterface addInter = new CollectionInterface(interfaceName, method, requestUrl, jsonPara, headerPara, note);
		interfaceService.addInterface(addInter);
		logger.info(addInter.toString());
		logger.info("----新增接口----插入mongodb成功");
		
		redisService.copyData(addInter);
		logger.info("----新增接口----插入redis成功");
		return ResultVOUtil.success();
		
	}
	
	
}
