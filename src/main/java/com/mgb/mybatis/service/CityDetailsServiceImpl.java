package com.mgb.mybatis.service;

import com.mgb.mybatis.entity.City;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CityDetailsServiceImpl implements CityDetailsService {

    /*  @Autowired
      CityMapper cityMapper;
  */
    private Logger logger = LoggerFactory.getLogger(CityDetailsServiceImpl.class);

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
        } catch (Exception ex) {
            logger.info("Error on opening sql session" + ex.getMessage());
            System.out.println("Error on opening sql session" + ex.getMessage());
        }
        return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
