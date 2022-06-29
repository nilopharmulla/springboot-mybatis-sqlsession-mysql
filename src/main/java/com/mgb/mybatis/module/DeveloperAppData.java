package com.mgb.mybatis.module;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
public class DeveloperAppData implements Serializable {
    private String developerName;
    private Map<String,String> appData;
}
