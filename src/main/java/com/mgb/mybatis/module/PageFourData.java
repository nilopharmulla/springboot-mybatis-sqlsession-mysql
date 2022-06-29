package com.mgb.mybatis.module;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
public class PageFourData implements Serializable{
    private String rsId;
    private String gene;
    private String genotype;
    private String genetic_alteration;
    private String mutation_type;
    private String effect;
}
