package com.imran.demo.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pet")
@NoArgsConstructor
@Getter
@Setter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "cid")
    private Category category;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    @Column(name = "photourl")
    private List<String> photoUrls;

    @ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinTable(name = "pet_tags",
            joinColumns = {@JoinColumn(name="id")},
            inverseJoinColumns ={@JoinColumn(name = "tid")})
    private List<Tag> tags;

    private String status;
}