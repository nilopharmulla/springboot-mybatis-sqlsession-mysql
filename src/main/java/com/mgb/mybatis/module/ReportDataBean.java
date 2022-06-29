package com.mgb.mybatis.module;

import java.util.List;
import java.util.Map;

import lombok.*;

@Data
@Getter
@Setter
public class ReportDataBean {
private String appId;
private String reportID;
private String orderID;
private String report_date;
private String custName;
private String gender;
private String risk;
private String cancerType;
private String cancerInfo;
private String variant_type;
private String variant_count;
private String variantData;
private String reportFilePath;
private String developerName;
private String cancerData;
private Map<String,String> referenceDtls;
private List<GeneTableData> geneTableData;
private DeveloperPersonalInfoData developerPersonalInfoData;
}
