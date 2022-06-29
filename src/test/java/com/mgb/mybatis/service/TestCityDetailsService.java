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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TestCityDetailsService {

    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private Logger logger = LoggerFactory.getLogger(TestCityDetailsService.class);

    public ResponseEntity<List<City>> testGetCityListUsingSqlSession(){
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("countryCode", "NLD");
        List<City> resultMap = null;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            resultMap = sqlSession.selectList("CityMapper.findByNameAndCountryUsingSession", paramMap);
        } catch (Exception ex) {
            logger.info("Error on opening sql session" + ex.getMessage());
        }
        return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
