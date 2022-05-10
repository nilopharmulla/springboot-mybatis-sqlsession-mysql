package com.mgb.mybatis.mapper;

import com.mgb.mybatis.entity.City;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CityMapper {

   // @Select("select * from city")
    public List<City> getAllCities();

    //@Select("select * from city where id=#{id}")
    public City findById(int id);

    public List<City> findByNameAndCountry(Map<String,String> paramMap);

    public City findByNameAndCountryUsingSession(Map<String,String> paramMap);
}
