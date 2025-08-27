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
@RequestMapping("/inSituTest")
public class InSituUserController {

    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private InSituUserRepository InSituUserRepository;

    private void updateFields(InSituUser existing, InSituUser updated) {
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
        if (updated.getDatabaseBelongsTo() != null)
            existing.setDatabaseBelongsTo(updated.getDatabaseBelongsTo());
        if (updated.getTestDescription() != null)
            existing.setTestDescription(updated.getTestDescription());
        if (updated.getApplications() != null)
            existing.setApplications(updated.getApplications());
         if (updated.getMaterials() != null)
            existing.setMaterials(updated.getMaterials());

    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InSituUser> addInSituUser(
            @RequestPart("data") String InSituEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            InSituUser entry = objectMapper.readValue(InSituEntryJson, InSituUser.class);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir, fileName);
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                entry.setImagePath(imagePath.toString());
                imagePath.toFile().setReadable(true, false);
                imagePath.toFile().setWritable(true, false);
            }

            InSituUser saved = InSituUserRepository.save(entry);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InSituUser> updateInSituUserWithImage(
            @PathVariable Long id,
            @RequestPart("data") String updatedEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        Optional<InSituUser> existingOpt = InSituUserRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        try {
            InSituUser updated = objectMapper.readValue(updatedEntryJson, InSituUser.class);
            InSituUser existing = existingOpt.get();

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

            InSituUser saved = InSituUserRepository.save(existing);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<InSituUser> updateInSituUser(
            @PathVariable Long id,
            @RequestBody InSituUser updatedEntry) {
        Optional<InSituUser> existingOpt = InSituUserRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        InSituUser existing = existingOpt.get();
        updateFields(existing, updatedEntry);
        InSituUser saved = InSituUserRepository.save(existing);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInSituUser(@PathVariable Long id) {
        Optional<InSituUser> existingOpt = InSituUserRepository.findById(id);
        if (existingOpt.isPresent()) {
            InSituUserRepository.delete(existingOpt.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public List<InSituUser> getAllInSituUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) return InSituUserRepository.findAll();
        switch (sort) {
            case "default":
                return InSituUserRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "name":
                return InSituUserRepository.findAll(Sort.by(Sort.Direction.ASC, "myGroup"));
            case "method":
                return InSituUserRepository.findAll(Sort.by(Sort.Direction.ASC, "testMethod"));
            case "parameter":
                return InSituUserRepository.findAll(Sort.by(Sort.Direction.ASC, "parameters"));
            default:
                return InSituUserRepository.findAll();
        }
    }

    @GetMapping("/groups")
    public List<String> getAllGroups() {
        return InSituUserRepository.findAllGroups();
    }

    @GetMapping("/id")
    public List<InSituUser> getById(@RequestParam String id) {
        try {
            Long longId = Long.parseLong(id);
            return InSituUserRepository.findById(longId).map(List::of).orElse(List.of());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    // Fuzzy search endpoints
    @GetMapping("/group") public List<InSituUser> getByGroup(@RequestParam String group) { return InSituUserRepository.findByMyGroupContaining(group); }
    @GetMapping("/test") public List<InSituUser> getByTest(@RequestParam String test) { return InSituUserRepository.findByTestContaining(test); }
    @GetMapping("/testAlsoKnownAs") public List<InSituUser> getByTestAlsoKnownAs(@RequestParam String testAlsoKnownAs) { return InSituUserRepository.findByTestAlsoKnownAsContaining(testAlsoKnownAs); }
    @GetMapping("/symbol") public List<InSituUser> getBySymbol(@RequestParam String symbol) { return InSituUserRepository.findBySymbolContaining(symbol); }
    @GetMapping("/parameters") public List<InSituUser> getByParameters(@RequestParam String parameters) { return InSituUserRepository.findByParametersContaining(parameters); }
    @GetMapping("/testMethod") public List<InSituUser> getByTestMethod(@RequestParam String testMethod) { return InSituUserRepository.findByTestMethodContaining(testMethod); }
    @GetMapping("/alt1") public List<InSituUser> getByAlt1(@RequestParam String alt1) { return InSituUserRepository.findByAlt1Containing(alt1); }
    @GetMapping("/alt2") public List<InSituUser> getByAlt2(@RequestParam String alt2) { return InSituUserRepository.findByAlt2Containing(alt2); }
    @GetMapping("/alt3") public List<InSituUser> getByAlt3(@RequestParam String alt3) { return InSituUserRepository.findByAlt3Containing(alt3); }
    @GetMapping("/databaseBelongsTo") public List<InSituUser> getByDatabaseBelongsTo(@RequestParam String databaseBelongsTo) { return InSituUserRepository.findByDatabaseBelongsToContaining(databaseBelongsTo); }
    @GetMapping("/imagePath") public List<InSituUser> getByImagePath(@RequestParam String imagePath) { return InSituUserRepository.findByImagePathContaining(imagePath); }
    @GetMapping("/testDescription") public List<InSituUser> getByTestDescription(@RequestParam String testDescription) { return InSituUserRepository.findByTestDescriptionContaining(testDescription); }
    @GetMapping("/materials") public List<InSituUser> getByMaterials(@RequestParam String materials) { return InSituUserRepository.findByMaterialsContaining(materials); }
    @GetMapping("/applications") public List<InSituUser> getByApplications(@RequestParam String applications) { return InSituUserRepository.findByMaterialsContaining(applications); }
}
