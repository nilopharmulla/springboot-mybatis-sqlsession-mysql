package com.mgb.mybatis.controller;

import com.mgb.mybatis.entity.City;
import com.mgb.mybatis.service.CityDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/getApi")
public class MybatisController {

    @Autowired
    CityDetailsService cityDetailsService;

    @GetMapping("/getResponseMsg")
    public ResponseEntity<String> getMsg(){
        return new ResponseEntity<>("This is mybatis demo project", HttpStatus.OK);
    }

   /* @GetMapping("/getCityDetails")
    public ResponseEntity<List<City>> getCityDetails(){
        List<City> cityData = new ArrayList<City>();
        cityData = cityDetailsService.getAllCityDetails();
        return new ResponseEntity<>(cityData,HttpStatus.OK);
    }

    @PostMapping("/getById/{cityId}")
    public ResponseEntity<City> getCityById(@PathVariable int cityId){
        City cityObj = cityDetailsService.getCityById(cityId);
        return new ResponseEntity<>(cityObj,HttpStatus.OK);
    }

    @PostMapping("/getCityList")
    public ResponseEntity<List<City>> getListOfCityByNameAndCountry(@RequestParam String CountryCode){
        return new ResponseEntity<>(cityDetailsService.getCityList(CountryCode),HttpStatus.OK);
    }*/

    @PostMapping("/getCityListByUsingSqlSession")
    public ResponseEntity<List<City>> getListOfCityByNameAndCountryBySession(@RequestParam String CountryCode){
        return cityDetailsService.getCityListUsingSqlSession(CountryCode);
    }
}
