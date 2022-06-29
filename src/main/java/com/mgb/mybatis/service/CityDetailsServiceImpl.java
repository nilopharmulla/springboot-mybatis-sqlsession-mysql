package com.mgb.mybatis.service;

import com.mgb.mybatis.entity.City;
import com.mgb.mybatis.module.DeveloperAppData;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
            sqlSession.close();
        } catch (Exception ex) {
            logger.info("Error on opening sql session" + ex.getMessage());
        }
        return new ResponseEntity<>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public String getUserUsingDeveloperInfo(String developer) {
    	String userId="";
    	List<DeveloperAppData> devAppData=null;
    	try {
    		devAppData = readDeveloperData();	
    		Map<String,String> mapData = devAppData.stream()
    				.filter(x->x.getDeveloperName().equalsIgnoreCase(developer))
    				.map(y->y.getAppData()).findFirst().get();
    				//.collect(Collectors.toMap(dataKey->dataKey.toString(),value->value.toString()));
    		if (mapData != null && mapData.size()>0) {
    			userId = mapData.get("ftpId");
    		}
		} catch (Exception e) {

		}
    	
    	return userId;
    }
    
    private List<DeveloperAppData> readDeveloperData() throws FileNotFoundException,IOException {

		Path resourceDirectory = Paths.get("src/main/resources/images/DeveloperInfo/DeveloperApps.txt");
		String txtFilePath = resourceDirectory.toFile().getAbsolutePath();
		List<DeveloperAppData> devDataList = new ArrayList<>();
		DeveloperAppData devData = null;
		File file = new File(txtFilePath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String contentStr = "";
		boolean flgAppData = false;
		Map<String,String> mapData = null;
		
		try {
			while((contentStr = br.readLine())!=null){
				String[] contentArray = contentStr.split("#");
				if (contentStr.trim().indexOf("#") >= 0 && (mapData == null ||  mapData.size()==0)){
					flgAppData = false;
					devData = new DeveloperAppData();
					devData.setDeveloperName(contentArray[1]);
					mapData = new HashMap<>();
				}

				if (contentStr.trim().indexOf("[") >= 0){
					flgAppData = true;
				}
				if (contentStr.trim().indexOf("]") >= 0 && mapData.size()>0) {
					devData.setAppData(mapData);
					devDataList.add(devData);
					mapData = new HashMap<>();
					flgAppData=false;
				}
				if (flgAppData && (contentStr.trim().indexOf("=") >= 0)){
					String[] contentData = contentStr.split("=");
					if (contentData.length > 0){
						for(int i=0;i<contentData.length;i++){
							mapData.put(contentData[0].toString(),contentData[1].toString());
						}
					}
				}
			}
		}catch (Exception ex){
			file = null;
			br = null;
		}finally {
			file = null;
			br = null;
		}
		return devDataList;
	}
    
}
