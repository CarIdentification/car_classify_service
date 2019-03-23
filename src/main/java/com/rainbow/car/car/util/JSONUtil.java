package com.rainbow.car.car.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class JSONUtil {
  public static JSONArray jsonArrayCovert(org.json.JSONArray jsonArray){
    return JSON.parseArray(jsonArray.toString());
  }
}
