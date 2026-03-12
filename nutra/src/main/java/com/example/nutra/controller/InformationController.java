package com.example.nutra.controller;

import com.example.nutra.model.Information;
import com.example.nutra.service.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/information")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class InformationController {

    private final InformationService informationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Information> addInformation(
            @RequestPart("information") Information info,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return new ResponseEntity<>(informationService.addInformation(info, image), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Information>> getAllInformation() {
        return ResponseEntity.ok(informationService.getAllInformation());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Information> getInformationById(@PathVariable Long id) {
        return ResponseEntity.ok(informationService.getInformationById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Information> updateInformation(
            @PathVariable Long id,
            @RequestPart("information") Information info,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(informationService.updateInformation(id, info, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInformation(@PathVariable Long id) {
        informationService.deleteInformation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Information>> getInformationByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(informationService.getInformationByCategory(categoryName));
    }
}
