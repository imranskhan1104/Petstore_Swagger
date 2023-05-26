package com.imran.demo.controllers;

import com.google.cloud.bigquery.Table;
import com.imran.demo.payloads.table.ColumnDefinition;
import com.imran.demo.payloads.table.TableSchema;
import com.imran.demo.response.ApiResponse;
import com.imran.demo.services.TableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
@Api(value = "BigQuery Table", description = "Table Controller")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping("/project-id/{projectId}/dataset-id/{datasetId}/table-id/{tableId}")
    @ApiOperation(value = "Create Table", notes = "Enter the private Project ID, Dataset Id and Table Id. Public data cannot be used to create dataset")
    public ResponseEntity<?> createTable(@PathVariable("projectId") String projectId,
                                             @PathVariable("datasetId") String datasetId,
                                             @PathVariable("tableId") String tableId,
                                             @ApiParam(name = "Column Definition Body",value = "Add the column name and the data type of colum", required = true)
                                             @RequestBody List<ColumnDefinition> columnDefinitions) throws FileNotFoundException {
        try {
            Table a = tableService.createTable(projectId, datasetId, tableId, columnDefinitions);
            return new ResponseEntity<ApiResponse>(new ApiResponse(a.toString(),true),HttpStatus.OK);
        } catch(Exception e)  {
            return new ResponseEntity<ApiResponse>(new ApiResponse(e.toString(),false),HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/project-id/{projectId}/dataset-id/{datasetId}/table-id/{tableId}")
    @ApiOperation(value = "Delete Table", notes = "Enter the private Project ID, Dataset Id and Table Id. Public data cannot be used to create dataset")
    public ResponseEntity<ApiResponse> deleteTable(@PathVariable("projectId") String projectId,
                                                   @PathVariable("datasetId") String datasetId,
                                                   @PathVariable("tableId") String tableId) throws FileNotFoundException {
        boolean result = tableService.deleteTable(projectId,datasetId,tableId);
        if (result){
            return new ResponseEntity<ApiResponse>(new ApiResponse("Table delete successfully",result), HttpStatus.OK);
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("Unable to delete table",result), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/project-id/{projectId}/dataset-id/{datasetId}")
    @ApiOperation(value = "Get List of Tables", notes = "Enter the Project ID, Dataset Id")
    public ResponseEntity<?> listOfTables(@PathVariable("projectId") String projectId,
                                                   @PathVariable("datasetId") String datasetId) throws FileNotFoundException {
        try {
            List<String> result = tableService.listOfTable(projectId, datasetId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<ApiResponse>(new ApiResponse("No Table with provided tableId",false), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/project-id/{projectId}/dataset-id/{datasetId}/table-id/{tableId}")
    @ApiOperation(value = "Get Table Info", notes = "Enter the Project ID, Dataset Id and Table Id.")
    public ResponseEntity<ApiResponse> getTableInfo(@PathVariable("projectId") String projectId,
                                                    @PathVariable("datasetId") String datasetId,
                                                    @PathVariable("tableId") String tableId) throws FileNotFoundException {
        try {
            String response= tableService.getTableInfo(projectId, datasetId, tableId);
            return new ResponseEntity<ApiResponse>(new ApiResponse(response,true),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ApiResponse>(new ApiResponse("No Table with provided tableId",false), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/project-id/{projectId}/dataset-id/{datasetId}/table-id/{tableId}/get-table-details")
    @ApiOperation(value = "Get Table Schema", notes = "Enter the Project ID, Dataset Id and Table Id. Public data cannot be used to create dataset")
    public ResponseEntity<?> getTableDetails(@PathVariable("projectId") String projectId,
                                                       @PathVariable("datasetId") String datasetId,
                                                       @PathVariable("tableId") String tableId) throws FileNotFoundException {
        try {
            TableSchema response= tableService.getTableSchema(projectId, datasetId, tableId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<ApiResponse>(new ApiResponse("No Table with provided tableId",false), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/project-id/{projectId}/dataset-id/{datasetId}/table-id/{tableId}/get-query-result")
    @ApiOperation(value = "Get Table Values by Query", notes = "Enter the Project ID, Dataset Id and Table Id.")
    public ResponseEntity<?> getQueryResult (@PathVariable("projectId") String projectId,
                                             @PathVariable("datasetId") String datasetId,
                                             @PathVariable("tableId") String tableId,
                                             @RequestParam Integer noOfResult,
                                             @RequestParam String columnName) throws FileNotFoundException {
        try {
            List<Map<String,Object>> result = tableService.getQueryResult(projectId,datasetId, tableId,noOfResult,columnName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<ApiResponse>(new ApiResponse("Something went wrong. Please check the query",false), HttpStatus.BAD_REQUEST);
        }
    }
}