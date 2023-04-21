package com.imran.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "pet_image")
@NoArgsConstructor
@Getter
@Setter
public class PetImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinTable(name = "pet_images",
            joinColumns = {@JoinColumn(name="id")},
            inverseJoinColumns ={@JoinColumn(name = "id")})
    private int id;

    @Lob
    private byte[] image;

    private String name;
}
