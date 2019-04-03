package com.rainbow.car.car.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CarDao {
  Integer queryCarIdByNameLike(@Param("name") String name);
}
