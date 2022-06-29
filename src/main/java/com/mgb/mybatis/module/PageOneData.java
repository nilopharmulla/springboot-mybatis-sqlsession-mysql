package com.mgb.mybatis.module;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PageOneData implements Serializable {
    private String column1;
    private String column2;

}
