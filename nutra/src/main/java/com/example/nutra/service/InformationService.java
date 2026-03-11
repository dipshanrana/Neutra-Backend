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
    private final FileStorageService fileStorageService;

    public Information addInformation(Information information, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            information.setImage(fileStorageService.storeFile(image));
        }
        return informationRepository.save(information);
    }

    public List<Information> getAllInformation() {
        return informationRepository.findAll();
    }

    public Information getInformationById(Long id) {
        return informationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Information not found with ID " + id));
    }

    public Information updateInformation(Long id, Information infoDetails, MultipartFile image) throws IOException {
        Information info = getInformationById(id);
        info.setTitle(infoDetails.getTitle());
        info.setContent(infoDetails.getContent());
        info.setCategory(infoDetails.getCategory());

        if (image != null && !image.isEmpty()) {
            fileStorageService.deleteFile(info.getImage());
            info.setImage(fileStorageService.storeFile(image));
        }
        return informationRepository.save(info);
    }

    public void deleteInformation(Long id) {
        Information info = getInformationById(id);
        fileStorageService.deleteFile(info.getImage());
        informationRepository.delete(info);
    }
}
