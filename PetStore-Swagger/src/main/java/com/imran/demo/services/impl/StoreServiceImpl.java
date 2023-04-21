package com.imran.demo.services.impl;

import com.imran.demo.entities.Store;
import com.imran.demo.exception.ResourceNotFoundException;
import com.imran.demo.payloads.StoreDto;
import com.imran.demo.repositories.StoreRepo;
import com.imran.demo.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepo storeRepo;

    @Override
    public StoreDto placeOrder(StoreDto storeDto) {
        Store store=this.dtoToStore(storeDto);
        Store placeOrder=this.storeRepo.save(store);
        return this.storeToDto(placeOrder);
    }

    @Override
    public void deleteOrder(int orderId) {
        Store store=this.storeRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order"," id ",orderId));
        this.storeRepo.delete(store);
    }

    @Override
    public StoreDto findOrderById(int orderId) {
        Store store=this.storeRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order"," id ",orderId));
        return this.storeToDto(store);
    }

    @Override
    public HashMap<String, Integer> countByStatus() {
        List<Object[]> statusCounts = this.storeRepo.countByStatus();
        HashMap<String, Integer> statusCountsMap = new HashMap<>();
        for (Object[] statusCount : statusCounts) {
            String status = Objects.toString(statusCount[0], null);
            Integer countValue = ((BigInteger) statusCount[1]).intValue();
            statusCountsMap.put(status, countValue);
        }
       return statusCountsMap;
    }

    public Store dtoToStore(StoreDto storeDto)
    {
        Store store=new Store();

        store.setId(storeDto.getId());
        store.setPetId(storeDto.getPetId());
        store.setStatus(storeDto.getStatus());
        store.setQuantity(storeDto.getQuantity());
        store.setShipDate(storeDto.getShipDate());
        store.setComplete(storeDto.isComplete());

        return store;
    }

    public StoreDto storeToDto(Store store)
    {
        StoreDto storeDto =new StoreDto();

        storeDto.setId(store.getId());
        storeDto.setStatus(store.getStatus());
        storeDto.setQuantity(store.getQuantity());
        storeDto.setShipDate(store.getShipDate());
        storeDto.setComplete(store.isComplete());

        return storeDto;
    }
}
