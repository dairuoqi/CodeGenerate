package com.domain;


import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author rock
 * @date 2019/01/31
 */
@Data
@Builder
public class Table {
    private String tableName;
    private Column pk;
    private List<Column> columns;
    private String uppercaseClassName;
    private String lowercaseClassName;

}
