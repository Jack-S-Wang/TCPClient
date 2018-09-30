package com.dascom.operation.controller.tcp;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dascom.operation.service.RedisService;
import com.dascom.operation.service.tcp.UserService;
import com.dascom.operation.service.tcp.impl.UserServiceImpl;
import com.dascom.operation.utils.ResultVOUtil;
import com.dascom.operation.vo.ResultVO;

@RestController
@RequestMapping(value="/tcp")
public class UserController {
	
	private static final Logger logger = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Value("${tcpUserMessageEncode}")
	private String tcpUserMessageEncode;
	
	@Autowired
	private RedisService redisService;
	
	@RequestMapping(value="/writeUser", method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public ResultVO writeUser(@RequestBody JSONObject json,HttpServletResponse response) {
		String number = null;
		String data = null;
		try {
			
			number = json.getString("number");
			data = json.getString("data");
			
			redisService.delUsing(number);
			
			if(StringUtils.isEmpty(number) || StringUtils.isEmpty(data)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("----参数错误----");
				return ResultVOUtil.error(1301, "参数错误");
			}
			byte[] bytesData = data.getBytes(tcpUserMessageEncode);

			if(bytesData.length>3583) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("----写入wifi的用户信息过大----");
				return ResultVOUtil.error(1323, "写入wifi的用户信息过大");
			}
			if(redisService.getAndSet(number)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("----设备被占用----");
				return ResultVOUtil.error(1303, "设备被占用");
			}
			
			String base64Data = Base64.encodeBase64String(bytesData);
			
			return userService.updateUer(number, base64Data);
			//return null;
		} catch (Exception e) {
			logger.error(e.toString());
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			logger.info("----服务器内部错误---- {}",e.toString());
			return ResultVOUtil.error(1300, "服务器内部错误");
		}finally {
			redisService.delUsing(number);
		}
	}

}
