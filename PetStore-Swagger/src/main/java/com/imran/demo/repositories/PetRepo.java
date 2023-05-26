package com.imran.demo.repositories;

import com.imran.demo.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface PetRepo extends JpaRepository<Pet,Integer> {

    @Transactional
    @Query(value = "select p from Pet p where p.status=?1",nativeQuery = false)
    public List<Pet> findByStatus(String status);
}

