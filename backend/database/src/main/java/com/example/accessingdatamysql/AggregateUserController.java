package com.example.accessingdatamysql;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:3100")
@Controller
@RequestMapping(path = "/aggregate")
public class AggregateUserController{

    private void updateFields(AggregateUser existing, AggregateUser updatedEntry) {
        existing.setTest(updatedEntry.getTest());
        existing.setSymbol(updatedEntry.getSymbol());
        existing.setParameters(updatedEntry.getParameters());
        existing.setTestMethod(updatedEntry.getTestMethod());
        existing.setSampleType(updatedEntry.getSampleType());
        existing.setFieldSampleMass(updatedEntry.getFieldSampleMass());
        existing.setSpecimenType(updatedEntry.getSpecimenType());
        existing.setSpecimenMass(updatedEntry.getSpecimenMass());
        existing.setSpecimenNumbers(updatedEntry.getSpecimenNumbers());
        existing.setSpecimenMaxGrainSize(updatedEntry.getSpecimenMaxGrainSize());
        existing.setSpecimenMaxGrainFraction(updatedEntry.getSpecimenMaxGrainFraction());
    }

    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AggregateUserRepository AggregateUserRepository;

    @GetMapping(path = "/all/table")
    public String getAllUsersByTable(Model model) {
        Iterable<AggregateUser> users = AggregateUserRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(path = "/all")
    public @ResponseBody List<AggregateUser> getAllUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return (List<AggregateUser>) AggregateUserRepository.findAll();
        }


        switch (sort) {
            case "default":
                return AggregateUserRepository.findAllByOrderByIdAsc();
            case "name":
                return AggregateUserRepository.findAllByOrderByGroupAsc();
            case "method":
                return AggregateUserRepository.findAllByOrderByTestMethodAsc();
            case "parameter":
                return AggregateUserRepository.findAllByOrderByParametersAsc();
            default:
                return (List<AggregateUser>) AggregateUserRepository.findAll();
        }
    }

    @GetMapping("/id")
    @ResponseBody
    public List<AggregateUser> getUserById(@RequestParam String id) {
        Long longId;
        try {
            longId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }

        Optional<AggregateUser> userOpt = AggregateUserRepository.findById(longId);
        if (userOpt.isPresent()) {
            return Collections.singletonList(userOpt.get());
        } else {
            return Collections.emptyList();
        }
    }

  @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<AggregateUser> addGeotechnicalEntryWithImage(
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

    @DeleteMapping(path = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<AggregateUser> entryOpt = AggregateUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            AggregateUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<AggregateUser> updateAggregateUser(
            @PathVariable Long id,
            @RequestBody AggregateUser updatedEntry) {
        // 1. Find an existing record in the database based on the primary key id.
        Optional<AggregateUser> existingOpt = AggregateUserRepository.findById(id);
        if (existingOpt.isEmpty()) {
           // Returns 404 if the corresponding record is not found.
            return ResponseEntity.notFound().build();
        }
        // 2. Synchronize the updated fields passed by the front-end into the database.
        AggregateUser existing = existingOpt.get();
        existing.setTest(updatedEntry.getTest());
        existing.setSymbol(updatedEntry.getSymbol());
        existing.setParameters(updatedEntry.getParameters());
        existing.setTestMethod(updatedEntry.getTestMethod());

        existing.setSampleType(updatedEntry.getSampleType());
        existing.setFieldSampleMass(updatedEntry.getFieldSampleMass());
        existing.setSpecimenType(updatedEntry.getSpecimenType());
        existing.setSpecimenMass(updatedEntry.getSpecimenMass());
        existing.setSpecimenNumbers(updatedEntry.getSpecimenNumbers());
        existing.setSpecimenMaxGrainSize(updatedEntry.getSpecimenMaxGrainSize());
        existing.setSpecimenMaxGrainFraction(updatedEntry.getSpecimenMaxGrainFraction());
        existing.setSchedulingNotes(updatedEntry.getSchedulingNotes());
        existing.setDatabaseBelongsTo(updatedEntry.getDatabaseBelongsTo());
        // ... More fields can be assigned here as well.

        // 3. Preservation of updated entities
        AggregateUser saved = AggregateUserRepository.save(existing);

    // 4. Return the updated entity to the front end
        return ResponseEntity.ok(saved);
    }

    @GetMapping(path = "/group")
    @ResponseBody
    public List<AggregateUser> getUsersByGroup(@RequestParam String group) {
        return AggregateUserRepository.findByGroupContainingIgnoreCase(group);
    }

    @GetMapping(path = "/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return AggregateUserRepository.findAllGroups();
    }

    @GetMapping(path = "/test")
    @ResponseBody
    public List<AggregateUser> getUsersByTest(@RequestParam String test) {
        return AggregateUserRepository.findByTestContainingIgnoreCase(test);
    }

    @GetMapping(path = "/testAlsoKnownAs")
    @ResponseBody
    public List<AggregateUser> getUsersByTestAlsoKnownAs(@RequestParam String testAlsoKnownAs) {
        return AggregateUserRepository.findByTestContainingIgnoreCase(testAlsoKnownAs);
    }

    @GetMapping(path = "/symbol")
    @ResponseBody
    public List<AggregateUser> getUsersBySymbol(@RequestParam String symbol) {
        return AggregateUserRepository.findBySymbolContainingIgnoreCase(symbol);
    }

    @GetMapping(path = "/parameters")
    @ResponseBody
    public List<AggregateUser> getUsersByParameters(@RequestParam String parameters) {
        return AggregateUserRepository.findByParametersContainingIgnoreCase(parameters);
    }

    @GetMapping(path = "/testMethod")
    @ResponseBody
    public List<AggregateUser> getAggregateUsersByTestMethod(@RequestParam String testMethod) {
        return AggregateUserRepository.findByTestMethodContainingIgnoreCase(testMethod);
    }

    @GetMapping(path = "/sampleType")
    @ResponseBody
    public List<AggregateUser> getUsersBySampleType(@RequestParam String sampleType) {
        return AggregateUserRepository.findBySampleTypeContainingIgnoreCase(sampleType);
    }

    @GetMapping(path = "/fieldSampleMassGreaterThan")
    @ResponseBody
    public List<AggregateUser> getUsersByFieldSampleMass(@RequestParam String mass) {
        return AggregateUserRepository.findByFieldSampleMassContainingIgnoreCase(mass);
    }

    @GetMapping(path = "/specimenType")
    @ResponseBody
    public List<AggregateUser> getUsersBySpecimenType(@RequestParam String specimenType) {
        return AggregateUserRepository.findBySpecimenTypeContainingIgnoreCase(specimenType);
    }

    @GetMapping(path = "/specimenMassGreaterThan")
    @ResponseBody
    public List<AggregateUser> getUsersBySpecimenMass(@RequestParam String mass) {
        return AggregateUserRepository.findBySpecimenMassContainingIgnoreCase(mass);
    }

    @GetMapping(path = "/specimenNumbers")
    @ResponseBody
    public List<AggregateUser> getUsersBySpecimenNumbers(@RequestParam String numbers) {
        return AggregateUserRepository.findBySpecimenNumbersContainingIgnoreCase(numbers);
    }


    @GetMapping(path = "/specimenMaxGrainSize")
    @ResponseBody
    public List<AggregateUser> getUsersBySpecimenMaxGrainSize(@RequestParam String grainSize) {
        return AggregateUserRepository.findBySpecimenMaxGrainSizeContainingIgnoreCase(grainSize);
    }

    @GetMapping(path = "/specimenMaxGrainFraction")
    @ResponseBody
    public List<AggregateUser> getUsersBySpecimenMaxGrainFraction(@RequestParam String fraction) {
        return AggregateUserRepository.findBySpecimenMaxGrainFractionContainingIgnoreCase(fraction);
    }

    @GetMapping("/schedulingNotes")
    public List<AggregateUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return AggregateUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

    @GetMapping("/imagePath")
    public List<AggregateUser> getByImagePath(@RequestParam String imagePath) {
        return AggregateUserRepository.findByImagePathContainingIgnoreCase(imagePath);
    }

    @GetMapping("testDescription/")
    public List<AggregateUser> getByTestDescriptionContaining(@RequestParam String imagePath) {
        return AggregateUserRepository.findByTestDescriptionContainingIgnoreCase(imagePath);
    }

}
