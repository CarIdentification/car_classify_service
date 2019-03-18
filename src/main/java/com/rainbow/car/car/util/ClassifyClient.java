package com.rainbow.car.car.util;

import com.baidu.aip.imageclassify.AipImageClassify;

/**
 * @author caijiatao
 * @date 2019/3/14 3:22 PM
 */
public class ClassifyClient {
    //设置APPID/AK/SK
    private static final String APP_ID = "15736377";
    private static final String API_KEY = "kdEkef4Mo1RUMfxLHpChNPbI";
    private static final String SECRET_KEY = "V9gM2L4m7KMT84sDQjRxahLqqG2X1dH6";

    private static AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);

    public static AipImageClassify getClient() {
        return client;
    }

}
