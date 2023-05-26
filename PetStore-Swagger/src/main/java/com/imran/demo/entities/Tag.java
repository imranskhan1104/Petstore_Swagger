package com.imran.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "tag")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Tag {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int tid;

        private String name;
}
