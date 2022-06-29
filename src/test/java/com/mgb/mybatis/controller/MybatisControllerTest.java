package com.mgb.mybatis.controller;


import com.mgb.mybatis.entity.City;
import com.mgb.mybatis.service.CityDetailsService;
import com.mgb.mybatis.service.TestCityDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MybatisControllerTest {

    @Autowired
    TestCityDetailsService testCityDetailsService;

    @Test
    public ResponseEntity<List<City>> testGetCityListUsingSession(String countryCode){
       return testCityDetailsService.testGetCityListUsingSqlSession();
    }


}
