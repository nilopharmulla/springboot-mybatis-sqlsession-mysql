package com.mgb.mybatis.service;

import com.mgb.mybatis.entity.City;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CityDetailsService {

    /*public List<City> getAllCityDetails();
    public City getCityById(int cityId);
    public List<City> getCityList(String countryCode);*/
    public ResponseEntity<List<City>> getCityListUsingSqlSession(String countryCode);
}
