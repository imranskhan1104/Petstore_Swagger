package com.imran.demo.repositories;

import com.imran.demo.entities.PetImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;

@Repository
public interface PetImageRepo extends JpaRepository<PetImages, Integer> {
}
