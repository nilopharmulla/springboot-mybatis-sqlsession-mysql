package com.mgb.mybatis.service;

import ch.qos.logback.core.CoreConstants;
import com.mgb.mybatis.entity.City;
import com.mgb.mybatis.mapper.CityMapper;
import com.sun.media.jfxmedia.logging.Logger;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;


@Service
public class CityDetailsServiceImpl implements CityDetailsService {

    /*  @Autowired
      CityMapper cityMapper;
  */
    @Autowired
    SqlSessionFactory sqlSessionFactory;

   /* public List<City> getAllCityDetails(){
        return cityMapper.getAllCities();
    }

    public City getCityById(int cityId){
        return cityMapper.findById(cityId);
    }

    public List<City> getCityList(String countryCode){
        Map<String,String> paramMap = new HashMap<String,String>();
      *//*  paramMap.put("name",cityName);*//*
        paramMap.put("countryCode",countryCode);
        return cityMapper.findByNameAndCountry(paramMap);
    }*/

    public ResponseEntity<List<City>> getCityListUsingSqlSession(String countryCode) {
        Map<String, String> paramMap = new HashMap<String, String>();
        /*  paramMap.put("name",cityName);*/
        paramMap.put("countryCode", countryCode);
        List<City> resultMap = null;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            resultMap = sqlSession.selectList("CityMapper.findByNameAndCountryUsingSession", paramMap);
            System.out.println("resultMap :" + resultMap);
        } catch (Exception ex) {
            System.out.println("Error on opening sql session" + ex.getMessage());
        }
        return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
