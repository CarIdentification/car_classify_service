package com.rainbow.car.car.service;

import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.rainbow.car.car.dao.CarDao;
import com.rainbow.car.car.util.ClassifyClient;
import com.rainbow.car.car.util.FileUtil;
import com.rainbow.car.car.util.JSONUtil;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author caijiatao
 * @author huangzhuodong
 * @date 2019/3/12 4:31 PM
 */
@Service
public class CarClassifyService {

  @Autowired
  private CarDao carDao;

  private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  private final String ROOT_PATH;
  private final AipImageClassify client;

  public CarClassifyService(@Value("${rainbow.file.root}") String ROOT_PATH) {
    //单例Service初始化时从配置文件加载根目录
    this.ROOT_PATH = ROOT_PATH;
    //获取分类客户端
    client = ClassifyClient.getClient();
    // 可选：设置网络连接参数
    client.setConnectionTimeoutInMillis(2000);
    client.setSocketTimeoutInMillis(60000);
  }


  public String saveToClassifyDirectory(MultipartFile carImg) {
    try {
      return FileUtil.saveFile(ROOT_PATH, carImg, "classify", carImg.getName());
    } catch (IOException e) {
      logger.error("保存图片文件时失败", e);
      return null;
    }
  }

  // 车型识别sample
  public JSONObject insideInterfaceClassifyCar(String filePath) {



    return new JSONObject();
  }


  // 车型识别sample
  public JSONArray outsideInterfaceClassifyCar(String filePath) {
    // 传入可选参数调用接口
    HashMap<String, String> options = new HashMap<>();
    options.put("top_num", "3");
    options.put("baike_num", "5");

    // 参数为本地路径
    JSONObject res = client.carDetect(filePath, options);
    System.out.println(res.toString(2));
    org.json.JSONArray arr = res.getJSONArray("result");
    JSONArray resultArray = new JSONArray();
    for(int i = 0; i < arr.length();i++){
      JSONObject obj = arr.getJSONObject(i);
      String name = obj.getString("name");
      Integer carId = carDao.queryCarIdByNameLike(name);
      if(carId != null){
        resultArray.add(carId);
      }
    }

    return resultArray;


  }

}
