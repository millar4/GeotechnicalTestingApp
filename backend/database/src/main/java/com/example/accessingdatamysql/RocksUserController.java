package com.example.accessingdatamysql;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:3100")
@Controller
@RequestMapping(path = "/rocks")
public class RocksUserController {

    private final String uploadDir = "public/testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RocksUserRepository RocksUserRepository;

    private void updateFields(RocksUser existing, RocksUser updatedEntry) {
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
        existing.setTestDescription(updatedEntry.getTestDescription());
    }

    @GetMapping(path = "/all/table")
    public String getAllUsersByTable(Model model) {
        Iterable<RocksUser> users = RocksUserRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(path = "/all")
    public @ResponseBody List<RocksUser> getAllUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return (List<RocksUser>) RocksUserRepository.findAll();
        }

        switch (sort) {
            case "default":
                return RocksUserRepository.findAllByOrderByIdAsc();
            case "name":
                return RocksUserRepository.findAllByOrderByMyGroupAsc();
            case "method":
                return RocksUserRepository.findAllByOrderByTestMethodAsc();
            case "parameter":
                return RocksUserRepository.findAllByOrderByParametersAsc();
            default:
                return (List<RocksUser>) RocksUserRepository.findAll();
        }
    }

    @GetMapping("/id")
    @ResponseBody
    public List<RocksUser> getUserById(@RequestParam String id) {
        try {
            Long longId = Long.valueOf(id);
            return RocksUserRepository.findById(longId)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }

    @PostMapping(path = "/add")
    @ResponseBody
    public ResponseEntity<RocksUser> addUser(@RequestBody RocksUser newEntry) {
        try {
            RocksUser savedEntry = RocksUserRepository.save(newEntry);
            return ResponseEntity.ok(savedEntry);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<RocksUser> entryOpt = RocksUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            RocksUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<RocksUser> updateRocksUserWithImage(
            @PathVariable Long id,
            @RequestPart("data") String updatedEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Optional<RocksUser> existingOpt = RocksUserRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            RocksUser updatedEntry = objectMapper.readValue(updatedEntryJson, RocksUser.class);
            RocksUser existing = existingOpt.get();
            updateFields(existing, updatedEntry);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = image.getOriginalFilename();
                Path newImagePath = Paths.get(uploadDir, fileName);

                String oldImagePath = existing.getImagePath();
                if (oldImagePath != null && !oldImagePath.isEmpty()) {
                    try {
                        Path oldPath = Paths.get(oldImagePath).normalize();
                        if (Files.exists(oldPath)) {
                            Files.delete(oldPath);
                        }
                    } catch (IOException ex) {
                        System.err.println("Failed to delete old image: " + ex.getMessage());
                    }
                }

                Files.copy(image.getInputStream(), newImagePath, StandardCopyOption.REPLACE_EXISTING);
                existing.setImagePath(newImagePath.toString());
            }

            RocksUser saved = RocksUserRepository.save(existing);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<RocksUser> updateRocksUser(@PathVariable Long id, @RequestBody RocksUser updatedEntry) {
        Optional<RocksUser> existingOpt = RocksUserRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RocksUser existing = existingOpt.get();
        updateFields(existing, updatedEntry);
        RocksUser saved = RocksUserRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // Various search filters
    @GetMapping(path = "/group") public List<RocksUser> getUsersByGroup(@RequestParam String group) {
        return RocksUserRepository.findByMyGroupContaining(group);
    }

    @GetMapping(path = "/groups") public List<String> getAllGroups() {
        return RocksUserRepository.findAllGroups();
    }

    @GetMapping(path = "/test") public List<RocksUser> getUsersByTest(@RequestParam String test) {
        return RocksUserRepository.findByTestContaining(test);
    }

    @GetMapping(path = "/testAlsoKnownAs") public List<RocksUser> getUsersByTestAlsoKnownAs(@RequestParam String testAlsoKnownAs) {
        return RocksUserRepository.findByTestAlsoKnownAsContaining(testAlsoKnownAs);
    }

    @GetMapping(path = "/symbol") public List<RocksUser> getUsersBySymbol(@RequestParam String symbol) {
        return RocksUserRepository.findBySymbolContaining(symbol);
    }

    @GetMapping(path = "/classification") public List<RocksUser> getUsersByClassification(@RequestParam String classification) {
        return RocksUserRepository.findByClassificationContaining(classification);
    }

    @GetMapping(path = "/parameters") public List<RocksUser> getUsersByParameters(@RequestParam String parameters) {
        return RocksUserRepository.findByParametersContaining(parameters);
    }

    @GetMapping(path = "/testMethod") public List<RocksUser> getRocksUsersByTestMethod(@RequestParam String testMethod) {
        return RocksUserRepository.findByTestMethodContaining(testMethod);
    }

    @GetMapping(path = "/alt1") public List<RocksUser> getUsersByAlt1(@RequestParam String alt1) {
        return RocksUserRepository.findByAlt1Containing(alt1);
    }

    @GetMapping(path = "/alt2") public List<RocksUser> getUsersByAlt2(@RequestParam String alt2) {
        return RocksUserRepository.findByAlt2Containing(alt2);
    }

    @GetMapping(path = "/alt3") public List<RocksUser> getUsersByAlt3(@RequestParam String alt3) {
        return RocksUserRepository.findByAlt3Containing(alt3);
    }

    @GetMapping(path = "/sampleType") public List<RocksUser> getUsersBySampleType(@RequestParam String sampleType) {
        return RocksUserRepository.findBySampleTypeContaining(sampleType);
    }

    @GetMapping(path = "/fieldSampleMassGreaterThan") public List<RocksUser> getUsersByFieldSampleMass(@RequestParam String mass) {
        return RocksUserRepository.findByFieldSampleMassContaining(mass);
    }

    @GetMapping(path = "/specimenType") public List<RocksUser> getUsersBySpecimenType(@RequestParam String specimenType) {
        return RocksUserRepository.findBySpecimenTypeContaining(specimenType);
    }

    @GetMapping(path = "/specimenMassGreaterThan") public List<RocksUser> getUsersBySpecimenMass(@RequestParam String mass) {
        return RocksUserRepository.findBySpecimenMassContaining(mass);
    }

    @GetMapping(path = "/specimenNumbers") public List<RocksUser> getUsersBySpecimenNumbers(@RequestParam String numbers) {
        return RocksUserRepository.findBySpecimenNumbersContaining(numbers);
    }

    @GetMapping(path = "/specimenD") public List<RocksUser> getUsersBySpecimenD(@RequestParam String diameter) {
        return RocksUserRepository.findBySpecimenDContaining(diameter);
    }

    @GetMapping(path = "/specimenL") public List<RocksUser> getUsersBySpecimenL(@RequestParam String length) {
        return RocksUserRepository.findBySpecimenLContaining(length);
    }

    @GetMapping(path = "/specimenW") public List<RocksUser> getUsersBySpecimenW(@RequestParam String width) {
        return RocksUserRepository.findBySpecimenWContaining(width);
    }

    @GetMapping(path = "/specimenH") public List<RocksUser> getUsersBySpecimenH(@RequestParam String height) {
        return RocksUserRepository.findBySpecimenHContaining(height);
    }

    @GetMapping(path = "/specimenMaxGrainSize") public List<RocksUser> getUsersBySpecimenMaxGrainSize(@RequestParam String grainSize) {
        return RocksUserRepository.findBySpecimenMaxGrainSizeContaining(grainSize);
    }

    @GetMapping(path = "/specimenMaxGrainFraction") public List<RocksUser> getUsersBySpecimenMaxGrainFraction(@RequestParam String fraction) {
        return RocksUserRepository.findBySpecimenMaxGrainFractionContaining(fraction);
    }

    @GetMapping("/schedulingNotes") public List<RocksUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return RocksUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

    @GetMapping("/databaseBelongsTo") public List<RocksUser> getBydatabaseBelongsTo(@RequestParam String databaseBelongsTo) {
        return RocksUserRepository.findByDatabaseBelongsToContaining(databaseBelongsTo);
    }

    @GetMapping("/imagePath") public List<RocksUser> getByImagePath(@RequestParam String imagePath) {
        return RocksUserRepository.findByImagePathContaining(imagePath);
    }

    @GetMapping("/TestDescription") public List<RocksUser> getByTestDescription(@RequestParam String testDescription) {
        return RocksUserRepository.findByTestDescriptionContaining(testDescription);
    }
}
