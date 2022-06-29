package com.mgb.mybatis.module;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GeneticVariantTableData {
 private String rsID;
 private String Gene;
 private String Genotype;
 private String GeneticAlteration;
 private String MutationType;
 private String Effect;
 
}
