package com.mgb.mybatis.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mgb.mybatis.entity.City;
import com.mgb.mybatis.module.DeveloperAppData;
import com.mgb.mybatis.module.GeneTableData;
import com.mgb.mybatis.module.PageFiveData;
import com.mgb.mybatis.module.PageFourData;
import com.mgb.mybatis.module.PageOneData;
import com.mgb.mybatis.module.PageThreeData;
import com.mgb.mybatis.module.ReportDataBean;
import com.mgb.mybatis.service.CityDetailsService;
import com.mgb.mybatis.service.SFTPClientFileClass;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@Controller
@RequestMapping("/getApi")
@PropertySource("classpath:application.properties")
@Slf4j
public class MybatisController {
	private Logger logger = LoggerFactory.getLogger(MybatisController.class);
	@Autowired
	CityDetailsService cityDetailsService;

	/*
	 * @Autowired FtpUploadFile ftpUploadFile;
	 */
	@Autowired
	SFTPClientFileClass sftpClientFile;

	@GetMapping("/getResponseMsg")
	public ResponseEntity<String> getMsg() {
		return new ResponseEntity<>("This is mybatis demo project", HttpStatus.OK);
	}

	/*
	 * @GetMapping("/getCityDetails") public ResponseEntity<List<City>>
	 * getCityDetails(){ List<City> cityData = new ArrayList<City>(); cityData =
	 * cityDetailsService.getAllCityDetails(); return new
	 * ResponseEntity<>(cityData,HttpStatus.OK); }
	 * 
	 * @PostMapping("/getById/{cityId}") public ResponseEntity<City>
	 * getCityById(@PathVariable int cityId){ City cityObj =
	 * cityDetailsService.getCityById(cityId); return new
	 * ResponseEntity<>(cityObj,HttpStatus.OK); }
	 * 
	 * @PostMapping("/getCityList") public ResponseEntity<List<City>>
	 * getListOfCityByNameAndCountry(@RequestParam String CountryCode){ return new
	 * ResponseEntity<>(cityDetailsService.getCityList(CountryCode),HttpStatus.OK);
	 * }
	 */

	@PostMapping("/getCityListByUsingSqlSession")
	public ResponseEntity<List<City>> getListOfCityByNameAndCountryBySession(@RequestParam String CountryCode) {
		logger.info("Country list using country code :" + CountryCode);
		return cityDetailsService.getCityListUsingSqlSession(CountryCode);
	}

	// ,produces = MediaType.APPLICATION_PDF_VALUE
	@GetMapping(value = "/getHomePage")
	public ModelAndView getHomePageData(Model modelMap) {
		ModelAndView mv = new ModelAndView();
		// mv.setViewName("home");
		mv.setViewName("index_New");
		return mv;
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = "/template/exportFileToPdf", produces =
	 * "application/pdf") public void exportFileToPdf(HttpServletResponse
	 * response, @RequestBody Map<String, Map<String, Object>> pageDataMap) throws
	 * Exception, JRException {
	 * 
	 * String cancerType = ""; String riskStr = ""; String appId = ""; String
	 * developerName = ""; String reportFilePath = ""; List<Map<String, Object>>
	 * finalReportData = new ArrayList<Map<String, Object>>(); List<Map<String,
	 * Object>> page3MapData = new ArrayList<Map<String, Object>>();
	 * List<Map<String, Object>> lstPageThreeMap = new ArrayList<Map<String,
	 * Object>>();
	 * 
	 * for (Map.Entry<String, Map<String, Object>> mapData : pageDataMap.entrySet())
	 * {
	 * 
	 * ReportDataBean reportDataBean = new ReportDataBean();
	 * 
	 * Map<String, Object> mapObj = null; if (mapData.getKey().contains("Page1")) {
	 * mapObj = mapData.getValue(); if (mapObj.containsKey("cancer_type")) {
	 * reportDataBean.setCancerType(mapObj.get("cancer_type").toString());
	 * //cancerType = mapObj.get("cancer_type").toString(); } if
	 * (mapObj.containsKey("risk")) {
	 * reportDataBean.setRiskStr(mapObj.get("risk").toString()); // riskStr =
	 * mapObj.get("risk").toString(); } if (mapObj.containsKey("appID")) {
	 * reportDataBean.setAppId(appId); //appId = mapObj.get("appID").toString(); }
	 * if (mapObj.containsKey("reportFilePath")) { reportFilePath =
	 * mapObj.get("reportFilePath").toString(); } //finalReportData.add(mapObj); }
	 * 
	 * List<GeneTableData> geneTableDataList = new ArrayList<GeneTableData>(); if
	 * (mapData.getKey().contains("Page3Data")) { mapObj = mapData.getValue();
	 * List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
	 * for (Map.Entry<String, Object> dataMap : mapObj.entrySet()) { if
	 * (dataMap.getKey().equals("Gene Table")) { tableData = (List<Map<String,
	 * Object>>) dataMap.getValue(); for (Map<String, Object> listMap : tableData) {
	 * GeneTableData geneTableData = new GeneTableData(); for (Map.Entry<String,
	 * Object> dataMap1 : listMap.entrySet()) { String dataKey = dataMap1.getKey();
	 * Object dataObj = dataMap1.getValue(); if (dataKey != null) { switch (dataKey)
	 * { case "Col1": geneTableData.setCol1(dataObj.toString()); break; case "Col2":
	 * geneTableData.setCol2(dataObj.toString()); break; case "Col3":
	 * geneTableData.setCol3(dataObj.toString()); break; case "Col4":
	 * geneTableData.setCol4(dataObj.toString()); break; case "Col5":
	 * geneTableData.setCol5(dataObj.toString()); break; case "Col6":
	 * geneTableData.setCol6(dataObj.toString()); break; case "Col7":
	 * geneTableData.setCol7(dataObj.toString()); break; case "Col8":
	 * geneTableData.setCol8(dataObj.toString()); break; case "Col9":
	 * geneTableData.setCol9(dataObj.toString()); break; case "Col10":
	 * geneTableData.setCol10(dataObj.toString()); break; default: break;
	 * 
	 * } } } geneTableDataList.add(geneTableData); // page3MapData.add(listMap); } }
	 * if (dataMap.getKey().equals("cancerInfo")) { String cancerInfo =
	 * dataMap.getValue().toString();
	 * 
	 * Map<String, Object> pageData = new HashMap<String, Object>();
	 * pageData.put("cancerInfo", cancerInfo); page3MapData.add(pageData);
	 * 
	 * reportDataBean.setCancerInfo(cancerInfo); }
	 * 
	 * if (dataMap.getKey().equals("References")) { int i = 1; Map<String, String>
	 * listTempMap = new HashMap<String, String>(); Map<String, String> listMap =
	 * (Map<String, String>) dataMap.getValue(); for (Map.Entry<String, String>
	 * objMap : listMap.entrySet()) { String dataKey = objMap.getKey().toString();
	 * String dataValue = objMap.getValue(); listTempMap.put("refKey" + i, dataKey);
	 * listTempMap.put("refValue" + i, dataValue); i++; } if (i < 7) { for (int j =
	 * i + 1; j <= 7; j++) { listTempMap.put("refKey" + j, "");
	 * listTempMap.put("refValue" + j, ""); } }
	 * reportDataBean.setReferenceDtls(listTempMap);
	 * //page3MapData.add(listTempMap); }
	 * 
	 * } }
	 * 
	 * 
	 * if (mapData.getKey().contains("Page4Table")) { mapObj = mapData.getValue();
	 * List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
	 * if (mapObj.containsKey("Table1")) { tableData = (List<Map<String, Object>>)
	 * mapObj.get("Table1"); Map<Object, Object> tableMap = tableData.stream()
	 * .collect(Collectors.toMap(dataKey -> dataKey.toString(), dataKey ->
	 * dataKey)); Map<String, Object> objMap = new HashMap<String, Object>(); for
	 * (Map.Entry<Object, Object> map : tableMap.entrySet()) { String dataKey =
	 * map.getKey().toString(); objMap.put(dataKey, map.getValue()); }
	 * finalReportData.add(objMap); } }
	 * 
	 * 
	 * if (mapData.getKey().contains("DeveloperData")) { mapObj =
	 * mapData.getValue(); List<Map<String, Object>> tableData = new
	 * ArrayList<Map<String, Object>>(); for (Map.Entry<String, Object> dataMap :
	 * mapObj.entrySet()) { if (dataMap.getKey().equals("Developer")) { tableData =
	 * (List<Map<String, Object>>) dataMap.getValue(); for (Map<String, Object>
	 * listMap : tableData) { Map<String, String> devAppData = (Map<String, String>)
	 * listMap.get("appData"); for (Map.Entry<String, String> appMap :
	 * devAppData.entrySet()) { if (appMap.getKey().equals(appId)) { developerName =
	 * listMap.get("developerName").toString(); break; } } if
	 * (!developerName.isEmpty()) { break; } } } } }
	 * 
	 * // =======================================================
	 * 
	 * if (mapData.getKey().contains("DeveloperData")) { mapObj =
	 * mapData.getValue(); if (mapObj.containsKey("Developer")) { for
	 * (Map.Entry<String, Object> devMap : mapObj.entrySet()) {
	 * List<DeveloperAppData> developerAppData = (List<DeveloperAppData>)
	 * devMap.getValue(); for (int i=0;i<developerAppData.size();i++) {
	 * Map<String,Object> appData = (Map<String,Object>)developerAppData.get(i);
	 * if(appData.containsKey("appData")) { //Map<String,String> devAppMap =
	 * (Map<String,String>)appData.get("appData"); Map<String,String> devAppData
	 * =(Map<String,String>)appData.get("appData"); if
	 * (devAppData.containsKey(appId)) { developerName = "xxxxxx"; }
	 * 
	 * // developerName = developerAppData.get(1).toString(); break; }
	 * 
	 * if (appData.containsKey("appData")) { Map<String,String> appData =
	 * (Map<String,String>)appData.get("appData"); }
	 * 
	 * }
	 * 
	 * for (DeveloperAppData devData : developerAppData) { Map<String,String>
	 * appData = devData.getAppData(); if(appData.containsKey(appId)) {
	 * developerName = devData.getDeveloperName(); break; } }
	 * 
	 * if (!developerName.isEmpty()) { break; }else { throw new
	 * Exception("Developer data Is Not Available"); } } } }
	 * 
	 * }
	 * 
	 * // Page5 field data String cancerData = getCancerInfo(cancerType); String
	 * cancerTypeStr = new String(cancerType); if
	 * (!cancerType.toLowerCase().contains("skin cancer")) { cancerType =
	 * cancerType.toLowerCase().replace("cancer", "").trim(); }
	 * 
	 * org.springframework.http.HttpHeaders httpHeaders = null; try {
	 * 
	 * List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>(); //
	 * -------------1 final JRBeanCollectionDataSource jrSource = new
	 * JRBeanCollectionDataSource(finalReportData); Path resourceDirectory =
	 * Paths.get("src/main/resources/images/BackgroundImages/0_report_cover.png");
	 * String backgroundImgPath = resourceDirectory.toFile().getAbsolutePath();
	 * 
	 * Path devLogoDir = Paths.get("src/main/resources/images/DeveloperInfo/Logo/" +
	 * developerName + ".png"); String devLogo =
	 * devLogoDir.toFile().getAbsolutePath(); String logoPath =
	 * getLogoPath(cancerType);
	 * 
	 * HashMap<String, Object> paramMap = new HashMap<String, Object>();
	 * paramMap.put("backImagePath", backgroundImgPath); paramMap.put("logoPath",
	 * logoPath); paramMap.put("devLogo", devLogo);
	 * 
	 * JasperReport compileReport = JasperCompileManager
	 * .compileReport(getClass().getResourceAsStream(
	 * "/jasperReports/Template_PageOne.jrxml")); JasperPrint jasperPrint1 =
	 * JasperFillManager.fillReport(compileReport, paramMap, jrSource);
	 * jasperPrintList.add(jasperPrint1);
	 * 
	 * // -------- Page2
	 * 
	 * final JRBeanCollectionDataSource jrSource2 = new
	 * JRBeanCollectionDataSource(finalReportData); Path resourceDirectory1 = Paths
	 * .get("src/main/resources/images/BackgroundImages/" + cancerType +
	 * "_page.png"); String backgroundImgPath1 =
	 * resourceDirectory1.toFile().getAbsolutePath();
	 * 
	 * Path devLogoDir1 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/"
	 * + developerName + ".png"); String devLogo1 =
	 * devLogoDir1.toFile().getAbsolutePath();
	 * 
	 * Map<String, Object> paramMap1 = new HashMap<String, Object>();
	 * paramMap1.put("backImagePath", backgroundImgPath1); paramMap1.put("devLogo",
	 * devLogo1);
	 * 
	 * JasperReport compileReport1 = JasperCompileManager
	 * .compileReport(getClass().getResourceAsStream(
	 * "/jasperReports/Template_PageTwo.jrxml")); JasperPrint jasperPrint2 =
	 * JasperFillManager.fillReport(compileReport1, paramMap1, jrSource2);
	 * jasperPrintList.add(jasperPrint2);
	 * 
	 * // ------- Page3 final JRBeanCollectionDataSource jrSource3 = new
	 * JRBeanCollectionDataSource(lstPageThreeMap);
	 * 
	 * Path resourceDirectory2 = Paths
	 * .get("src/main/resources/images/BackgroundImages/" + cancerType +
	 * "_page.png"); String backgroundImgPath2 =
	 * resourceDirectory2.toFile().getAbsolutePath();
	 * 
	 * Path devLogoDir2 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/"
	 * + developerName + ".png"); String devLogo2 =
	 * devLogoDir2.toFile().getAbsolutePath();
	 * 
	 * Map<String, Object> paramMap2 = new HashMap<String, Object>(); String
	 * genePanelStr =
	 * "Inherited mutations in the following curated genes are associated with an increased risk of "
	 * + cancerTypeStr; paramMap2.put("cancerType", cancerTypeStr);
	 * paramMap2.put("backImagePath", backgroundImgPath2); //
	 * paramMap2.put("devLogo", devLogo2); paramMap2.put("genePanel", genePanelStr);
	 * 
	 * JasperReport compileReport2 = JasperCompileManager
	 * .compileReport(getClass().getResourceAsStream(
	 * "/jasperReports/Template_PageThree.jrxml")); JasperPrint jasperPrint3 =
	 * JasperFillManager.fillReport(compileReport2, paramMap2, jrSource3);
	 * jasperPrintList.add(jasperPrint3);
	 * 
	 * // ------- Page4
	 * 
	 * 
	 * final JRBeanCollectionDataSource jrSource4 = new
	 * JRBeanCollectionDataSource(finalReportData);
	 * 
	 * 
	 * Path resourceDirectory3 = Paths
	 * .get("src/main/resources/images/BackgroundImages/" + cancerType +
	 * "_page.png"); String backgroundImgPath3 =
	 * resourceDirectory3.toFile().getAbsolutePath();
	 * 
	 * Path riskImagePath = Paths .get("src/main/resources/images/RiskImages/" +
	 * riskStr.toLowerCase().trim() + ".png"); String riskImageAbsPath =
	 * riskImagePath.toFile().getAbsolutePath();
	 * 
	 * Path devLogoDir3 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/"
	 * + developerName + ".png"); String devLogo3 =
	 * devLogoDir3.toFile().getAbsolutePath();
	 * 
	 * Map<String, Object> paramMap3 = new HashMap<String, Object>();
	 * paramMap3.put("backImagePath", backgroundImgPath1); //
	 * paramMap3.put("devLogo", devLogo3); paramMap3.put("riskImagePath",
	 * riskImageAbsPath);
	 * 
	 * JasperReport compileReport3 = JasperCompileManager
	 * .compileReport(getClass().getResourceAsStream(
	 * "/jasperReports/Template_PageFour.jrxml")); JasperPrint jasperPrint4 =
	 * JasperFillManager.fillReport(compileReport3, paramMap3, jrSource4);
	 * jasperPrintList.add(jasperPrint4);
	 * 
	 * 
	 * // ----- Page5
	 * 
	 * final JRBeanCollectionDataSource jrSource5 = new
	 * JRBeanCollectionDataSource(finalReportData);
	 * 
	 * Path resourceDirectory4 = Paths
	 * .get("src/main/resources/images/BackgroundImages/" + cancerType +
	 * "_page.png"); String backgroundImgPath4 =
	 * resourceDirectory4.toFile().getAbsolutePath();
	 * 
	 * Path devLogoDir4 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/"
	 * + developerName + ".png"); String devLogo4 =
	 * devLogoDir4.toFile().getAbsolutePath();
	 * 
	 * Map<String, Object> paramMap4 = new HashMap<String, Object>();
	 * paramMap4.put("backImagePath", backgroundImgPath4); paramMap4.put("devLogo",
	 * devLogo4); paramMap4.put("titlePara", "About " + cancerType.trim() +
	 * " Assessment Panel"); paramMap4.put("cancerData", cancerData);
	 * 
	 * JasperReport compileReport4 = JasperCompileManager
	 * .compileReport(getClass().getResourceAsStream(
	 * "/jasperReports/Template_PageFive.jrxml")); JasperPrint jasperPrint5 =
	 * JasperFillManager.fillReport(compileReport4, paramMap4, jrSource5);
	 * jasperPrintList.add(jasperPrint5);
	 * 
	 * 
	 * // ----- Page6
	 * 
	 * final JRBeanCollectionDataSource jrSource6 = new
	 * JRBeanCollectionDataSource(finalReportData);
	 * 
	 * Path resourceDirectory5 =
	 * Paths.get("src/main/resources/images/BackgroundImages/"+ cancerType
	 * +"_page.png"); String backgroundImgPath5 =
	 * resourceDirectory5.toFile().getAbsolutePath();
	 * 
	 * 
	 * Path devLogoDir5 =
	 * Paths.get("src/main/resources/images/DeveloperInfo/Logo/"+developerName+
	 * ".png"); String devLogo5 = devLogoDir5.toFile().getAbsolutePath();
	 * 
	 * Map<String, Object> paramMap5 = new HashMap<String, Object>();
	 * paramMap5.put("backImagePath", backgroundImgPath1); paramMap5.put("devLogo",
	 * devLogo5); paramMap5.put("linkedInLink", "www.google.com");
	 * 
	 * JasperReport compileReport5 = JasperCompileManager
	 * .compileReport(getClass().getResourceAsStream(
	 * "/jasperReports/Template_PageSix.jrxml")); JasperPrint jasperPrint6 =
	 * JasperFillManager.fillReport(compileReport5, paramMap5, jrSource5);
	 * jasperPrintList.add(jasperPrint6);
	 * 
	 * 
	 * // ========================= JRPdfExporter pdfExporter = new JRPdfExporter();
	 * pdfExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList)
	 * );
	 * 
	 * // Path reportFileDir = Paths.get("src/main/resources/Cancer_reports/" +
	 * appId + "/REPORT_FILES"); // String reportFilePath =
	 * reportFileDir.toFile().getAbsolutePath();
	 * 
	 * pdfExporter.setExporterOutput( new
	 * SimpleOutputStreamExporterOutput(reportFilePath +
	 * "\\" + cancerType.trim() + "_Cancer.pdf")); //
	 * "D:\\Nilophar\\PdfTemplates\\" + cancerType.trim() + "_Cancer.pdf"));
	 * 
	 * SimplePdfExporterConfiguration configuration = new
	 * SimplePdfExporterConfiguration();
	 * configuration.setCreatingBatchModeBookmarks(true);
	 * pdfExporter.setConfiguration(configuration); pdfExporter.exportReport(); //
	 * -------------------------
	 * 
	 * httpHeaders = new org.springframework.http.HttpHeaders();
	 * 
	 * httpHeaders.set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
	 * "inline;filename:" + cancerType.trim() + "_Cancer.pdf");
	 * 
	 * httpHeaders.setContentType(MediaType.APPLICATION_PDF); } catch (Exception e)
	 * { throw new Exception("Error On Generating Report " + e.getMessage()); //
	 * TODO: handle exception }
	 * 
	 * }
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/template/exportFileToPdf", produces = "application/pdf")
	public void exportFileToPdf(HttpServletResponse response, @RequestBody List<ReportDataBean> listReportDataBean)
			throws Exception, JRException {

		String developerName = "";
		String cancerType = "";
		String cancerData = "";
		String reportFilePath = "";
		String riskStr="";
		for (ReportDataBean reportDataBean : listReportDataBean) {
			developerName = reportDataBean.getDeveloperName();
			cancerType = reportDataBean.getCancerType();
			cancerData = reportDataBean.getCancerData();
			reportFilePath = reportDataBean.getReportFilePath();
			riskStr = reportDataBean.getRisk();
		}
		org.springframework.http.HttpHeaders httpHeaders = null;
		try {

			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			// -------------1
			final JRBeanCollectionDataSource jrSource = new JRBeanCollectionDataSource(listReportDataBean);
			Path resourceDirectory = Paths.get("src/main/resources/images/BackgroundImages/0_report_cover.png");
			String backgroundImgPath = resourceDirectory.toFile().getAbsolutePath();

			Path devLogoDir = Paths.get("src/main/resources/images/DeveloperInfo/Logo/" + developerName + ".png");
			String devLogo = devLogoDir.toFile().getAbsolutePath();
			String logoPath = getLogoPath(cancerType);

			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("backImagePath", backgroundImgPath);
			paramMap.put("logoPath", logoPath);
			paramMap.put("devLogo", devLogo);

			JasperReport compileReport = JasperCompileManager
					.compileReport(getClass().getResourceAsStream("/jasperReports/Template_PageOne.jrxml"));
			JasperPrint jasperPrint1 = JasperFillManager.fillReport(compileReport, paramMap, jrSource);
			jasperPrintList.add(jasperPrint1);

			// -------- Page2

			final JRBeanCollectionDataSource jrSource2 = new JRBeanCollectionDataSource(listReportDataBean);
			Path resourceDirectory1 = Paths
					.get("src/main/resources/images/BackgroundImages/" + cancerType + "_page.png");
			String backgroundImgPath1 = resourceDirectory1.toFile().getAbsolutePath();

			Path devLogoDir1 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/" + developerName + ".png");
			String devLogo1 = devLogoDir1.toFile().getAbsolutePath();

			Map<String, Object> paramMap1 = new HashMap<String, Object>();
			paramMap1.put("backImagePath", backgroundImgPath1);
			paramMap1.put("devLogo", devLogo1);

			JasperReport compileReport1 = JasperCompileManager
					.compileReport(getClass().getResourceAsStream("/jasperReports/Template_PageTwo.jrxml"));
			JasperPrint jasperPrint2 = JasperFillManager.fillReport(compileReport1, paramMap1, jrSource2);
			jasperPrintList.add(jasperPrint2);

			// ------- Page3
			final JRBeanCollectionDataSource jrSource3 = new JRBeanCollectionDataSource(listReportDataBean);

			Path resourceDirectory2 = Paths
					.get("src/main/resources/images/BackgroundImages/" + cancerType + "_page.png");
			String backgroundImgPath2 = resourceDirectory2.toFile().getAbsolutePath();

			Path devLogoDir2 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/" + developerName + ".png");
			String devLogo2 = devLogoDir2.toFile().getAbsolutePath();

			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			String genePanelStr = "Inherited mutations in the following curated genes are associated with an increased risk of "
					+ cancerType;
			paramMap2.put("cancerType", cancerType);
			paramMap2.put("backImagePath", backgroundImgPath2); //
			paramMap2.put("devLogo", devLogo2);
			paramMap2.put("genePanel", genePanelStr);

			JasperReport compileReport2 = JasperCompileManager
					.compileReport(getClass().getResourceAsStream("/jasperReports/Template_PageThree.jrxml"));
			JasperPrint jasperPrint3 = JasperFillManager.fillReport(compileReport2, paramMap2, jrSource3);
			jasperPrintList.add(jasperPrint3);

			// ------- Page4

			final JRBeanCollectionDataSource jrSource4 = new JRBeanCollectionDataSource(listReportDataBean);

			Path resourceDirectory3 = Paths
					.get("src/main/resources/images/BackgroundImages/" + cancerType + "_page.png");
			String backgroundImgPath3 = resourceDirectory3.toFile().getAbsolutePath();

			Path riskImagePath = Paths
					.get("src/main/resources/images/RiskImages/" + riskStr.toLowerCase().trim() + ".png");
			String riskImageAbsPath = riskImagePath.toFile().getAbsolutePath();

			Path devLogoDir3 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/" + developerName + ".png");
			String devLogo3 = devLogoDir3.toFile().getAbsolutePath();

			Map<String, Object> paramMap3 = new HashMap<String, Object>();
			paramMap3.put("backImagePath", backgroundImgPath1); //
			paramMap3.put("devLogo", devLogo3);
			paramMap3.put("riskImagePath", riskImageAbsPath);

			JasperReport compileReport3 = JasperCompileManager
					.compileReport(getClass().getResourceAsStream("/jasperReports/Template_PageFour.jrxml"));
			JasperPrint jasperPrint4 = JasperFillManager.fillReport(compileReport3, paramMap3, jrSource4);
			jasperPrintList.add(jasperPrint4);

			// ----- Page5
			final JRBeanCollectionDataSource jrSource5 = new JRBeanCollectionDataSource(listReportDataBean);

			Path resourceDirectory4 = Paths
					.get("src/main/resources/images/BackgroundImages/" + cancerType + "_page.png");
			String backgroundImgPath4 = resourceDirectory4.toFile().getAbsolutePath();

			Path devLogoDir4 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/" + developerName + ".png");
			String devLogo4 = devLogoDir4.toFile().getAbsolutePath();

			Map<String, Object> paramMap4 = new HashMap<String, Object>();
			paramMap4.put("backImagePath", backgroundImgPath4);
			paramMap4.put("devLogo", devLogo4);
			paramMap4.put("titlePara", "About " + cancerType.trim() + " Assessment Panel");
			paramMap4.put("cancerData", cancerData);

			JasperReport compileReport4 = JasperCompileManager
					.compileReport(getClass().getResourceAsStream("/jasperReports/Template_PageFive.jrxml"));
			JasperPrint jasperPrint5 = JasperFillManager.fillReport(compileReport4, paramMap4, jrSource5);
			jasperPrintList.add(jasperPrint5);

			// ----- Page6

			final JRBeanCollectionDataSource jrSource6 = new JRBeanCollectionDataSource(listReportDataBean);

			Path resourceDirectory5 = Paths
					.get("src/main/resources/images/BackgroundImages/" + cancerType + "_page.png");
			String backgroundImgPath5 = resourceDirectory5.toFile().getAbsolutePath();

			Path devLogoDir5 = Paths.get("src/main/resources/images/DeveloperInfo/Logo/" + developerName + ".png");
			String devLogo5 = devLogoDir5.toFile().getAbsolutePath();

			Map<String, Object> paramMap5 = new HashMap<String, Object>();
			paramMap5.put("backImagePath", backgroundImgPath5);
			paramMap5.put("devLogo", devLogo5);
			paramMap5.put("linkedInLink", "www.google.com");

			JasperReport compileReport5 = JasperCompileManager
					.compileReport(getClass().getResourceAsStream("/jasperReports/Template_PageSix.jrxml"));
			JasperPrint jasperPrint6 = JasperFillManager.fillReport(compileReport5, paramMap5, jrSource5);
			jasperPrintList.add(jasperPrint6);
			 
			// =========================
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));

			pdfExporter.setExporterOutput(
					new SimpleOutputStreamExporterOutput(reportFilePath + "\\" + cancerType.trim() + "_Cancer.pdf"));

			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.setCreatingBatchModeBookmarks(true);
			pdfExporter.setConfiguration(configuration);
			pdfExporter.exportReport();
			// -------------------------

			httpHeaders = new org.springframework.http.HttpHeaders();

			httpHeaders.set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
					"inline;filename:" + cancerType.trim() + "_Cancer.pdf");

			httpHeaders.setContentType(MediaType.APPLICATION_PDF);
		} catch (Exception e) {
			throw new Exception("Error On Generating Report " + e.getMessage());
			// TODO: handle exception
		}

	}

	private String getLogoPath(String cancerType) {
		if (cancerType.toLowerCase().contains("cancer")) {
			cancerType = cancerType.toLowerCase().replace("cancer", "").trim();
		}
		Path resourceLogoDirectory = Paths.get("src/main/resources/images/CancerIcons/" + cancerType + ".png");
		String logoPath = resourceLogoDirectory.toFile().getAbsolutePath();
		return logoPath;
	}

	private String getCancerInfo(String cancerType) {
		int genMutation = 130;
		int snps = 127;
		int indels = 3;

		StringBuilder cancerData = new StringBuilder(200);
		cancerData.append("The ").append(cancerType).append(" test panel is designed to detect the ")
				.append(genMutation).append(" genetic mutations ( out of which ").append(snps).append(" SNPs and ")
				.append(indels).append(" INDELs which have been extensively ")
				.append("studied and reviewed across various research articles.");

		return cancerData.toString();
	}
}
