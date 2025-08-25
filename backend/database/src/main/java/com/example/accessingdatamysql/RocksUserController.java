package com.example.accessingdatamysql;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3100")
@RequestMapping("/rocks")
public class RocksUserController {

    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RocksUserRepository rocksUserRepository;

    private void updateFields(RocksUser existing, RocksUser updated) {
        if (updated.getTest() != null && !updated.getTest().isEmpty())
            existing.setTest(updated.getTest());
        if (updated.getmyGroup() != null && !updated.getmyGroup().isEmpty())
            existing.setmyGroup(updated.getmyGroup());
        if (updated.getSymbol() != null && !updated.getSymbol().isEmpty())
            existing.setSymbol(updated.getSymbol());
        if (updated.getParameters() != null)
            existing.setParameters(updated.getParameters());
        if (updated.getTestMethod() != null)
            existing.setTestMethod(updated.getTestMethod());
        if (updated.getAlt1() != null)
            existing.setAlt1(updated.getAlt1());
        if (updated.getAlt2() != null)
            existing.setAlt2(updated.getAlt2());
        if (updated.getAlt3() != null)
            existing.setAlt3(updated.getAlt3());
        if (updated.getSampleType() != null)
            existing.setSampleType(updated.getSampleType());
        if (updated.getFieldSampleMass() != null)
            existing.setFieldSampleMass(updated.getFieldSampleMass());
        if (updated.getSpecimenType() != null)
            existing.setSpecimenType(updated.getSpecimenType());
        if (updated.getSpecimenMass() != null)
            existing.setSpecimenMass(updated.getSpecimenMass());
        if (updated.getSpecimenNumbers() != null)
            existing.setSpecimenNumbers(updated.getSpecimenNumbers());
        if (updated.getSpecimenD() != null)
            existing.setSpecimenD(updated.getSpecimenD());
        if (updated.getSpecimenL() != null)
            existing.setSpecimenL(updated.getSpecimenL());
        if (updated.getSpecimenW() != null)
            existing.setSpecimenW(updated.getSpecimenW());
        if (updated.getSpecimenH() != null)
            existing.setSpecimenH(updated.getSpecimenH());
        if (updated.getSpecimenMaxGrainSize() != null)
            existing.setSpecimenMaxGrainSize(updated.getSpecimenMaxGrainSize());
        if (updated.getSpecimenMaxGrainFraction() != null)
            existing.setSpecimenMaxGrainFraction(updated.getSpecimenMaxGrainFraction());
        if (updated.getSchedulingNotes() != null)
            existing.setSchedulingNotes(updated.getSchedulingNotes());
        if (updated.getDatabaseBelongsTo() != null)
            existing.setDatabaseBelongsTo(updated.getDatabaseBelongsTo());
        if (updated.getTestDescription() != null)
            existing.setTestDescription(updated.getTestDescription());
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RocksUser> addRocksUser(
            @RequestPart("data") String rocksEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            RocksUser entry = objectMapper.readValue(rocksEntryJson, RocksUser.class);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir, fileName);
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                entry.setImagePath(imagePath.toString());
                imagePath.toFile().setReadable(true, false);
                imagePath.toFile().setWritable(true, false);
            }

            RocksUser saved = rocksUserRepository.save(entry);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RocksUser> updateRocksUserWithImage(
            @PathVariable Long id,
            @RequestPart("data") String updatedEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        Optional<RocksUser> existingOpt = rocksUserRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        try {
            RocksUser updated = objectMapper.readValue(updatedEntryJson, RocksUser.class);
            RocksUser existing = existingOpt.get();

            updateFields(existing, updated);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));
                if (existing.getImagePath() != null && !existing.getImagePath().isEmpty()) {
                    Path oldPath = Paths.get(existing.getImagePath()).normalize();
                    if (Files.exists(oldPath)) Files.delete(oldPath);
                }
                Path newImagePath = Paths.get(uploadDir, image.getOriginalFilename());
                Files.copy(image.getInputStream(), newImagePath, StandardCopyOption.REPLACE_EXISTING);
                existing.setImagePath(newImagePath.toString());
            }

            RocksUser saved = rocksUserRepository.save(existing);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RocksUser> updateRocksUser(
            @PathVariable Long id,
            @RequestBody RocksUser updatedEntry) {
        Optional<RocksUser> existingOpt = rocksUserRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        RocksUser existing = existingOpt.get();
        updateFields(existing, updatedEntry);
        RocksUser saved = rocksUserRepository.save(existing);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRocksUser(@PathVariable Long id) {
        Optional<RocksUser> existingOpt = rocksUserRepository.findById(id);
        if (existingOpt.isPresent()) {
            rocksUserRepository.delete(existingOpt.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public List<RocksUser> getAllRocksUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) return rocksUserRepository.findAll();
        switch (sort) {
            case "default":
                return rocksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "name":
                return rocksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "myGroup"));
            case "method":
                return rocksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "testMethod"));
            case "parameter":
                return rocksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "parameters"));
            default:
                return rocksUserRepository.findAll();
        }
    }

    @GetMapping("/groups")
    public List<String> getAllGroups() {
        return rocksUserRepository.findAllGroups();
    }

    @GetMapping("/id")
    public List<RocksUser> getById(@RequestParam String id) {
        try {
            Long longId = Long.parseLong(id);
            return rocksUserRepository.findById(longId).map(List::of).orElse(List.of());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    // Fuzzy search endpoints
    @GetMapping("/group") public List<RocksUser> getByGroup(@RequestParam String group) { return rocksUserRepository.findByMyGroupContaining(group); }
    @GetMapping("/test") public List<RocksUser> getByTest(@RequestParam String test) { return rocksUserRepository.findByTestContaining(test); }
    @GetMapping("/testAlsoKnownAs") public List<RocksUser> getByTestAlsoKnownAs(@RequestParam String testAlsoKnownAs) { return rocksUserRepository.findByTestAlsoKnownAsContaining(testAlsoKnownAs); }
    @GetMapping("/symbol") public List<RocksUser> getBySymbol(@RequestParam String symbol) { return rocksUserRepository.findBySymbolContaining(symbol); }
    @GetMapping("/classification") public List<RocksUser> getByClassification(@RequestParam String classification) { return rocksUserRepository.findByClassificationContaining(classification); }
    @GetMapping("/parameters") public List<RocksUser> getByParameters(@RequestParam String parameters) { return rocksUserRepository.findByParametersContaining(parameters); }
    @GetMapping("/testMethod") public List<RocksUser> getByTestMethod(@RequestParam String testMethod) { return rocksUserRepository.findByTestMethodContaining(testMethod); }
    @GetMapping("/alt1") public List<RocksUser> getByAlt1(@RequestParam String alt1) { return rocksUserRepository.findByAlt1Containing(alt1); }
    @GetMapping("/alt2") public List<RocksUser> getByAlt2(@RequestParam String alt2) { return rocksUserRepository.findByAlt2Containing(alt2); }
    @GetMapping("/alt3") public List<RocksUser> getByAlt3(@RequestParam String alt3) { return rocksUserRepository.findByAlt3Containing(alt3); }
    @GetMapping("/sampleType") public List<RocksUser> getBySampleType(@RequestParam String sampleType) { return rocksUserRepository.findBySampleTypeContaining(sampleType); }
    @GetMapping("/fieldSampleMass") public List<RocksUser> getByFieldSampleMass(@RequestParam String mass) { return rocksUserRepository.findByFieldSampleMassContaining(mass); }
    @GetMapping("/specimenType") public List<RocksUser> getBySpecimenType(@RequestParam String specimenType) { return rocksUserRepository.findBySpecimenTypeContaining(specimenType); }
    @GetMapping("/specimenMass") public List<RocksUser> getBySpecimenMass(@RequestParam String mass) { return rocksUserRepository.findBySpecimenMassContaining(mass); }
    @GetMapping("/specimenNumbers") public List<RocksUser> getBySpecimenNumbers(@RequestParam String numbers) { return rocksUserRepository.findBySpecimenNumbersContaining(numbers); }
    @GetMapping("/specimenD") public List<RocksUser> getBySpecimenD(@RequestParam String diameter) { return rocksUserRepository.findBySpecimenDContaining(diameter); }
    @GetMapping("/specimenL") public List<RocksUser> getBySpecimenL(@RequestParam String length) { return rocksUserRepository.findBySpecimenLContaining(length); }
    @GetMapping("/specimenW") public List<RocksUser> getBySpecimenW(@RequestParam String width) { return rocksUserRepository.findBySpecimenWContaining(width); }
    @GetMapping("/specimenH") public List<RocksUser> getBySpecimenH(@RequestParam String height) { return rocksUserRepository.findBySpecimenHContaining(height); }
    @GetMapping("/specimenMaxGrainSize") public List<RocksUser> getBySpecimenMaxGrainSize(@RequestParam String grainSize) { return rocksUserRepository.findBySpecimenMaxGrainSizeContaining(grainSize); }
    @GetMapping("/specimenMaxGrainFraction") public List<RocksUser> getBySpecimenMaxGrainFraction(@RequestParam String fraction) { return rocksUserRepository.findBySpecimenMaxGrainFractionContaining(fraction); }
    @GetMapping("/schedulingNotes") public List<RocksUser> getBySchedulingNotes(@RequestParam String schedulingNotes) { return rocksUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes); }
    @GetMapping("/databaseBelongsTo") public List<RocksUser> getByDatabaseBelongsTo(@RequestParam String databaseBelongsTo) { return rocksUserRepository.findByDatabaseBelongsToContaining(databaseBelongsTo); }
    @GetMapping("/imagePath") public List<RocksUser> getByImagePath(@RequestParam String imagePath) { return rocksUserRepository.findByImagePathContaining(imagePath); }
    @GetMapping("/testDescription") public List<RocksUser> getByTestDescription(@RequestParam String testDescription) { return rocksUserRepository.findByTestDescriptionContaining(testDescription); }
}
