package com.imran.demo.services;

import com.google.cloud.bigquery.Table;
import com.imran.demo.payloads.table.ColumnDefinition;
import com.imran.demo.payloads.table.TableSchema;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface TableService {
    Table createTable(String projectId, String datasetId, String tableId,List<ColumnDefinition> columnDefinitions) throws FileNotFoundException;
    boolean deleteTable(String projectId, String datasetId, String tableId) throws FileNotFoundException;
    List<String> listOfTable(String projectId, String datasetId) throws FileNotFoundException;

    String getTableInfo(String projectId, String datasetId, String tableId) throws FileNotFoundException;

    TableSchema getTableSchema(String projectId, String datasetId, String tableId) throws FileNotFoundException;

    List<Map<String,Object>> getQueryResult(String projectId, String datasetId, String tableId,Integer noOResult, String columnName) throws FileNotFoundException;
}
