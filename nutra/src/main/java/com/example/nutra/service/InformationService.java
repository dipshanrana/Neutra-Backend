package com.example.nutra.service;

import com.example.nutra.Exception.ResourceNotFoundException;
import com.example.nutra.model.Information;
import com.example.nutra.repository.InformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;

    public Information addInformation(Information info) {
        return informationRepository.save(info);
    }

    public List<Information> getAllInformation() {
        return informationRepository.findAll();
    }

    public Information getInformationById(Long id) {
        return informationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Information not found with ID " + id));
    }
}
