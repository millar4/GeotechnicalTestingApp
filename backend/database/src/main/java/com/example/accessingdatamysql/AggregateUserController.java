package com.example.accessingdatamysql;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "http://localhost:3100")
@RequestMapping("/aggregate")
public class AggregateUserController {

    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AggregateUserRepository AggregateUserRepository;

    private void updateFields(AggregateUser existing, AggregateUser updatedEntry) {
        if (updatedEntry.getTest() != null && !updatedEntry.getTest().isEmpty()) existing.setTest(updatedEntry.getTest());
        if (updatedEntry.getGroup() != null) existing.setGroup(updatedEntry.getGroup());
        if (updatedEntry.getClassification() != null) existing.setClassification(updatedEntry.getClassification());
        if (updatedEntry.getSymbol() != null) existing.setSymbol(updatedEntry.getSymbol());
        if (updatedEntry.getParameters() != null) existing.setParameters(updatedEntry.getParameters());
        if (updatedEntry.getTestMethod() != null) existing.setTestMethod(updatedEntry.getTestMethod());
        if (updatedEntry.getSampleType() != null) existing.setSampleType(updatedEntry.getSampleType());
        if (updatedEntry.getFieldSampleMass() != null) existing.setFieldSampleMass(updatedEntry.getFieldSampleMass());
        if (updatedEntry.getSpecimenType() != null) existing.setSpecimenType(updatedEntry.getSpecimenType());
        if (updatedEntry.getSpecimenMass() != null) existing.setSpecimenMass(updatedEntry.getSpecimenMass());
        if (updatedEntry.getSpecimenNumbers() != null) existing.setSpecimenNumbers(updatedEntry.getSpecimenNumbers());
        if (updatedEntry.getSpecimenMaxGrainSize() != null) existing.setSpecimenMaxGrainSize(updatedEntry.getSpecimenMaxGrainSize());
        if (updatedEntry.getSpecimenMaxGrainFraction() != null) existing.setSpecimenMaxGrainFraction(updatedEntry.getSpecimenMaxGrainFraction());
        if (updatedEntry.getSchedulingNotes() != null) existing.setSchedulingNotes(updatedEntry.getSchedulingNotes());
        if (updatedEntry.getTestDescription() != null) existing.setTestDescription(updatedEntry.getTestDescription());
        if (updatedEntry.getDatabaseBelongsTo() != null) existing.setDatabaseBelongsTo(updatedEntry.getDatabaseBelongsTo());
    }

 @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<AggregateUser> addGeotechnicalEntryWithImage(
        @RequestPart("data") String aggregateEntryJson,
        @RequestPart(value = "image", required = false) MultipartFile image) {
        

        try {
            System.out.println("Incoming JSON: " + aggregateEntryJson);
            AggregateUser entry = objectMapper.readValue(aggregateEntryJson, AggregateUser.class);
            System.out.println("Test: " + entry.getTest());
            System.out.println("Group: " + entry.getGroup());
            System.out.println("Classification: " + entry.getClassification());
            System.out.println("Mapped object: " + entry);
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

        Optional<AggregateUser> existingOpt = AggregateUserRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        try {
            AggregateUser updatedEntry = objectMapper.readValue(updatedEntryJson, AggregateUser.class);
            AggregateUser existing = existingOpt.get();
            updateFields(existing, updatedEntry);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));

                // Delete old image if exists
                if (existing.getImagePath() != null && !existing.getImagePath().isEmpty()) {
                    Path oldPath = Paths.get(existing.getImagePath()).normalize();
                    if (Files.exists(oldPath)) Files.delete(oldPath);
                }

                Path newImagePath = Paths.get(uploadDir, image.getOriginalFilename());
                Files.copy(image.getInputStream(), newImagePath, StandardCopyOption.REPLACE_EXISTING);
                existing.setImagePath(newImagePath.toString());
            }

            AggregateUser saved = AggregateUserRepository.save(existing);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AggregateUser> updateAggregateUser(@PathVariable Long id, @RequestBody AggregateUser updatedUser) {
        Optional<AggregateUser> optionalUser = AggregateUserRepository.findById(id);
        if (optionalUser.isEmpty()) return ResponseEntity.notFound().build();

        AggregateUser existingUser = optionalUser.get();
        updateFields(existingUser, updatedUser);
        return ResponseEntity.ok(AggregateUserRepository.save(existingUser));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<AggregateUser> entryOpt = AggregateUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            AggregateUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public List<AggregateUser> getAllAggregateUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) return AggregateUserRepository.findAll();
        switch (sort) {
            case "default": return AggregateUserRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "name": return AggregateUserRepository.findAll(Sort.by(Sort.Direction.ASC, "group"));
            case "method": return AggregateUserRepository.findAll(Sort.by(Sort.Direction.ASC, "testMethod"));
            case "parameter": return AggregateUserRepository.findAll(Sort.by(Sort.Direction.ASC, "parameters"));
            default: return AggregateUserRepository.findAll();
        }
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return AggregateUserRepository.findAllGroups();
    }

    @GetMapping("/id")
    public List<AggregateUser> getById(@RequestParam String id) {
        try {
            Long longId = Long.parseLong(id);
            return AggregateUserRepository.findById(longId).map(List::of).orElse(List.of());
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
    public List<AggregateUser> getByTestAlsoKnownAs(@RequestParam String test) {
        return AggregateUserRepository.findByTestAlsoKnownAs(test);
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

    @GetMapping("/testDescription")
    public List<AggregateUser> getByTestDescription(@RequestParam String testDescription) {
        return AggregateUserRepository.findByTestDescriptionContainingIgnoreCase(testDescription);
    }

    @GetMapping("/databaseBelongsTo")
    public List<AggregateUser> getByDatabaseBelongsTo(@RequestParam String databaseBelongsTo) {
        return AggregateUserRepository.findByDatabaseBelongsToContainingIgnoreCase(databaseBelongsTo);
    }
}
