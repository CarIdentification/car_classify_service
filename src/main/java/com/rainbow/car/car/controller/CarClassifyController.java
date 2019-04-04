package com.rainbow.car.car.controller;

import com.rainbow.car.car.service.CarClassifyService;
import com.rainbow.car.car.vo.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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

    /**
     * 车型识别接口
     * @param carImg 车辆照片
     * @param type 识别类型
     */
    @PostMapping("/classify/car")
    @ResponseBody
    public BaseResult classifyCar(@RequestParam(name = "carImg", required = true) MultipartFile carImg,@RequestParam(name = "type", required = false, defaultValue = "0") Integer type,
                                  @RequestParam(name = "userId", required = false) Integer userId){
        BaseResult result = new BaseResult();
        String filePath = carClassifyService.saveToClassifyDirectory(carImg);
        if(filePath == null){
            return result.toError().setMsg("保存失败");
        }
        if(type == 0){
            result.setData(carClassifyService.outsideInterfaceClassifyCar(filePath,userId));
        }else if(type == 1){
            result.setData(carClassifyService.insideInterfaceClassifyCar(filePath));
        }

        return result;
    }
}
