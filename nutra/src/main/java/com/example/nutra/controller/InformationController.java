package com.example.nutra.controller;

import com.example.nutra.model.Information;
import com.example.nutra.service.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/information")
@RequiredArgsConstructor
public class InformationController {

    private final InformationService informationService;

    @PostMapping
    public ResponseEntity<Information> addInformation(@RequestBody Information info) {
        return new ResponseEntity<>(informationService.addInformation(info), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Information>> getAllInformation() {
        return ResponseEntity.ok(informationService.getAllInformation());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Information> getInformationById(@PathVariable Long id) {
        return ResponseEntity.ok(informationService.getInformationById(id));
    }
}
