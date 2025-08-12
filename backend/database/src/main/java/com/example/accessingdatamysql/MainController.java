package com.example.accessingdatamysql;

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

@CrossOrigin(origins = "http://localhost:3100")
@RestController
@RequestMapping(path = "/database")
public class MainController {

    @Autowired
    private GeotechnicalEntryRepository userRepository;

    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(path = "/all/table")
    public String getAllUsersByTable(Model model) {
        Iterable<GeotechnicalEntry> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(path = "/all")
    public @ResponseBody List<GeotechnicalEntry> getAllUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return (List<GeotechnicalEntry>) userRepository.findAll();
        }

        switch (sort) {
            case "default":
                return userRepository.findAllByOrderByIdAsc();
            case "name":
                return userRepository.findAllByOrderByGroupAsc();
            case "method":
                return userRepository.findAllByOrderByTestMethodAsc();
            case "parameter":
                return userRepository.findAllByOrderByParametersAsc();
            default:
                return (List<GeotechnicalEntry>) userRepository.findAll();
        }
    }

    @GetMapping("/id")
    @ResponseBody
    public List<GeotechnicalEntry> getUserById(@RequestParam String id) {
        try {
            Long longId = Long.valueOf(id);
            return userRepository.findById(longId).map(Collections::singletonList).orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format: " + id);
            return Collections.emptyList();
        }
    }

    @GetMapping("/classification")
    @ResponseBody
    public List<GeotechnicalEntry> getUserByClassification(@RequestParam String classification) {
        return userRepository.findByClassificationContaining(classification);
    }

   @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<GeotechnicalEntry> addGeotechnicalEntryWithImage(
        @RequestPart("data") String geotechnicalEntryJson,
        @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            GeotechnicalEntry entry = objectMapper.readValue(geotechnicalEntryJson, GeotechnicalEntry.class);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir, fileName);
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                entry.setImagePath(Paths.get(uploadDir).resolve(fileName).toString());
                imagePath.toFile().setReadable(true, false);
                imagePath.toFile().setWritable(true, false);
            }

            GeotechnicalEntry savedEntry = userRepository.save(entry);
            return ResponseEntity.ok(savedEntry);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @DeleteMapping(path = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<GeotechnicalEntry> entryOpt = userRepository.findById(id);
        if (entryOpt.isPresent()) {
            userRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<GeotechnicalEntry> updateGeotechnicalEntry(
            @PathVariable Long id,
            @RequestBody GeotechnicalEntry updatedEntry) {

        Optional<GeotechnicalEntry> existingOpt = userRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        GeotechnicalEntry existing = existingOpt.get();
        updateFields(existing, updatedEntry);

        GeotechnicalEntry saved = userRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<GeotechnicalEntry> updateGeotechnicalEntryWithImage(
            @PathVariable Long id,
            @RequestPart("data") String updatedEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        System.out.println("=== [DEBUG] Received update-with-image request for ID: " + id + " ===");

        Optional<GeotechnicalEntry> existingOpt = userRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            GeotechnicalEntry updatedEntry = objectMapper.readValue(updatedEntryJson, GeotechnicalEntry.class);
            GeotechnicalEntry existing = existingOpt.get();
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

            GeotechnicalEntry saved = userRepository.save(existing);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update entry with image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    private void updateFields(GeotechnicalEntry existing, GeotechnicalEntry updatedEntry) {
        existing.setTest(updatedEntry.getTest());
        existing.setGroup(updatedEntry.getGroup());
        existing.setSymbol(updatedEntry.getSymbol());
        existing.setParameters(updatedEntry.getParameters());
        existing.setTestMethod(updatedEntry.getTestMethod());
        existing.setAlt1(updatedEntry.getAlt1());
        existing.setAlt2(updatedEntry.getAlt2());
        existing.setAlt3(updatedEntry.getAlt3());
        existing.setSampleType(updatedEntry.getSampleType());
        existing.setFieldSampleMass(updatedEntry.getFieldSampleMass());
        existing.setSpecimenType(updatedEntry.getSpecimenType());
        existing.setSpecimenMass(updatedEntry.getSpecimenMass());
        existing.setSpecimenNumbers(updatedEntry.getSpecimenNumbers());
        existing.setSpecimenD(updatedEntry.getSpecimenD());
        existing.setSpecimenL(updatedEntry.getSpecimenL());
        existing.setSpecimenW(updatedEntry.getSpecimenW());
        existing.setSpecimenH(updatedEntry.getSpecimenH());
        existing.setSpecimenMaxGrainSize(updatedEntry.getSpecimenMaxGrainSize());
        existing.setSpecimenMaxGrainFraction(updatedEntry.getSpecimenMaxGrainFraction());
        existing.setTestDescription(updatedEntry.getTestDescription());
    }

    // --- Remaining filters ---
    @GetMapping(path = "/group")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByGroup(@RequestParam String group) {
        return userRepository.findByGroupContaining(group);
    }

    @GetMapping(path = "/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return userRepository.findAllGroups();
    }

    @GetMapping(path = "/test")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByTest(@RequestParam String test) {
        return userRepository.findByTestContaining(test);
    }

    @GetMapping(path = "/testAlsoKnownAs")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByTestAlsoKnownAs(@RequestParam String testAlsoKnownAs) {
        return userRepository.findByTestAlsoKnownAsContaining(testAlsoKnownAs);
    }

    @GetMapping(path = "/symbol")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySymbol(@RequestParam String symbol) {
        return userRepository.findBySymbolContaining(symbol);
    }

    @GetMapping(path = "/parameters")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByParameters(@RequestParam String parameters) {
        return userRepository.findByParametersContaining(parameters);
    }

    @GetMapping(path = "/testMethod")
    @ResponseBody
    public List<GeotechnicalEntry> getGeotechnicalEntrysByTestMethod(@RequestParam String testMethod) {
        return userRepository.findByTestMethodContaining(testMethod);
    }

    @GetMapping(path = "/alt1")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByAlt1(@RequestParam String alt1) {
        return userRepository.findByAlt1Containing(alt1);
    }

    @GetMapping(path = "/alt2")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByAlt2(@RequestParam String alt2) {
        return userRepository.findByAlt2Containing(alt2);
    }

    @GetMapping(path = "/alt3")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByAlt3(@RequestParam String alt3) {
        return userRepository.findByAlt3Containing(alt3);
    }

    @GetMapping(path = "/sampleType")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySampleType(@RequestParam String sampleType) {
        return userRepository.findBySampleTypeContaining(sampleType);
    }

    @GetMapping(path = "/fieldSampleMassGreaterThan")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByFieldSampleMass(@RequestParam String mass) {
        return userRepository.findByFieldSampleMassContaining(mass);
    }

    @GetMapping(path = "/specimenType")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenType(@RequestParam String specimenType) {
        return userRepository.findBySpecimenTypeContaining(specimenType);
    }

    @GetMapping(path = "/specimenMassGreaterThan")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenMass(@RequestParam String mass) {
        return userRepository.findBySpecimenMassContaining(mass);
    }

    @GetMapping(path = "/specimenNumbers")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenNumbers(@RequestParam String numbers) {
        return userRepository.findBySpecimenNumbersContaining(numbers);
    }

    @GetMapping(path = "/specimenD")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenD(@RequestParam String diameter) {
        return userRepository.findBySpecimenDContaining(diameter);
    }

    @GetMapping(path = "/specimenL")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenL(@RequestParam String length) {
        return userRepository.findBySpecimenLContaining(length);
    }

    @GetMapping(path = "/specimenW")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenW(@RequestParam String width) {
        return userRepository.findBySpecimenWContaining(width);
    }

    @GetMapping(path = "/specimenH")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenH(@RequestParam String height) {
        return userRepository.findBySpecimenHContaining(height);
    }

    @GetMapping(path = "/specimenMaxGrainSize")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenMaxGrainSize(@RequestParam String grainSize) {
        return userRepository.findBySpecimenMaxGrainSizeContaining(grainSize);
    }

    @GetMapping(path = "/specimenMaxGrainFraction")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersBySpecimenMaxGrainFraction(@RequestParam String fraction) {
        return userRepository.findBySpecimenMaxGrainFractionContaining(fraction);
    }

    @GetMapping(path = "/databaseBelongsTo")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByDatabaseBelongsTo(@RequestParam String databaseBelongsTo) {
        return userRepository.findByDatabaseBelongsToContaining(databaseBelongsTo);
    }

    @GetMapping(path = "/imagePath")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByImagepath(@RequestParam String imagePath) {
        return userRepository.findByImagePathContaining(imagePath);
    }

    @GetMapping(path = "/testDescription")
    @ResponseBody
    public List<GeotechnicalEntry> getUsersByTestDescripton(@RequestParam String testDescription) {
        return userRepository.findByTestDescriptionContaining(testDescription);
    }
}
