package com.rainbow.car.car.controller;

import com.alibaba.fastjson.JSONObject;
import com.rainbow.car.car.service.CarClassifyService;
import com.rainbow.car.car.vo.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author caijiatao
 * @date 2019/3/12 4:24 PM
 */
@RestController
@RequestMapping("/rainbow")
public class CarClassifyController {
    @Autowired
    private CarClassifyService carClassifyService;

    @PostMapping("/classify/car")
    @ResponseBody
    public BaseResult classifyCar(@RequestParam(name = "carImg", required = false) MultipartFile carImg){
        BaseResult result = new BaseResult();
        org.json.JSONObject classifyResult = carClassifyService.carClassify(carImg);
        result.setData(JSONObject.parse(classifyResult.get("result").toString()));
        return result;
    }
}
