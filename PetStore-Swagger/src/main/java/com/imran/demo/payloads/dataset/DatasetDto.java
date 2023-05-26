package com.imran.demo.payloads.dataset;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DatasetDto implements Serializable {
    private List<?> acl;
    private long creationTime;
    private DatasetIdDTO datasetId;
    private long defaultPartitionExpirationMs;
    private long defaultTableLifetime;
    private String description;
    private String etag;
    private String friendlyName;
    private String generatedId;
    private Map<String, String> labels;
    private long lastModified;
    private String projectId;
    private String selfLink;

}
