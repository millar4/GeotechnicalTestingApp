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
@RequestMapping("/earthworks")
public class EarthworksUserController {

    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EarthworksUserRepository earthworksUserRepository;

    private void updateFields(EarthworksUser existing, EarthworksUser updatedEntry) {
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
    public ResponseEntity<EarthworksUser> addGeotechnicalEntryWithImage(
        @RequestPart("data") String aggregateEntryJson,
        @RequestPart(value = "image", required = false) MultipartFile image) {
        

        try {
            System.out.println("Incoming JSON: " + aggregateEntryJson);
            EarthworksUser entry = objectMapper.readValue(aggregateEntryJson, EarthworksUser.class);
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

            EarthworksUser savedEntry = earthworksUserRepository.save(entry);
            return ResponseEntity.ok(savedEntry);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<EarthworksUser> updateEarthworksUserWithImage(
            @PathVariable Long id,
            @RequestPart("data") String updatedEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Optional<EarthworksUser> existingOpt = earthworksUserRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        try {
            EarthworksUser updatedEntry = objectMapper.readValue(updatedEntryJson, EarthworksUser.class);
            EarthworksUser existing = existingOpt.get();
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

            EarthworksUser saved = earthworksUserRepository.save(existing);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EarthworksUser> updateEarthworksUser(@PathVariable Long id, @RequestBody EarthworksUser updatedUser) {
        Optional<EarthworksUser> optionalUser = earthworksUserRepository.findById(id);
        if (optionalUser.isEmpty()) return ResponseEntity.notFound().build();

        EarthworksUser existingUser = optionalUser.get();
        updateFields(existingUser, updatedUser);
        return ResponseEntity.ok(earthworksUserRepository.save(existingUser));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<EarthworksUser> entryOpt = earthworksUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            earthworksUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public List<EarthworksUser> getAllEarthworksUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) return earthworksUserRepository.findAll();
        switch (sort) {
            case "default": return earthworksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "name": return earthworksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "group"));
            case "method": return earthworksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "testMethod"));
            case "parameter": return earthworksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "parameters"));
            default: return earthworksUserRepository.findAll();
        }
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return earthworksUserRepository.findAllGroups();
    }

    @GetMapping("/id")
    public List<EarthworksUser> getById(@RequestParam String id) {
        try {
            Long longId = Long.parseLong(id);
            return earthworksUserRepository.findById(longId).map(List::of).orElse(List.of());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    // Fuzzy search endpoints
    @GetMapping("/group")
    public List<EarthworksUser> getByGroup(@RequestParam String group) {
        return earthworksUserRepository.findByGroupContainingIgnoreCase(group);
    }

    @GetMapping("/test")
    public List<EarthworksUser> getByTest(@RequestParam String test) {
        return earthworksUserRepository.findByTestContainingIgnoreCase(test);
    }

    @GetMapping("/testAlsoKnownAs")
    public List<EarthworksUser> getByTestAlsoKnownAs(@RequestParam String test) {
        return earthworksUserRepository.findByTestAlsoKnownAs(test);
    }

    @GetMapping("/symbol")
    public List<EarthworksUser> getBySymbol(@RequestParam String symbol) {
        return earthworksUserRepository.findBySymbolContainingIgnoreCase(symbol);
    }

    @GetMapping("/parameters")
    public List<EarthworksUser> getByParameters(@RequestParam String parameters) {
        return earthworksUserRepository.findByParametersContainingIgnoreCase(parameters);
    }

    @GetMapping("/testMethod")
    public List<EarthworksUser> getByTestMethod(@RequestParam String testMethod) {
        return earthworksUserRepository.findByTestMethodContainingIgnoreCase(testMethod);
    }

    @GetMapping("/classification")
    public List<EarthworksUser> getByClassification(@RequestParam String classification) {
        return earthworksUserRepository.findByClassification(classification);
    }

    @GetMapping("/sampleType")
    public List<EarthworksUser> getBySampleType(@RequestParam String sampleType) {
        return earthworksUserRepository.findBySampleTypeContainingIgnoreCase(sampleType);
    }

    @GetMapping("/fieldSampleMass")
    public List<EarthworksUser> getByFieldSampleMass(@RequestParam String fieldSampleMass) {
        return earthworksUserRepository.findByFieldSampleMassContainingIgnoreCase(fieldSampleMass);
    }

    @GetMapping("/specimenType")
    public List<EarthworksUser> getBySpecimenType(@RequestParam String specimenType) {
        return earthworksUserRepository.findBySpecimenTypeContainingIgnoreCase(specimenType);
    }

    @GetMapping("/specimenMass")
    public List<EarthworksUser> getBySpecimenMass(@RequestParam String specimenMass) {
        return earthworksUserRepository.findBySpecimenMassContainingIgnoreCase(specimenMass);
    }

    @GetMapping("/specimenNumbers")
    public List<EarthworksUser> getBySpecimenNumbers(@RequestParam String specimenNumbers) {
        return earthworksUserRepository.findBySpecimenNumbersContainingIgnoreCase(specimenNumbers);
    }

    @GetMapping("/specimenMaxGrainSize")
    public List<EarthworksUser> getBySpecimenMaxGrainSize(@RequestParam String specimenMaxGrainSize) {
        return earthworksUserRepository.findBySpecimenMaxGrainSizeContainingIgnoreCase(specimenMaxGrainSize);
    }

    @GetMapping("/specimenMaxGrainFraction")
    public List<EarthworksUser> getBySpecimenMaxGrainFraction(@RequestParam String specimenMaxGrainFraction) {
        return earthworksUserRepository.findBySpecimenMaxGrainFractionContainingIgnoreCase(specimenMaxGrainFraction);
    }

    @GetMapping("/schedulingNotes")
    public List<EarthworksUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return earthworksUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

    @GetMapping("/testDescription")
    public List<EarthworksUser> getByTestDescription(@RequestParam String testDescription) {
        return earthworksUserRepository.findByTestDescriptionContainingIgnoreCase(testDescription);
    }

    @GetMapping("/databaseBelongsTo")
    public List<EarthworksUser> getByDatabaseBelongsTo(@RequestParam String databaseBelongsTo) {
        return earthworksUserRepository.findByDatabaseBelongsToContainingIgnoreCase(databaseBelongsTo);
    }
}
