package com.imran.demo.controllers;


import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.Dataset;
import com.imran.demo.exception.DatasetAlreadyExistsException;
import com.imran.demo.payloads.dataset.DatasetDto;
import com.imran.demo.response.ApiResponse;
import com.imran.demo.services.DatasetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/big-query")
@Api(value = "BigQuery Dataset", description = "Dataset Controller")
public class DatasetController {

    @Autowired
    DatasetService datasetService;


    @DeleteMapping("/project-id/{projectId}/dataset-id/{datasetId}")
    @ApiOperation(value = "Delete Dataset", notes = "Enter the private Project ID and Dataset ID. Public data cannot be used to delete dataset")
    public ResponseEntity<ApiResponse> deleteDatasets(@PathVariable("projectId")  String projectId, @PathVariable("datasetId") String datasetId) throws FileNotFoundException {
        try {
            boolean response = datasetService.deleteDatasets(projectId, datasetId);
            if (response) {
                return new ResponseEntity<ApiResponse>(new ApiResponse("Dataset deleted successfully", true), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<ApiResponse>(new ApiResponse("Unable to delete Dataset", false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<ApiResponse>(new ApiResponse("Unable to delete Dataset", false), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/project-id/{projectId}/dataset-id/{datasetId}")
    @ApiOperation(value = "Create Dataset", notes = "Enter the private Project ID and Dataset ID. Public data cannot be used to create dataset")
    public ResponseEntity<?> createDatasets(@PathVariable("projectId") String projectId, @PathVariable("datasetId") String datasetId) throws FileNotFoundException, DatasetAlreadyExistsException {
        try {
            String response = datasetService.createDatasets(projectId, datasetId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (BigQueryException e) {
            if (e.getMessage().contains("Already Exists")) {
                throw new DatasetAlreadyExistsException("Dataset already exists: " + datasetId);
            } else {
                return new ResponseEntity<>(new ApiResponse("Unable to create dataset", false), HttpStatus.EXPECTATION_FAILED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Unable to create dataset", false), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/project-id/{projectId}")
    @ApiOperation(value = "Get all dataset for a Project ID", notes = "Enter the Project ID. ")
    public ResponseEntity<?> getAllDatasets(@PathVariable("projectId") String projectId) throws FileNotFoundException {
        try {
            List<String> datasetIds = datasetService.getAllDataset(projectId);
            return ResponseEntity.ok(datasetIds);
        } catch (BigQueryException e){
            throw new FileNotFoundException("Wrong Project Id: "+projectId);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Unable to get dataset", false), HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PutMapping("/project-id/{projectId}/dataset-id/{datasetId}")
    @ApiOperation(value = "Update Dataset", notes = "Enter the private Project ID. Public data cannot be used to update dataset")
    public ResponseEntity<ApiResponse> updateDataset(@PathVariable("projectId") String projectId, @PathVariable("datasetId") String datasetId,
                                                     @ApiParam(name = "Dataset Body",value = "Dataset Object that needs to be updated.", required = true)
                                                     @RequestBody DatasetDto datasetDto){
        Dataset dataset1 = datasetService.updateDataset(projectId,datasetId,datasetDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse(dataset1.toString(),true ),HttpStatus.OK);
    }
}

