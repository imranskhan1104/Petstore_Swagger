package com.imran.demo.controllers;

import com.imran.demo.entities.Pet;
import com.imran.demo.entities.Store;
import com.imran.demo.entities.User;
import com.imran.demo.payloads.StoreDto;
import com.imran.demo.repositories.PetRepo;
import com.imran.demo.repositories.StoreRepo;
import com.imran.demo.response.ApiResponse;
import com.imran.demo.services.StoreService;
import com.imran.demo.utils.Variable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/store")
@Api(value = "STORE", description = "Access to Petstore orders")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private PetRepo petRepo;

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    Variable variable;

    @PostMapping("/")
    @ApiOperation(value = "Place an order for a pet")
    public ResponseEntity<?> placeOrder(@ApiParam(name = "Order Body",value = "Order placed for purchasing the pet")
                                        @RequestBody StoreDto storeDto)
    {
        if (variable.isToken()) {
            Optional<Pet> pet = this.petRepo.findById(storeDto.getPetId());

            if (pet.isPresent()) {
                StoreDto placeOrder = this.storeService.placeOrder(storeDto);
                return new ResponseEntity<>(placeOrder, HttpStatus.CREATED);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("No pet with the given pet ID", false));
            }
        }
        else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{orderId}")
    @ApiOperation(value = "Delete purchase order by Id", notes = "For valid response try integer IDs with positive integer value. Negative or non-integer values will generate API errors")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable("orderId") Integer orderId)
    {
        if (variable.isToken()) {
            Optional<Store> store= this.storeRepo.findById(orderId);
            if (store.isPresent()) {
                this.storeService.deleteOrder(orderId);
            }
            else{
                return new ResponseEntity<ApiResponse>(new ApiResponse("No order with respect to given ID", false), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<ApiResponse>(new ApiResponse("Order Deleted Successfully", true), HttpStatus.OK);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "Find purchase order by Id")
    public ResponseEntity<?> findOrderById(@PathVariable("orderId")Integer orderId)
    {
        if (variable.isToken()) {
            Optional<Store> store= this.storeRepo.findById(orderId);
            if (store.isPresent()) {
                return ResponseEntity.ok(this.storeService.findOrderById(orderId));
            }else {
                return new ResponseEntity<ApiResponse>(new ApiResponse("No order with respect to given ID", false), HttpStatus.NOT_FOUND);
            }
        }
        else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    @ApiOperation(value = "Returns pet inventories by status", notes = "Returns a map of status codes to quantities")
    public ResponseEntity<?> countByStatus()
    {
        if (variable.isToken()) {
            return ResponseEntity.ok(this.storeService.countByStatus());
        }
        else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }
}
