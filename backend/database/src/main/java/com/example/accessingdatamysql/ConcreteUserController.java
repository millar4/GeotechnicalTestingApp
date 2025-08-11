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
@RequestMapping(path = "/concrete")
public class ConcreteUserController{

    private void updateFields(ConcreteUser existing, ConcreteUser updatedEntry) {
        existing.setTest(updatedEntry.getTest());
        existing.setmyGroup(updatedEntry.getmyGroup());
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
    }

    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ConcreteUserRepository ConcreteUserRepository;

    @GetMapping(path = "/all/table")
    public String getAllUsersByTable(Model model) {
        Iterable<ConcreteUser> users = ConcreteUserRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(path = "/all")
    public @ResponseBody List<ConcreteUser> getAllUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return (List<ConcreteUser>) ConcreteUserRepository.findAll();
        }


        switch (sort) {
            case "default":
                return ConcreteUserRepository.findAllByOrderByIdAsc();
            case "name":
                return ConcreteUserRepository.findAllByOrderByMyGroupAsc();
            case "method":
                return ConcreteUserRepository.findAllByOrderByTestMethodAsc();
            case "parameter":
                return ConcreteUserRepository.findAllByOrderByParametersAsc();
            default:
                return (List<ConcreteUser>) ConcreteUserRepository.findAll();
        }
    }

    @GetMapping("/id")
    @ResponseBody
    public List<ConcreteUser> getUserById(@RequestParam String id) {
        Long longId;
        try {
            longId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }

        Optional<ConcreteUser> userOpt = ConcreteUserRepository.findById(longId);
        if (userOpt.isPresent()) {
            return Collections.singletonList(userOpt.get());
        } else {
            return Collections.emptyList();
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<ConcreteUser> addConcreteUserWithImage(
        @RequestPart("data") String aggregateEntryJson,
        @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            ConcreteUser entry = objectMapper.readValue(aggregateEntryJson, ConcreteUser.class);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir, fileName);
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                entry.setImagePath(Paths.get(uploadDir).resolve(fileName).toString());
                imagePath.toFile().setReadable(true, false);
                imagePath.toFile().setWritable(true, false);
            }

            ConcreteUser savedEntry = ConcreteUserRepository.save(entry);
            return ResponseEntity.ok(savedEntry);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<ConcreteUser> updateConcreteUserWithImage(
            @PathVariable Long id,
            @RequestPart("data") String updatedEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        System.out.println("=== [DEBUG] Received update-with-image request for ID: " + id + " ===");

        Optional<ConcreteUser> existingOpt = ConcreteUserRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            ConcreteUser updatedEntry = objectMapper.readValue(updatedEntryJson, ConcreteUser.class);
            ConcreteUser existing = existingOpt.get();
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

            ConcreteUser saved =ConcreteUserRepository.save(existing);
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
        Optional<ConcreteUser> entryOpt = ConcreteUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            ConcreteUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ConcreteUser> updateConcreteUser(
            @PathVariable Long id,
            @RequestBody ConcreteUser updatedEntry) {
        // 1. Find an existing record in the database based on the primary key id.
        Optional<ConcreteUser> existingOpt = ConcreteUserRepository.findById(id);
        if (existingOpt.isEmpty()) {
           // Returns 404 if the corresponding record is not found.
            return ResponseEntity.notFound().build();
        }
        // 2. Synchronize the updated fields passed by the front-end into the database.
        ConcreteUser existing = existingOpt.get();
        existing.setTest(updatedEntry.getTest());
        existing.setmyGroup(updatedEntry.getmyGroup());
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
        existing.setSchedulingNotes(updatedEntry.getSchedulingNotes());
        existing.setDatabaseBelongsTo(updatedEntry.getDatabaseBelongsTo());
        // ... More fields can be assigned here as well.

        // 3. Preservation of updated entities
        ConcreteUser saved = ConcreteUserRepository.save(existing);

    // 4. Return the updated entity to the front end
        return ResponseEntity.ok(saved);
    }

    @GetMapping(path = "/group")
    @ResponseBody
    public List<ConcreteUser> getUsersByGroup(@RequestParam String group) {
        return ConcreteUserRepository.findByMyGroupContaining(group);
    }

    @GetMapping(path = "/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return ConcreteUserRepository.findAllGroups();
    }

    @GetMapping(path = "/test")
    @ResponseBody
    public List<ConcreteUser> getUsersByTest(@RequestParam String test) {
        return ConcreteUserRepository.findByTestContaining(test);
    }

    @GetMapping(path = "/testAlsoKnownAs")
    @ResponseBody
    public List<ConcreteUser> getUsersByTestAlsoKnownAs(@RequestParam String testAlsoKnownAs) {
        return ConcreteUserRepository.findByTestContaining(testAlsoKnownAs);
    }

    @GetMapping(path = "/symbol")
    @ResponseBody
    public List<ConcreteUser> getUsersBySymbol(@RequestParam String symbol) {
        return ConcreteUserRepository.findBySymbolContaining(symbol);
    }

    @GetMapping(path = "/parameters")
    @ResponseBody
    public List<ConcreteUser> getUsersByParameters(@RequestParam String parameters) {
        return ConcreteUserRepository.findByParametersContaining(parameters);
    }

    @GetMapping(path = "/testMethod")
    @ResponseBody
    public List<ConcreteUser> getConcreteUsersByTestMethod(@RequestParam String testMethod) {
        return ConcreteUserRepository.findByTestMethodContaining(testMethod);
    }

    @GetMapping(path = "/alt1")
    @ResponseBody
    public List<ConcreteUser> getUsersByAlt1(@RequestParam String alt1) {
        return ConcreteUserRepository.findByAlt1Containing(alt1);
    }

    @GetMapping(path = "/alt2")
    @ResponseBody
    public List<ConcreteUser> getUsersByAlt2(@RequestParam String alt2) {
        return ConcreteUserRepository.findByAlt2Containing(alt2);
    }

    @GetMapping(path = "/alt3")
    @ResponseBody
    public List<ConcreteUser> getUsersByAlt3(@RequestParam String alt3) {
        return ConcreteUserRepository.findByAlt3Containing(alt3);
    }

    @GetMapping(path = "/sampleType")
    @ResponseBody
    public List<ConcreteUser> getUsersBySampleType(@RequestParam String sampleType) {
        return ConcreteUserRepository.findBySampleTypeContaining(sampleType);
    }

    @GetMapping(path = "/fieldSampleMassGreaterThan")
    @ResponseBody
    public List<ConcreteUser> getUsersByFieldSampleMass(@RequestParam String mass) {
        return ConcreteUserRepository.findByFieldSampleMassContaining(mass);
    }

    @GetMapping(path = "/specimenType")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenType(@RequestParam String specimenType) {
        return ConcreteUserRepository.findBySpecimenTypeContaining(specimenType);
    }

    @GetMapping(path = "/specimenMassGreaterThan")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenMass(@RequestParam String mass) {
        return ConcreteUserRepository.findBySpecimenMassContaining(mass);
    }

    @GetMapping(path = "/specimenNumbers")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenNumbers(@RequestParam String numbers) {
        return ConcreteUserRepository.findBySpecimenNumbersContaining(numbers);
    }

    @GetMapping(path = "/specimenD")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenD(@RequestParam String diameter) {
        return ConcreteUserRepository.findBySpecimenDContaining(diameter);
    }

    @GetMapping(path = "/specimenL")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenL(@RequestParam String length) {
        return ConcreteUserRepository.findBySpecimenLContaining(length);
    }

    @GetMapping(path = "/specimenW")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenW(@RequestParam String width) {
        return ConcreteUserRepository.findBySpecimenWContaining(width);
    }

    @GetMapping(path = "/specimenH")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenH(@RequestParam String height) {
        return ConcreteUserRepository.findBySpecimenHContaining(height);
    }

    @GetMapping(path = "/specimenMaxGrainSize")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenMaxGrainSize(@RequestParam String grainSize) {
        return ConcreteUserRepository.findBySpecimenMaxGrainSizeContaining(grainSize);
    }

    @GetMapping(path = "/specimenMaxGrainFraction")
    @ResponseBody
    public List<ConcreteUser> getUsersBySpecimenMaxGrainFraction(@RequestParam String fraction) {
        return ConcreteUserRepository.findBySpecimenMaxGrainFractionContaining(fraction);
    }

    @GetMapping("/schedulingNotes")
    public List<ConcreteUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return ConcreteUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

    @GetMapping("/imagePath")
    public List<ConcreteUser> getByImagePath(@RequestParam String imagePath) {
        return ConcreteUserRepository.findByImagePathContainingIgnoreCase(imagePath);
    }

    @GetMapping("testDescription/")
    public List<ConcreteUser> getByTestDescriptionContaining(@RequestParam String imagePath) {
        return ConcreteUserRepository.findByTestDescriptionContainingIgnoreCase(imagePath);
    }

}
