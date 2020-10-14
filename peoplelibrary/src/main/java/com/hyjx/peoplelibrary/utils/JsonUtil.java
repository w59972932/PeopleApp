package com.hyjx.peoplelibrary.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Json数据解析
 * @author wangzhengfu
 *
 */
public class JsonUtil {
	//返回结果类型
	public static final int MAP = 0;
	public static final int LIST_MAP = 1;
	public static final int LIST_LIST_MAP = 2;
	
	
	/**
	 * json解析
	 * @param jsonStr json数据
	 * @param dataType 解析返回结果类型  HttpUtil.TYPE_*
	 * @return
	 */
	public static Map<String, Object> parseJsonStr(String jsonStr, int dataType){
		
		Map<String, Object> map = JSON.parseObject(jsonStr,
                new TypeReference<Map<String, Object>>() {
                });
		//返回结果
		Object dataJsonStr = map.get("data");
		if(dataJsonStr != null && !"".equals(dataJsonStr)){
			switch (dataType) {
			case MAP:
				map.put("data", JSON.parseObject(dataJsonStr.toString(), new TypeReference<Map<String, String>>() {
                }));
				break;
			case LIST_MAP:
				map.put("data", JSON.parseObject(dataJsonStr.toString(), new TypeReference<List<Map<String, String>>>() {
                }));
				break;
			case LIST_LIST_MAP:
				map.put("data", JSON.parseObject(dataJsonStr.toString(), new TypeReference<List<List<Map<String, String>>>>() {
                }));
				break;
			}
		}
		return map;
	}
	
	/**
	 * json解析
	 * @param jsonStr json数据
	 * @return
	 */
	public static Map<String, String> parseJsonStr(String jsonStr){
		return JSON.parseObject(jsonStr,
                new TypeReference<Map<String, String>>() {
                });
	}

    /**
     * json解析
     * @param jsonStr json数据
     * @return
     */
    public static Map<String, Object> parseJson(String jsonStr){
        return JSON.parseObject(jsonStr,
                new TypeReference<Map<String, Object>>() {
                });
    }
	
	/**
	 * json解析
	 * @param jsonStr json数据
	 * @return
	 */
	
	public static ArrayList<Map<String, String>> parseJsonStrToListmap(String jsonStr){
		return JSON.parseObject(jsonStr,
                new TypeReference<ArrayList<Map<String, String>>>() {
                });
	}

    /**
     * json解析
     * @param jsonStr json数据
     * @return
     */

    public static ArrayList<Map<String, Object>> parseJsonToListmap(String jsonStr){
        return JSON.parseObject(jsonStr,
                new TypeReference<ArrayList<Map<String, Object>>>() {
                });
    }

    public static Map<String, String> parse2Map(String jsonStr){
        if(null == jsonStr || "".equals(jsonStr)){
            return null;

        }
        return JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
        });
    }

    public static String listmap2Json(List<Map<String, Object>> list) {
        return JSON.toJSONString(list, true);
    }
	public static String listomap2Json(List<Map<String, String>> list) {
		return JSON.toJSONString(list, true);
	}
}
