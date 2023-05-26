package com.imran.demo.services.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.imran.demo.payloads.table.ColumnDefinition;
import com.imran.demo.payloads.table.SchemaVar;
import com.imran.demo.payloads.table.TableSchema;
import com.imran.demo.services.TableService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.google.cloud.bigquery.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {
    public BigQuery authentication() throws FileNotFoundException {
        File credentialsPath = new File("C:/Users/ikhan/Downloads/bionic-union-368009-4e4bb149ad82.json");
        GoogleCredentials credentials;
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
            return BigQueryOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @CacheEvict(value = "createTableCache", key = "{#projectId, #datasetId, #tableId}")
    @Override
    public Table createTable(String projectId, String datasetId, String tableId, List<ColumnDefinition> columnDefinitions) throws FileNotFoundException {

        try {
            List<Field> fields = columnDefinitions.stream()
                    .map(columnDefinition -> Field.newBuilder(
                                    columnDefinition.getName(),
                                    StandardSQLTypeName.valueOf(columnDefinition.getType()))
                            .build())
                    .collect(Collectors.toList());
            TableDefinition tableDefinition = com.google.cloud.bigquery.StandardTableDefinition.newBuilder()
                    .setSchema(Schema.of(fields))
                    .build();
            TableId tableId1 = TableId.of(projectId, datasetId, tableId);
            TableInfo tableInfo = TableInfo.newBuilder(tableId1, tableDefinition).build();
            return authentication().create(tableInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating table: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "createTableCache", key = "{#projectId, #datasetId, #tableId}")
    @Override
    public boolean deleteTable(String projectId, String datasetId, String tableId) throws FileNotFoundException {
        TableId tableId1 = TableId.of(projectId, datasetId, tableId);
        boolean result = authentication().delete(tableId1);
        return result;
    }

    @Cacheable(value = "listOfTableCache", key = "{#projectId, #datasetId}")
    @Override
    public List<String> listOfTable(String projectId, String datasetId) throws FileNotFoundException {
        DatasetId dataset = DatasetId.of(projectId, datasetId);
        List<String> tableList = new ArrayList<>();
        for (Table table : authentication().listTables(dataset).iterateAll()) {
            TableId tableId = table.getTableId();
            String tableName = tableId.getTable();
            tableList.add(tableName);
        }
        return tableList;
    }

    @Cacheable(value = "getTableInfoCache", key = "{#projectId, #datasetId, #tableId}")
    @Override
    public String getTableInfo(String projectId, String datasetId, String tableId) throws FileNotFoundException {
        TableId table = TableId.of(projectId, datasetId, tableId);
        Table tableInfo = authentication().getTable(table);
        return tableInfo.toString();
    }

    @Cacheable(value = "getTableDetailsCache", key = "{#projectId, #datasetId, #tableId}")
    @Override
    public TableSchema getTableSchema(String projectId, String datasetId, String tableId) throws FileNotFoundException {
        TableId table = TableId.of(projectId, datasetId, tableId);
        Table tableInfo = authentication().getTable(table);
        Schema schema = tableInfo.getDefinition().getSchema();
        TableSchema tableSchema = new TableSchema();
        List<SchemaVar> schemaVars = new ArrayList<>();
        for (Field field : schema.getFields()) {
            SchemaVar schemaVars1 = new SchemaVar();
            schemaVars1.setColumnName(field.getName());
            schemaVars1.setColumnType(field.getType().name());
            schemaVars.add(schemaVars1);
        }
        tableSchema.setSchemaVar(schemaVars);
        return tableSchema;
    }

    @Override
    @Cacheable("queryResultCache")
    public List<Map<String, Object>> getQueryResult(String projectId, String datasetId, String tableId,Integer noOfResult, String columnNames) throws FileNotFoundException {
        String query = "SELECT "+columnNames+" FROM `" + projectId + "." + datasetId + "." + tableId + "`" + " LIMIT "+noOfResult.toString();
        try {
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
            TableResult result = authentication().query(queryConfig);
            List<Map<String, Object>> allResults = new ArrayList<>();

            Schema schema = result.getSchema();

            for (FieldValueList row : result.iterateAll()) {
                Map<String, Object> rowValues = new HashMap<>();
                for (int i = 0; i < schema.getFields().size(); i++) {
                    Field field = schema.getFields().get(i);
                    String columnName = field.getName();
                    Object columnValue = row.get(i).getValue();
                    rowValues.put(columnName, columnValue);
                }

                allResults.add(rowValues);
            }
            return allResults;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating table: " + e.getMessage(), e);
        }
    }
}
