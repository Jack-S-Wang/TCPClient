package com.dascom.operation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dascom.operation.entity.PrintStatistics;
import com.dascom.operation.service.PrintStatisticeService;

@RestController
@RequestMapping("/statistics")
public class PrintStatisticsController {
	
	@Autowired
	PrintStatisticeService printStatisticeService;
	
	@GetMapping("index")
	public List<PrintStatistics> getAll(){
		return printStatisticeService.getAll();
	}
	
	/**
	 * 按月份模糊查询
	 */
	@GetMapping("getMonth")
	public List<PrintStatistics> getMonth(){
		String month = "201808";
		return printStatisticeService.getMonth(month);
	}
	
	/**
	 * 查询各设备的使用次数
	 */
	@GetMapping("printTotal")
	public List<String> getTotal(){
		return printStatisticeService.getPrintTotal();
	}
	
	/**
	 * 计算每台设备的成功打印次数
	 */
	@GetMapping("successTimes")
	public String getSuccessTimes(){
		return printStatisticeService.totalSucceedTimes("0030BE7350021A00");
	}
	
	
	/**
	 * 统计所有设备的总打印数
	 */
	@GetMapping("totalPrintTimes")
	public int getPrintTotal(){
		return printStatisticeService.totalPrintTimes();
	}
	
	
	/**
	 * 按月统计设备的打印情况
	 */
	@GetMapping("printMonth")
	public String getPrintMonth(){
		String month = "201808";
		return printStatisticeService.totalPrintWithMonth(month);
	}
	
	
	/**
	 * 统计截止当前月的各月份的打印记录
	 */
	@GetMapping("printWithEachMonth")
	public Map<String,Integer> getEachMonthResult(){
		return printStatisticeService.totalPrintWithPerMonth();
	}

}
