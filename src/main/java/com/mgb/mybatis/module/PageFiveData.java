package com.mgb.mybatis.module;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PageFiveData implements Serializable {
    private String cancer_type;
    private int markers;
    private int snps;
    private int indels;
}
