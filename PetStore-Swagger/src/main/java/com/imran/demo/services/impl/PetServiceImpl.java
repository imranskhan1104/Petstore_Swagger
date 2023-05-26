package com.imran.demo.services.impl;

import com.imran.demo.entities.*;
import com.imran.demo.exception.ResourceNotFoundException;
import com.imran.demo.payloads.PetDto;
import com.imran.demo.repositories.PetImageRepo;
import com.imran.demo.repositories.PetRepo;
import com.imran.demo.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepo petRepo;

    @Autowired
    private PetImageRepo petImageRepo;


    @Override
    @Cacheable(value = "pets", key = "#petId")
    public PetDto getPetByID(Integer petId) {
        Pet pet =this.petRepo.findById(petId).orElseThrow(()-> new ResourceNotFoundException("Pet"," id ",petId));
        return this.petToDto(pet);
    }

    @Override
    @CacheEvict(value = "pets", key = "#result.id")
    public PetDto addPet(PetDto petDto) {
        Pet pet=this.dtoToPet(petDto);
        Pet savedPet = this.petRepo.save(pet);
        return this.petToDto(savedPet);
    }

    @Override
    @CacheEvict(value = "pets", key = "#petId")
    public PetDto updatePet(PetDto petDto, Integer petId) {
        Pet pet =this.petRepo.findById(petId).orElseThrow(()-> new ResourceNotFoundException("Pet"," id ",petId));
        pet.setCategory(petDto.getCategory());
        pet.setName(petDto.getName());
        pet.setPhotoUrls(petDto.getPhotoUrls());
        pet.setTags(petDto.getTags());
        pet.setStatus(petDto.getStatus());

        Pet updatePet=this.petRepo.save(pet);
        return this.petToDto(updatePet);
    }

    @Override
    @CacheEvict(value = "pets", key = "#petId")
    public void updateWithFormData(Integer petId, String name, String status) {
        Pet pet =this.petRepo.findById(petId).orElseThrow(()-> new ResourceNotFoundException("Pet"," id ",petId));

        pet.setName(name);
        pet.setStatus(status);
        this.petRepo.save(pet);
    }

    @Override
    @CacheEvict(value = "pets", key = "#petId")
    public void deletePet(Integer petId) {
        Pet pet=this.petRepo.findById(petId).orElseThrow(()-> new ResourceNotFoundException("Pet"," id ",petId));
        this.petRepo.delete(pet);
    }

    @Override
    @CacheEvict(value = "pets", key = "#petId")
    public void uploadImage(MultipartFile file, Integer petId) throws IOException {
        PetImages pet=new PetImages();
        pet.setImage(file.getBytes());
        pet.setId(petId);
        pet.setName(file.getOriginalFilename());
        this.petImageRepo.save(pet);
    }

    @Override
    @Cacheable(value = "petsByStatus", key = "#status")
    public List<PetDto> getPetByStatus(String status) {
        List<Pet> petList = this.petRepo.findByStatus(status);
        List<PetDto> petDtoList = new ArrayList<>();
        for (Pet pet : petList) {
            PetDto petDto = this.petToDto(pet);
            petDtoList.add(petDto);
        }
        return petDtoList;
    }

    public Pet dtoToPet(PetDto petDto)
    {
        Pet pet=new Pet();
        pet.setId(petDto.getId());
        pet.setCategory(petDto.getCategory());
        pet.setName(petDto.getName());
        pet.setPhotoUrls(petDto.getPhotoUrls());
        pet.setTags(petDto.getTags());
        pet.setStatus(petDto.getStatus());

        return pet;
    }

    public PetDto petToDto(Pet pet) {
        PetDto petDto = new PetDto();
        petDto.setId(pet.getId());
        petDto.setCategory(new Category(pet.getCategory().getCid(), pet.getCategory().getName()));
        petDto.setName(pet.getName());
        petDto.setPhotoUrls(pet.getPhotoUrls());
        List<Tag> tag1 = pet.getTags().stream().map(tag -> new Tag(tag.getTid(), tag.getName())).collect(Collectors.toList());
        petDto.setTags(tag1);
        petDto.setStatus(pet.getStatus());
        return petDto;
    }
}
