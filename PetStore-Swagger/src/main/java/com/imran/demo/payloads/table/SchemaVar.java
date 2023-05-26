package com.imran.demo.payloads.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchemaVar implements Serializable {
    private String columnName;
    private String columnType;
}
