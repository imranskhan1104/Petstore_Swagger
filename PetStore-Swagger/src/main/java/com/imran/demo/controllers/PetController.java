package com.imran.demo.controllers;

import com.imran.demo.entities.Pet;
import com.imran.demo.payloads.PetDto;
import com.imran.demo.repositories.PetRepo;
import com.imran.demo.response.ApiResponse;
import com.imran.demo.services.PetService;
import com.imran.demo.utils.Variable;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/pets")
@Api(value = "PET", description = "Everything about your Pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepo petRepo;

    @Autowired
    Variable variable;

    @PostMapping("/")
    @ApiOperation(value = "Add a new pet to the store")
    public ResponseEntity<?> addPet(@ApiParam(name = "Pet Body",value = "Pet object that needs to be added to the store", required = true)
                                    @RequestBody PetDto petDto) {
        if (variable.isToken()) {
            PetDto createPetDto = this.petService.addPet(petDto);
            return new ResponseEntity<>(createPetDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{petId}")
    @ApiOperation(value = "Find pet by ID", notes = "Returns a single pet")
    public ResponseEntity<?> getPetById(@ApiParam(name = "petId",value = "ID of pet to return", required = true) Integer petId) {
        if (variable.isToken()) {
            for (Pet a : (this.petRepo.findAll())) {
                if (petId.equals(a.getId())) {
                    return ResponseEntity.ok(this.petService.getPetByID(petId));
                }
            }
            return new ResponseEntity<ApiResponse>(new ApiResponse("No pet with given pet ID", false), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{petId}")
    @ApiOperation(value = "Update an existing pet")
    public ResponseEntity<?> updatePet(@ApiParam(name = "Pet Body",value = "Pet object that needs to be added to the store", required = true)
                                       @RequestBody PetDto petDto,
                                       @PathVariable("petId") Integer petId) {
        if (variable.isToken()) {
            for (Pet a : (this.petRepo.findAll())) {
                if (petId.equals(a.getId())) {
                    PetDto updatePet = this.petService.updatePet(petDto, petId);
                    return ResponseEntity.ok(updatePet);
                }
            }
            return new ResponseEntity<ApiResponse>(new ApiResponse("No pet with given pet ID", false), HttpStatus.BAD_REQUEST);

        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/form-data")
    @ApiOperation(value = "Update a pet in the store with form data")
    public ResponseEntity<ApiResponse> updateWithFormData(@RequestParam(name = "PetId") Integer petId,
                                                          @RequestParam(name = "Name", required = false) String name,
                                                          @RequestParam(name = "Status", required = false) String status) {
        if (variable.isToken()) {
            for (Pet a : (this.petRepo.findAll())) {
                if (petId.equals(a.getId())) {
                    Pet pet=this.petRepo.findById(petId).get();
                    if (name == null) {
                        name=pet.getName();
                    }
                    if (status == null) {
                        status=pet.getStatus();
                    }
                    this.petService.updateWithFormData(petId, name, status);
                    return new ResponseEntity<ApiResponse>(new ApiResponse("Pet Updated Successfully", true), HttpStatus.OK);
                }
            }
            return new ResponseEntity<ApiResponse>(new ApiResponse("No pet with given pet ID", false), HttpStatus.BAD_REQUEST);

        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{petId}")
    @ApiOperation(value = "Deletes a pet")
    public ResponseEntity<ApiResponse> deletePet(@RequestParam(name = "Pet Id") Integer petId) {
        if (variable.isToken()) {
            for (Pet a : (this.petRepo.findAll())) {
                if (petId.equals(a.getId())) {
                    this.petService.deletePet(petId);
                    return new ResponseEntity<ApiResponse>(new ApiResponse("Pet Deleted Successfully", true), HttpStatus.OK);
                }
            }
            return new ResponseEntity<ApiResponse>(new ApiResponse("No pet with given pet ID", false), HttpStatus.BAD_REQUEST);

        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/{petId}/upload-image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Uploads an image")
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam(name = "petId") Integer petId,
                                                   @RequestPart(name = "uploadImage") MultipartFile file) throws IOException {
        if (variable.isToken()) {
            for (Pet a : (this.petRepo.findAll())) {
                if (petId.equals(a.getId())) {
                        this.petService.uploadImage(file, petId);
                        return new ResponseEntity<ApiResponse>(new ApiResponse("Pet Image Uploaded Successfully", true), HttpStatus.OK);
                }}
            return new ResponseEntity<ApiResponse>(new ApiResponse("No pet with given pet ID", false), HttpStatus.BAD_REQUEST);

        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{status}/status")
    @ApiOperation(value = "Find Pets by Status",notes="Multiple status values can be provided with comma separated strings")
    public ResponseEntity<?> getPetByStatus(@ApiParam(name = "status",value = "Status values that need to be considered for filter",
                                                            allowableValues = "available,pending,sold") String status) {
        if (variable.isToken()) {
            List<PetDto> getPetByStatus = this.petService.getPetByStatus(String.valueOf(status));
            return ResponseEntity.ok(getPetByStatus);
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }
}

