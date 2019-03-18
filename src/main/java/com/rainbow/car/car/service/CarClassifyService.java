package com.rainbow.car.car.service;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.rainbow.car.car.util.ClassifyClient;
import com.rainbow.car.car.util.FileUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author caijiatao
 * @date 2019/3/12 4:31 PM
 */
@Service
public class CarClassifyService {
    public JSONObject carClassify(MultipartFile carImg){
        try {
            String fileAbsPath = FileUtil.saveFile("/Users/cai/car/",carImg,"classify",carImg.getName());

            AipImageClassify client = ClassifyClient.getClient();
            // 可选：设置网络连接参数
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);
            return classifyCar(client,fileAbsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    // 车型识别sample
    public JSONObject classifyCar(AipImageClassify client,String filePath) {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("top_num", "3");
        options.put("baike_num", "5");


        // 参数为本地路径
        JSONObject res = client.carDetect(filePath, options);
        System.out.println(res.toString(2));
        return res;
    }

}
