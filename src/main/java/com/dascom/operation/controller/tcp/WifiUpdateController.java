package com.dascom.operation.controller.tcp;

import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.dascom.operation.entity.CollectionPrinters;
import com.dascom.operation.service.CollectionPrintersService;
import com.dascom.operation.service.RedisService;
import com.dascom.operation.service.tcp.UpdateWifiService;
import com.dascom.operation.utils.ResultVOUtil;
import com.dascom.operation.vo.ResultVO;


@RestController
@RequestMapping(value="/tcp")
public class WifiUpdateController {
	
	private static final Logger logger = LogManager.getLogger(WifiUpdateController.class);
	
	@Autowired
	private UpdateWifiService updateWifiService;
	
	@Autowired
	private CollectionPrintersService printersService;
	
	@Autowired
	private RedisService redisService;
	
	@Value("${tcpUserMessageEncode}")
	private String tcpUserMessageEncode;
	

	
	@RequestMapping(value="/updateWifi",method=RequestMethod.POST,produces = "application/json;charset=utf-8")
	public ResultVO update(@RequestBody JSONObject obj ,HttpServletResponse response) {
		String number = null;
		String base64Data = null;
		try {
			number = obj.getString("number");
			base64Data = obj.getString("data");
			
			//判断参数是否为空
			if(StringUtils.isEmpty(number) || StringUtils.isEmpty(base64Data)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("----参数错误----");
				return ResultVOUtil.error(1301, "参数错误");
			}
			//判断设备是否存在
			CollectionPrinters printer = printersService.fetchByNumber(number);
			if(printer == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("----设备不存在----");
				return ResultVOUtil.error(1302, "设备不存在");
			}
			//判断设备是否被占用
			if(redisService.getAndSet(number)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("----设备被占用----");
				return ResultVOUtil.error(1303, "设备被占用");
			}
			//更新设备
			return updateWifiService.updateWifi(number, base64Data);
		}catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			logger.info("----服务器内部异常---- {}",e.toString());
			return ResultVOUtil.error(1300, "服务器内部异常");
		}finally {
			redisService.delUsing(number);
		}
		
	}
	
}
