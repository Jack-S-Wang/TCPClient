package com.dascom.operation.service.tcp.impl;


import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dascom.operation.entity.tcp.ControlEntity;
import com.dascom.operation.entity.tcp.WifiConfig;
import com.dascom.operation.service.tcp.DeviceService;
import com.dascom.operation.utils.ResultVOUtil;
import com.dascom.operation.utils.tcp.AnalyseMessage;
import com.dascom.operation.utils.tcp.BytesHexStrTranslate;
import com.dascom.operation.utils.tcp.SubStringResult;
import com.dascom.operation.utils.tcp.TcpHttpUtils;
import com.dascom.operation.vo.ResultVO;

@Component("deviceService")
public class DeviceServiceImpl implements DeviceService{
	
	private static final Logger logger = LogManager.getLogger(DeviceServiceImpl.class);
	
	@Value("${tcpControlUrl}")
	private String tcpControlUrl;
	
	@Autowired
	private TcpHttpUtils tcpHttp;
	@Autowired
	private SubStringResult subStringResult;

	@Override
	public ResultVO readDevice(String number){
		JSONObject obj = tcpHttp.connectControl(tcpControlUrl, new ControlEntity(UUID.randomUUID().toString(), number, "10170000", "00", null));
		String data = obj.getString("data");
		byte[] message = BytesHexStrTranslate.hexToByteArray(data);
		if(message.length <= 20+4+8) {
			logger.info("----读取wifi配置失败----");
			return ResultVOUtil.error(1312, "读取wifi配置失败");
		}else {
			WifiConfig wifiConfig = AnalyseMessage.getWifiConfig(message, 20+4+8);
			logger.info("----读取wifi配置成功----"+wifiConfig.toString());
			return ResultVOUtil.success(wifiConfig);
		}
		
	}

	@Override
	public ResultVO setDevice(String number,WifiConfig wifiConfig) {
		byte[] message = AnalyseMessage.getWifiConfigtoByte(wifiConfig);
		String instruct = BytesHexStrTranslate.bytesToHex(Arrays.copyOf(message, 4));
		System.out.println(instruct);
		byte[] data = Arrays.copyOfRange(message, 4, message.length);
		String configData = Base64.encodeBase64String(data);
		System.out.println(configData);
		JSONObject obj = tcpHttp.connectControl(tcpControlUrl, new ControlEntity(UUID.randomUUID().toString(), number, instruct, "00", configData));
		String result = obj.getString("data");
		List<String> resultList = subStringResult.resultList(result);
		if(resultList.get(3).equals("00")) {
			logger.info("---- 设备配置更新成功----");
			return ResultVOUtil.success();
		}else {
			logger.info("---- 设备配置更新失败 ----");
			return ResultVOUtil.error(1313, "设备更新失败！返回SS值："+resultList.get(3));
		}
	}
	
	@Override
	public ResultVO RestartDevice(String number) {
		// 11H  设置设备重启
		JSONObject obj = tcpHttp.connectControl(tcpControlUrl, new ControlEntity(UUID.randomUUID().toString(), number, "10110000", "00", null));
		String data = obj.getString("data");
		List<String> result = subStringResult.resultList(data);
		if(result.get(2).equals("00")&&result.get(3).equals("00")) {
			logger.info("----设备重启成功----");
		}else {
			logger.info("----设备重启失败----");
			ResultVOUtil.error(1302, "设备重启失败");
		}
		return ResultVOUtil.success();
	}

}
