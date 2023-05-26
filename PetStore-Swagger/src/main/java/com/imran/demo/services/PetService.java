package com.imran.demo.services;

import com.imran.demo.payloads.PetDto;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface PetService {
    PetDto getPetByID(Integer petId);
    PetDto addPet(PetDto petDto);
    PetDto updatePet(PetDto petDto, Integer petId);
    void updateWithFormData (Integer petId, String name, String status);
    void deletePet(Integer petId);
    void uploadImage(MultipartFile file, Integer petId) throws IOException;
    List<PetDto> getPetByStatus(String status);
}
