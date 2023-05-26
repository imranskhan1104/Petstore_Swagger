package com.imran.demo.services.impl;


import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;
import com.google.gson.Gson;
import com.imran.demo.payloads.dataset.DatasetDto;
import com.imran.demo.services.DatasetService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DatasetServiceImpl implements DatasetService {
    File credentialsPath = new File("C:/Users/ikhan/Downloads/bionic-union-368009-4e4bb149ad82.json");

    Gson gson = new Gson();

    public BigQuery authentication() throws FileNotFoundException {
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

    @CacheEvict(value = "deleteDatasetsCache", key = "{#projectId, #datasetID}")
    @Override
    public boolean deleteDatasets(String projectId,String datasetID) throws FileNotFoundException {
        DatasetId ownInstanceFullId = DatasetId.of(projectId, datasetID);
        Dataset ownDataset = authentication().getDataset(ownInstanceFullId);
        return ownDataset.delete();
    }

    @CacheEvict(value = "createDatasetsCache", key = "{#projectId, #datasetID}")
    @Override
    public String createDatasets(String projectId, String datasetID) throws FileNotFoundException {
        Dataset createDataset = authentication().create(DatasetInfo.newBuilder(projectId, datasetID).build());
        String datasetString = createDataset.toString();
        Map<String, Object> jsonObject = parseJsonObject(datasetString);
        return gson.toJson(jsonObject);
    }

    @Cacheable(value = "getAllDatasetCache", key = "#projectId")
    @Override
    public List<String> getAllDataset(String projectId) throws FileNotFoundException {
        List<String> allDatasetList1 =new ArrayList<>();

        Page<Dataset> datasetList = authentication().listDatasets(projectId);

        for (Dataset dataset1 : datasetList.iterateAll()) {
            String datasetId = dataset1.getDatasetId().getDataset();
            allDatasetList1.add(datasetId.toString());
        }
        return allDatasetList1;
    }

    @CacheEvict(value = "updateDatasetCache", key = "{#projectId, #datasetID}")
    public  Dataset updateDataset(String projectId, String datasetID, DatasetDto datasetDto){
        DatasetId dataset1 = DatasetId.of(projectId, datasetID);
        try {
            DatasetInfo datasetInfo = authentication().getDataset(dataset1);

            datasetInfo = datasetInfo.toBuilder()
                    .setFriendlyName(datasetDto.getFriendlyName())
                    .setDescription(datasetDto.getDescription())
                    .setLabels(datasetDto.getLabels())
                    .setDefaultTableLifetime(datasetDto.getDefaultTableLifetime())
                    .setDefaultPartitionExpirationMs(datasetDto.getDefaultPartitionExpirationMs())
                    .setDatasetId(DatasetId.of(projectId, datasetID))
                    .build();

            return authentication().update(datasetInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error updating dataset: " + e.getMessage());
        }
    }

    private Map<String, Object> parseJsonObject(String jsonString) {
        jsonString = jsonString.substring(1, jsonString.length() - 1); // Remove outer curly braces
        String[] keyValuePairs = jsonString.split(", ");
        Map<String, Object> jsonObject = new HashMap<>();

        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split("=", 2);
            String key = keyValue[0].trim().toString();
            String value = keyValue.length > 1 ? keyValue[1].trim().toString() : null;

            if (value != null && value.startsWith("{") && value.endsWith("}")) {
                // Value is a nested JSON object
                Map<String, Object> nestedObject = parseJsonObject(value);
                jsonObject.put(key, nestedObject);
            } else {
                jsonObject.put(key, value);
            }
        }
        return jsonObject;
    }
}
