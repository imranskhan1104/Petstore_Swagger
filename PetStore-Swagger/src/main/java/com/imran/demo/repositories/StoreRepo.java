package com.imran.demo.repositories;

import com.imran.demo.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreRepo extends JpaRepository<Store, Integer> {

    @Query(value = "SELECT status, COUNT(status) as count FROM store GROUP BY status",nativeQuery = true)
    List<Object[]> countByStatus();
}
