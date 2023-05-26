package com.imran.demo.services;

import com.google.cloud.bigquery.Dataset;
import com.imran.demo.payloads.dataset.DatasetDto;

import java.io.FileNotFoundException;
import java.util.List;

public interface DatasetService {
    boolean deleteDatasets(String projectId,String datasetID) throws FileNotFoundException;

    String createDatasets(String projectId, String datasetID) throws FileNotFoundException;

    List<String>  getAllDataset(String projectId) throws FileNotFoundException;

    Dataset updateDataset(String projectId, String datasetID, DatasetDto datasetDto);
}
