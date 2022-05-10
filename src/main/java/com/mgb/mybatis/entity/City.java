package com.mgb.mybatis.entity;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private int id;
    private String name;
    private String CountryCode;
    private String District;
    private String Population;
}
