package com.mgb.mybatis.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mgb.mybatis.entity.City;

public interface CityDetailsService {

    /*public List<City> getAllCityDetails();
    public City getCityById(int cityId);
    public List<City> getCityList(String countryCode);*/
    public ResponseEntity<List<City>> getCityListUsingSqlSession(String countryCode);
    public String getUserUsingDeveloperInfo(String developer);
    
}
