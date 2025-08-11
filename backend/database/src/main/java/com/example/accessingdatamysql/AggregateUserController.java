package com.example.accessingdatamysql;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3100")
@RequestMapping("/aggregate")
public class AggregateUserController {

    private void updateFields(AggregateUser existing, AggregateUser updatedEntry) {
    if (updatedEntry.getTest() != null && !updatedEntry.getTest().isEmpty()) {
        existing.setTest(updatedEntry.getTest());
    }
    if (updatedEntry.getTestAlsoKnownAs() != null && !updatedEntry.getTestAlsoKnownAs().isEmpty()) {
        existing.setTestAlsoKnownAs(updatedEntry.getTestAlsoKnownAs());
    }
    if (updatedEntry.getGroup() != null && !updatedEntry.getGroup().isEmpty()) {
        existing.setGroup(updatedEntry.getGroup());
    }
    if (updatedEntry.getClassification() != null && !updatedEntry.getClassification().isEmpty()) {
        existing.setClassification(updatedEntry.getClassification());
    }
    if (updatedEntry.getSymbol() != null && !updatedEntry.getSymbol().isEmpty()) {
        existing.setSymbol(updatedEntry.getSymbol());
    }
    if (updatedEntry.getParameters() != null && !updatedEntry.getParameters().isEmpty()) {
        existing.setParameters(updatedEntry.getParameters());
    }
    if (updatedEntry.getTestMethod() != null && !updatedEntry.getTestMethod().isEmpty()) {
        existing.setTestMethod(updatedEntry.getTestMethod());
    }
    if (updatedEntry.getSampleType() != null && !updatedEntry.getSampleType().isEmpty()) {
        existing.setSampleType(updatedEntry.getSampleType());
    }
    if (updatedEntry.getFieldSampleMass() != null && !updatedEntry.getFieldSampleMass().isEmpty()) {
        existing.setFieldSampleMass(updatedEntry.getFieldSampleMass());
    }
    if (updatedEntry.getSpecimenType() != null && !updatedEntry.getSpecimenType().isEmpty()) {
        existing.setSpecimenType(updatedEntry.getSpecimenType());
    }
    if (updatedEntry.getSpecimenMass() != null && !updatedEntry.getSpecimenMass().isEmpty()) {
        existing.setSpecimenMass(updatedEntry.getSpecimenMass());
    }
    if (updatedEntry.getSpecimenNumbers() != null && !updatedEntry.getSpecimenNumbers().isEmpty()) {
        existing.setSpecimenNumbers(updatedEntry.getSpecimenNumbers());
    }
    if (updatedEntry.getSpecimenMaxGrainSize() != null && !updatedEntry.getSpecimenMaxGrainSize().isEmpty()) {
        existing.setSpecimenMaxGrainSize(updatedEntry.getSpecimenMaxGrainSize());
    }
    if (updatedEntry.getSpecimenMaxGrainFraction() != null && !updatedEntry.getSpecimenMaxGrainFraction().isEmpty()) {
        existing.setSpecimenMaxGrainFraction(updatedEntry.getSpecimenMaxGrainFraction());
    }
    if (updatedEntry.getDatabaseBelongsTo() != null && !updatedEntry.getDatabaseBelongsTo().isEmpty()) {
        existing.setDatabaseBelongsTo(updatedEntry.getDatabaseBelongsTo());
    }
    
    if (updatedEntry.getSchedulingNotes() != null && !updatedEntry.getSchedulingNotes().isEmpty()) {
        existing.setSchedulingNotes(updatedEntry.getSchedulingNotes());
    }
    if (updatedEntry.getImagePath() != null && !updatedEntry.getImagePath().isEmpty()) {
        existing.setImagePath(updatedEntry.getImagePath());
    }
}




    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AggregateUserRepository AggregateUserRepository;

    // Add new record
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<AggregateUser> addAggregateUserWithImage(
        @RequestPart("data") String aggregateEntryJson,
        @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            AggregateUser entry = objectMapper.readValue(aggregateEntryJson, AggregateUser.class);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir, fileName);
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                entry.setImagePath(Paths.get(uploadDir).resolve(fileName).toString());
                imagePath.toFile().setReadable(true, false);
                imagePath.toFile().setWritable(true, false);
            }

            AggregateUser savedEntry = AggregateUserRepository.save(entry);
            return ResponseEntity.ok(savedEntry);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<AggregateUser> updateAggregateUserWithImage(
            @PathVariable Long id,
            @RequestPart("data") String updatedEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        System.out.println("=== [DEBUG] Received update-with-image request for ID: " + id + " ===");

        Optional<AggregateUser> existingOpt = AggregateUserRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            AggregateUser updatedEntry = objectMapper.readValue(updatedEntryJson, AggregateUser.class);
            AggregateUser existing = existingOpt.get();
            updateFields(existing, updatedEntry);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = image.getOriginalFilename();
                Path newImagePath = Paths.get(uploadDir, fileName);

                // Delete old image if it exists
                String oldImagePath = existing.getImagePath();
                if (oldImagePath != null && !oldImagePath.isEmpty()) {
                    try {
                        Path oldPath = Paths.get(uploadDir, Paths.get(oldImagePath).getFileName().toString()).normalize();
                        Path basePath = Paths.get(uploadDir).toAbsolutePath().normalize();

                        if (!oldPath.toAbsolutePath().startsWith(basePath)) {
                            System.err.println("[WARN] Attempted to delete image outside of upload directory: " + oldPath);
                        } else if (Files.exists(oldPath)) {
                            Files.delete(oldPath);
                            System.out.println("[DEBUG] Deleted old image: " + oldPath);
                        }
                    } catch (IOException ex) {
                        System.err.println("[WARN] Failed to delete old image: " + ex.getMessage());
                    }
                }


                // Save new image
                Files.copy(image.getInputStream(), newImagePath, StandardCopyOption.REPLACE_EXISTING);
                existing.setImagePath(Paths.get(uploadDir).resolve(fileName).toString());
                newImagePath.toFile().setReadable(true, false);
                newImagePath.toFile().setWritable(true, false);
                System.out.println("[DEBUG] Saved new image: " + newImagePath);
            }

            AggregateUser saved = AggregateUserRepository.save(existing);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update entry with image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping(path = "/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return AggregateUserRepository.findAllGroups();
    }



    // Update existing record
    @PutMapping("/update/{id}")
    public AggregateUser updateAggregateUser(@PathVariable Long id, @RequestBody AggregateUser updatedUser) {
        Optional<AggregateUser> optionalUser = AggregateUserRepository.findById(id);
        if (optionalUser.isPresent()) {
            AggregateUser existingUser = optionalUser.get();
            existingUser.setTest(updatedUser.getTest());
            existingUser.setGroup(updatedUser.getGroup());
            existingUser.setClassification(updatedUser.getClassification());
            existingUser.setSymbol(updatedUser.getSymbol());
            existingUser.setParameters(updatedUser.getParameters());
            existingUser.setTestMethod(updatedUser.getTestMethod());
            existingUser.setSampleType(updatedUser.getSampleType());
            existingUser.setFieldSampleMass(updatedUser.getFieldSampleMass());
            existingUser.setSpecimenType(updatedUser.getSpecimenType());
            existingUser.setSpecimenMass(updatedUser.getSpecimenMass());
            existingUser.setSpecimenNumbers(updatedUser.getSpecimenNumbers());
            existingUser.setSpecimenMaxGrainSize(updatedUser.getSpecimenMaxGrainSize());
            existingUser.setSpecimenMaxGrainFraction(updatedUser.getSpecimenMaxGrainFraction());
            existingUser.setSchedulingNotes(updatedUser.getSchedulingNotes());
            existingUser.setDatabaseBelongsTo(updatedUser.getDatabaseBelongsTo());

            return AggregateUserRepository.save(existingUser);
        } else {
            throw new RuntimeException("AggregateUser not found with id " + id);
        }
    }

    // Delete by ID
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<AggregateUser> entryOpt = AggregateUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            AggregateUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    // Get all records, optional sort
    @GetMapping("/all")
    public List<AggregateUser> getAllAggregateUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return AggregateUserRepository.findAll();
        }
        switch (sort) {
            case "default":
                return AggregateUserRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "name":
                return AggregateUserRepository.findAll(Sort.by(Sort.Direction.ASC, "group"));
            case "method":
                return AggregateUserRepository.findAll(Sort.by(Sort.Direction.ASC, "testMethod"));
            case "parameter":
                return AggregateUserRepository.findAll(Sort.by(Sort.Direction.ASC, "parameters"));
            default:
                return AggregateUserRepository.findAll();
        }
    }

    // Precise search by ID
    @GetMapping("/id")
    public List<AggregateUser> getById(@RequestParam String id) {
        try {
            Long longId = Long.parseLong(id);
            Optional<AggregateUser> opt = AggregateUserRepository.findById(longId);
            return opt.map(List::of).orElse(List.of());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    // Fuzzy search endpoints
    @GetMapping("/group")
    public List<AggregateUser> getByGroup(@RequestParam String group) {
        return AggregateUserRepository.findByGroupContainingIgnoreCase(group);
    }

    @GetMapping("/test")
    public List<AggregateUser> getByTest(@RequestParam String test) {
        return AggregateUserRepository.findByTestContainingIgnoreCase(test);
    }
    @GetMapping("/testAlsoKnownAs")
    public List<AggregateUser> getByTestAlsoKnown(@RequestParam String testAlsoKnownAs) {
        return AggregateUserRepository.findByTestAlsoKnownAs(testAlsoKnownAs);
    }

    @GetMapping("/symbol")
    public List<AggregateUser> getBySymbol(@RequestParam String symbol) {
        return AggregateUserRepository.findBySymbolContainingIgnoreCase(symbol);
    }

    @GetMapping("/parameters")
    public List<AggregateUser> getByParameters(@RequestParam String parameters) {
        return AggregateUserRepository.findByParametersContainingIgnoreCase(parameters);
    }

    @GetMapping("/testMethod")
    public List<AggregateUser> getByTestMethod(@RequestParam String testMethod) {
        return AggregateUserRepository.findByTestMethodContainingIgnoreCase(testMethod);
    }

    @GetMapping("/classification")
    public List<AggregateUser> getByClassification(@RequestParam String classification) {
        return AggregateUserRepository.findByClassification(classification);
    }

    @GetMapping("/sampleType")
    public List<AggregateUser> getBySampleType(@RequestParam String sampleType) {
        return AggregateUserRepository.findBySampleTypeContainingIgnoreCase(sampleType);
    }

    @GetMapping("/fieldSampleMass")
    public List<AggregateUser> getByFieldSampleMass(@RequestParam String fieldSampleMass) {
        return AggregateUserRepository.findByFieldSampleMassContainingIgnoreCase(fieldSampleMass);
    }

    @GetMapping("/specimenType")
    public List<AggregateUser> getBySpecimenType(@RequestParam String specimenType) {
        return AggregateUserRepository.findBySpecimenTypeContainingIgnoreCase(specimenType);
    }

    @GetMapping("/specimenMass")
    public List<AggregateUser> getBySpecimenMass(@RequestParam String specimenMass) {
        return AggregateUserRepository.findBySpecimenMassContainingIgnoreCase(specimenMass);
    }

    @GetMapping("/specimenNumbers")
    public List<AggregateUser> getBySpecimenNumbers(@RequestParam String specimenNumbers) {
        return AggregateUserRepository.findBySpecimenNumbersContainingIgnoreCase(specimenNumbers);
    }

    @GetMapping("/specimenMaxGrainSize")
    public List<AggregateUser> getBySpecimenMaxGrainSize(@RequestParam String specimenMaxGrainSize) {
        return AggregateUserRepository.findBySpecimenMaxGrainSizeContainingIgnoreCase(specimenMaxGrainSize);
    }

    @GetMapping("/specimenMaxGrainFraction")
    public List<AggregateUser> getBySpecimenMaxGrainFraction(@RequestParam String specimenMaxGrainFraction) {
        return AggregateUserRepository.findBySpecimenMaxGrainFractionContainingIgnoreCase(specimenMaxGrainFraction);
    }

    @GetMapping("/schedulingNotes")
    public List<AggregateUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return AggregateUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

    @GetMapping("/databaseBelongsTo")
    public List<AggregateUser> getByDatabaseBelongsTo(@RequestParam String databaseBelongsTo) {
        return AggregateUserRepository.findByDatabaseBelongsToContainingIgnoreCase(databaseBelongsTo);
    }

    @GetMapping("/imagePath")
    public List<AggregateUser> getByImagePathBelongsTo(@RequestParam String imagePath) {
        return AggregateUserRepository.findByImagePathContainingIgnoreCase(imagePath);
    }
}
