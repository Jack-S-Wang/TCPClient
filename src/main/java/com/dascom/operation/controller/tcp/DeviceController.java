package com.dascom.operation.controller.tcp;




import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dascom.operation.entity.CollectionPrinters;
import com.dascom.operation.entity.tcp.WifiConfig;
import com.dascom.operation.service.CollectionPrintersService;
import com.dascom.operation.service.RedisService;
import com.dascom.operation.service.tcp.DeviceService;
import com.dascom.operation.utils.ResultVOUtil;
import com.dascom.operation.vo.ResultVO;

@RestController
@RequestMapping(value="/tcp")
public class DeviceController {
	
	private static final Logger logger = LogManager.getLogger(DeviceController.class);
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private CollectionPrintersService printersService;
	
	@Autowired
	private RedisService redisService;
	
	

	/**
		* 
	    * showdoc
	    * @catalog tcp/WIFI固件设备
	    * @title 读取wifi固件信息
	    * @description 读取wifi固件信息
	    * @method GET
	    * @url localhost:13200/readDevice/{number}
	    * @param number 必选 String 设备No号
	    * @return {"code": 0,"data": {"localIpAddress": "192.168.1.10","localSubnetMask": "255.255.255.0","localGateway": "192.168.1.10","remoteServerAddress": "192.168.11.155","mainDNSServerAddress": "114.114.114.114","slaveDNSServerAddress": "114.114.114.115","udpPort": 20000,"controlPort": 20001,"dataPort": 20002,"remoteRouteSsid": "TP123","remoteRoutePassword": "123456789","dhcp": 1,"reconnectionInterval": 3,"apModelSsid": "AisinoCloudPrinter","apModelPassword": "1234567890","networkMode": 0,"dataTransmissionSpeed": 3,"networkChannel": 6,"dns": 0,"deviceModel": 1,"domainName": "yun.dascomyun.cn"}}
	    * @return_param code int 错误码
	    * @return_param data json 返回值
	    * @remark 测试api文档自动生成
	    * @number 1
	*/
	@RequestMapping("/readDevice/{number}")
	public ResultVO read(@PathVariable(value = "number") String number){
		try {
			//判断设备是否存在
			CollectionPrinters printer = printersService.fetchByNumber(number);
			if(StringUtils.isEmpty(printer)) {
				logger.info("----设备不存在----");
				return ResultVOUtil.error(1311, "设备不存在"); //设备不存在
			}
			//判断设备是否被占用
			if(redisService.getAndSet(number)) {
				logger.info("----设备被占用----");
				return ResultVOUtil.error(1312, "设备被占用");
			}
			
			//读取设备信息
			return deviceService.readDevice(number);
		} finally {
			redisService.delUsing(number);
		}
	}
	
	/**
	 * 
		* showdoc
		* @catalog tcp/WIFI固件设备
		* @title 更新WIFi固件信息
		* @description 更新WIFi固件信息
		* @method POST
		* @url localhost:13200/tcp/updateDevice
		* @param number 是 String 设备No号
		* @param data 是 String 更新信息
		* @return {"code":0,"data":null}
		* @return_param code int 错误码
		* @return_param data String 返回值
		* @remark 
		* @number
	 */
	@RequestMapping(value="/updateDevice", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public ResultVO updateDevice(@RequestBody JSONObject obj,HttpServletResponse response) {
		
		String number = null;
		try {
			number = obj.getString("number");
			redisService.delUsing(number);
			WifiConfig wifiConfig = obj.getObject("data", WifiConfig.class);
			//判断参数是否为空
			if(StringUtils.isEmpty(number) || wifiConfig == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("----参数错误----");
				return ResultVOUtil.error(1301, "参数错误");
			}
			//判断是否被占用
			if(redisService.getAndSet(number)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				logger.info("----设备被占用----");
				return ResultVOUtil.error(1312, "设备被占用");
			}
			return deviceService.setDevice(number, wifiConfig);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			logger.info("----服务器内部异常----"+e.toString());
			return ResultVOUtil.error(1300, "服务器内部异常");
		}finally {
			redisService.delUsing(number);
		}
	}
	
	/**
	 * 
		* showdoc
		* @catalog tcp/WIFI固件设备
		* @title 重启WiFi设备
		* @description 重启WiFi设备
		* @method GET
		* @url loalhost:13200/tcp/restartDevice/{number}
		* @param number 是 String 设备No号
		* @return {"code":0,"data":null}
		* @return_param code int 错误码
		* @return_param data String 返回值
		* @remark 
		* @number
	 */
	@RequestMapping("/restartDevice/{number}")
	public ResultVO restart(@PathVariable(value="number")String number,HttpServletResponse response) {
		try {
			return deviceService.RestartDevice(number);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			logger.info("----服务器内部异常---- {}",e.toString());
			return ResultVOUtil.error(1300, "服务器内部异常");
		}
	}
	

}
