package com.example.nutra.service;

import com.example.nutra.Exception.ResourceNotFoundException;
import com.example.nutra.model.Information;
import com.example.nutra.repository.InformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;

    public Information addInformation(Information info, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            info.setImage(image.getBytes());
        }
        return informationRepository.save(info);
    }

    public List<Information> getAllInformation() {
        return informationRepository.findAll();
    }

    public Information getInformationById(Long id) {
        return informationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Information not found with ID " + id));
    }

    public Information updateInformation(Long id, Information updatedInfo, MultipartFile image) throws IOException {
        Information existingInfo = getInformationById(id);
        existingInfo.setTitle(updatedInfo.getTitle());
        existingInfo.setContent(updatedInfo.getContent());
        existingInfo.setCategory(updatedInfo.getCategory());
        if (image != null && !image.isEmpty()) {
            existingInfo.setImage(image.getBytes());
        }
        return informationRepository.save(existingInfo);
    }

    public void deleteInformation(Long id) {
        Information info = getInformationById(id);
        informationRepository.delete(info);
    }
}
