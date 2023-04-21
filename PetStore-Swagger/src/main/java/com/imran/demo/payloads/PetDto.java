package com.imran.demo.payloads;

import com.imran.demo.entities.Category;
import com.imran.demo.entities.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PetDto {
    private int id;
    private Category category;
    private String name;
    private List<String> photoUrls=new ArrayList<>();
    private List<Tag> tags=new ArrayList<>();
    private String status;
}
