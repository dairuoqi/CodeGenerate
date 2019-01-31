package com.domain;

import lombok.Data;


/**
 * @author rock
 * @date 2019/01/31
 */
@Data
public class Column {

    private String columnName;
    private String columnDataType;
    private String columnKey;
    private String columnComment;
    private String uppercaseAttrName;
    private String lowercaseAttrName;
    private String javaDataType;

}
