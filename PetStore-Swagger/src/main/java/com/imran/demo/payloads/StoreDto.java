package com.imran.demo.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoreDto implements Serializable {
    private Integer id;
    private int petId;
    private  int quantity;
    private LocalDateTime shipDate;
    private String status;
    private boolean complete;
}
