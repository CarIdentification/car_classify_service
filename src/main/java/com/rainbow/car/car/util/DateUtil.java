package com.rainbow.car.car.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

  private static ThreadLocal<SimpleDateFormat> fileFormatDateThreadLocal;

  static {
    fileFormatDateThreadLocal = ThreadLocal
        .withInitial(() -> new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss"));
  }

  /**
   * 用于文件名的日期格式化 复用DateFormat 线程安全
   *
   * @return 格式化后的日期
   */
  public static String getFileFormatDate(Date date) {
    SimpleDateFormat simpleDateFormat = fileFormatDateThreadLocal.get();
    return simpleDateFormat.format(date);
  }

}
