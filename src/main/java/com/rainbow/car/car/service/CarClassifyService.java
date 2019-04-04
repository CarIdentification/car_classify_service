package com.rainbow.car.car.service;

import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.rainbow.car.car.dao.CarDao;
import com.rainbow.car.car.util.ClassifyClient;
import com.rainbow.car.car.util.FileUtil;
import com.rainbow.car.car.util.HttpClientUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

  private Pattern PURE_NAME_PATTERN = Pattern.compile("【(.*?)图片】.*?_.*?图片_汽车图片库_汽车之家");

  private static final String AUTOHOME_PREFIX = "汽车之家 ";

  private ConcurrentHashMap<String,String> pureNameCache = new ConcurrentHashMap<>();

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
  public JSONArray outsideInterfaceClassifyCar(String filePath,Integer userId) {
    // 传入可选参数调用接口
    HashMap<String, String> options = new HashMap<>();
    options.put("top_num", "3");

    // 参数为本地路径
    JSONObject res = client.carDetect(filePath, options);
    System.out.println(res.toString(2));
    org.json.JSONArray arr = res.getJSONArray("result");
    JSONArray resultArray = new JSONArray();
    for (int i = 0; i < arr.length(); i++) {
      JSONObject obj = arr.getJSONObject(i);
      String name = obj.getString("name");

      if(pureNameCache.contains(name)){
        name = pureNameCache.get(name);
      }else{
        //这里不做并发控制
        String pureName = carPureNameResolve(name);
        pureNameCache.put(name,pureName);
        name = pureName;
      }

      logger.info(name);
      Integer carId = carDao.queryCarIdByNameLike(name);
      if (carId != null) {
        resultArray.add(carId);
      }
    }

    // 写入搜索历史
    userId = userId == null ? 0 : userId;
    logger.info(filePath + "userId : " + userId);
    carDao.insertClassifyHistory(userId,filePath);

    return resultArray;
  }

  private String carPureNameResolve(String name) {
    HashMap<String, Object> param = new HashMap<>();
    param.put("wd", AUTOHOME_PREFIX + name);
    String html = HttpClientUtil.getForString("http://www.baidu.com/s", param);
    if (html != null) {
      Document doc = Jsoup.parse(html);
      String text = doc.body().text();

      Matcher matcher = PURE_NAME_PATTERN.matcher(text);
      if (matcher.find()) {
        String rs = matcher.group(1);
        if(rs.indexOf('】')>=0){
          rs = rs.substring(0,rs.indexOf('】'));
          return rs;
        }
      }
    }
    return name;
  }
}
