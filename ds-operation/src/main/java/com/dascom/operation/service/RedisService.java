package com.dascom.operation.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {
	
	//获取所有key
	Set<String> getAllkey();
	
	//获取所有hash中的key和对应的map
	Map<String,Map<Object,Object>> getAllHash();
	
	//从mongodb复制数据到redis
	void copyData(Object obj) throws Exception;
	
	Map<String, Map<String, String>> test();
}
