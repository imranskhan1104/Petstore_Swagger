package com.imran.demo.payloads.dataset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DatasetIdDTO implements Serializable {
    private String dataset;
    private String project;
    private String projectId;

}
