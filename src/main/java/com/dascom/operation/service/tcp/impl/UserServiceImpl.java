package com.dascom.operation.service.tcp.impl;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dascom.operation.entity.tcp.ControlEntity;
import com.dascom.operation.service.tcp.UserService;
import com.dascom.operation.utils.ResultVOUtil;
import com.dascom.operation.utils.tcp.SubStringResult;
import com.dascom.operation.utils.tcp.TcpHttpUtils;
import com.dascom.operation.vo.ResultVO;


@Component("userService")
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Value("${tcpControlUrl}")
	private String tcpControlUrl;
	
	@Autowired
	private TcpHttpUtils tcpHttp;
	@Autowired
	private SubStringResult subStringResult;

	@Override
	public ResultVO updateUer(String number, String base64Data) {
		
		String uuid = UUID.randomUUID().toString();
		// 64H开始读/写用户数据
		JSONObject readOrWriteObj = tcpHttp.connectControl(tcpControlUrl, new ControlEntity(uuid, number, "10640000", null, null));
		/*String readOrWriteData = readOrWriteObj.getString("data");
		List<String> readOrWriteList = subStringResult.resultList(readOrWriteData);
		if(!readOrWriteList.get(3).equals("00")) {
			logger.error("设备读取用户数据初始化失败,返回错误码:{}",readOrWriteList.get(3));
			return ResultVOUtil.error(1321,"tcp设备操作失败");
		}*/
		logger.info("----设备将完成用户数据的读写初始化,进入等待用户数据的读/写操作状态----");
		//66H写入用户数据
		JSONObject writeObj = tcpHttp.connectControl(tcpControlUrl, new ControlEntity(uuid, number, "10660000", null, base64Data));
		String writeData = writeObj.getString("data");
		List<String> writeList = subStringResult.resultList(writeData);
		if(!writeList.get(3).equals("00")) {
			logger.error("写入用户数据失败,SS码返回值:{}",writeList.get(3));
			return ResultVOUtil.error(1321, "写入用户数据失败");
		}
		logger.info("----写入用户数据成功----");
		return ResultVOUtil.success();
	}

}
