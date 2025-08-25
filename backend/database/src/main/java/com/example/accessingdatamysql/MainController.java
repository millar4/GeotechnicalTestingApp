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
@RequestMapping("/database")
public class MainController {

    private final String uploadDir = "testImages/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private GeotechnicalEntryRepository geotechnicalEntryRepository;

    private void updateFields(GeotechnicalEntry existing, GeotechnicalEntry updated) {
        if (updated.getTest() != null && !updated.getTest().isEmpty())
            existing.setTest(updated.getTest());
        if (updated.getGroup() != null && !updated.getGroup().isEmpty())
            existing.setGroup(updated.getGroup());
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
        if (updated.getDatabaseBelongsTo() != null)
            existing.setDatabaseBelongsTo(updated.getDatabaseBelongsTo());
        if (updated.getTestDescription() != null)
            existing.setTestDescription(updated.getTestDescription());
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeotechnicalEntry> addGeotechnicalEntry(
            @RequestPart("data") String rocksEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            GeotechnicalEntry entry = objectMapper.readValue(rocksEntryJson, GeotechnicalEntry.class);

            if (image != null && !image.isEmpty()) {
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir, fileName);
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                entry.setImagePath(imagePath.toString());
                imagePath.toFile().setReadable(true, false);
                imagePath.toFile().setWritable(true, false);
            }

            GeotechnicalEntry saved = geotechnicalEntryRepository.save(entry);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeotechnicalEntry> updateGeotechnicalEntryWithImage(
            @PathVariable Long id,
            @RequestPart("data") String updatedEntryJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        Optional<GeotechnicalEntry> existingOpt = geotechnicalEntryRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        try {
            GeotechnicalEntry updated = objectMapper.readValue(updatedEntryJson, GeotechnicalEntry.class);
            GeotechnicalEntry existing = existingOpt.get();

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

            GeotechnicalEntry saved = geotechnicalEntryRepository.save(existing);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeotechnicalEntry> updateGeotechnicalEntry(
            @PathVariable Long id,
            @RequestBody GeotechnicalEntry updatedEntry) {
        Optional<GeotechnicalEntry> existingOpt = geotechnicalEntryRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        GeotechnicalEntry existing = existingOpt.get();
        updateFields(existing, updatedEntry);
        GeotechnicalEntry saved = geotechnicalEntryRepository.save(existing);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGeotechnicalEntry(@PathVariable Long id) {
        Optional<GeotechnicalEntry> existingOpt = geotechnicalEntryRepository.findById(id);
        if (existingOpt.isPresent()) {
            geotechnicalEntryRepository.delete(existingOpt.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public List<GeotechnicalEntry> getAllGeotechnicalEntrys(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) return geotechnicalEntryRepository.findAll();
        switch (sort) {
            case "default":
                return geotechnicalEntryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "name":
                return geotechnicalEntryRepository.findAll(Sort.by(Sort.Direction.ASC, "myGroup"));
            case "method":
                return geotechnicalEntryRepository.findAll(Sort.by(Sort.Direction.ASC, "testMethod"));
            case "parameter":
                return geotechnicalEntryRepository.findAll(Sort.by(Sort.Direction.ASC, "parameters"));
            default:
                return geotechnicalEntryRepository.findAll();
        }
    }

    @GetMapping("/id")
    public List<GeotechnicalEntry> getById(@RequestParam String id) {
        try {
            Long longId = Long.parseLong(id);
            return geotechnicalEntryRepository.findById(longId).map(List::of).orElse(List.of());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }
    @GetMapping("/groups")
        public List<String> getAllGroups() {
            return geotechnicalEntryRepository.findAllGroups();
        }

    // Fuzzy search endpoints
    @GetMapping("/group") public List<GeotechnicalEntry> getByGroup(@RequestParam("group") String myGroup) { return geotechnicalEntryRepository.findByMyGroupContaining(myGroup); }
    @GetMapping("/test") public List<GeotechnicalEntry> getByTest(@RequestParam String test) { return geotechnicalEntryRepository.findByTestContaining(test); }
    @GetMapping("/testAlsoKnownAs") public List<GeotechnicalEntry> getByTestAlsoKnownAs(@RequestParam String testAlsoKnownAs) { return geotechnicalEntryRepository.findByTestAlsoKnownAsContaining(testAlsoKnownAs); }
    @GetMapping("/symbol") public List<GeotechnicalEntry> getBySymbol(@RequestParam String symbol) { return geotechnicalEntryRepository.findBySymbolContaining(symbol); }
    @GetMapping("/classification") public List<GeotechnicalEntry> getByClassification(@RequestParam String classification) { return geotechnicalEntryRepository.findByClassificationContaining(classification); }
    @GetMapping("/parameters") public List<GeotechnicalEntry> getByParameters(@RequestParam String parameters) { return geotechnicalEntryRepository.findByParametersContaining(parameters); }
    @GetMapping("/testMethod") public List<GeotechnicalEntry> getByTestMethod(@RequestParam String testMethod) { return geotechnicalEntryRepository.findByTestMethodContaining(testMethod); }
    @GetMapping("/alt1") public List<GeotechnicalEntry> getByAlt1(@RequestParam String alt1) { return geotechnicalEntryRepository.findByAlt1Containing(alt1); }
    @GetMapping("/alt2") public List<GeotechnicalEntry> getByAlt2(@RequestParam String alt2) { return geotechnicalEntryRepository.findByAlt2Containing(alt2); }
    @GetMapping("/alt3") public List<GeotechnicalEntry> getByAlt3(@RequestParam String alt3) { return geotechnicalEntryRepository.findByAlt3Containing(alt3); }
    @GetMapping("/sampleType") public List<GeotechnicalEntry> getBySampleType(@RequestParam String sampleType) { return geotechnicalEntryRepository.findBySampleTypeContaining(sampleType); }
    @GetMapping("/fieldSampleMass") public List<GeotechnicalEntry> getByFieldSampleMass(@RequestParam String mass) { return geotechnicalEntryRepository.findByFieldSampleMassContaining(mass); }
    @GetMapping("/specimenType") public List<GeotechnicalEntry> getBySpecimenType(@RequestParam String specimenType) { return geotechnicalEntryRepository.findBySpecimenTypeContaining(specimenType); }
    @GetMapping("/specimenMass") public List<GeotechnicalEntry> getBySpecimenMass(@RequestParam String mass) { return geotechnicalEntryRepository.findBySpecimenMassContaining(mass); }
    @GetMapping("/specimenNumbers") public List<GeotechnicalEntry> getBySpecimenNumbers(@RequestParam String numbers) { return geotechnicalEntryRepository.findBySpecimenNumbersContaining(numbers); }
    @GetMapping("/specimenD") public List<GeotechnicalEntry> getBySpecimenD(@RequestParam String diameter) { return geotechnicalEntryRepository.findBySpecimenDContaining(diameter); }
    @GetMapping("/specimenL") public List<GeotechnicalEntry> getBySpecimenL(@RequestParam String length) { return geotechnicalEntryRepository.findBySpecimenLContaining(length); }
    @GetMapping("/specimenW") public List<GeotechnicalEntry> getBySpecimenW(@RequestParam String width) { return geotechnicalEntryRepository.findBySpecimenWContaining(width); }
    @GetMapping("/specimenH") public List<GeotechnicalEntry> getBySpecimenH(@RequestParam String height) { return geotechnicalEntryRepository.findBySpecimenHContaining(height); }
    @GetMapping("/specimenMaxGrainSize") public List<GeotechnicalEntry> getBySpecimenMaxGrainSize(@RequestParam String grainSize) { return geotechnicalEntryRepository.findBySpecimenMaxGrainSizeContaining(grainSize); }
    @GetMapping("/specimenMaxGrainFraction") public List<GeotechnicalEntry> getBySpecimenMaxGrainFraction(@RequestParam String fraction) { return geotechnicalEntryRepository.findBySpecimenMaxGrainFractionContaining(fraction); }
    @GetMapping("/databaseBelongsTo") public List<GeotechnicalEntry> getByDatabaseBelongsTo(@RequestParam String databaseBelongsTo) { return geotechnicalEntryRepository.findByDatabaseBelongsToContaining(databaseBelongsTo); }
    @GetMapping("/imagePath") public List<GeotechnicalEntry> getByImagePath(@RequestParam String imagePath) { return geotechnicalEntryRepository.findByImagePathContaining(imagePath); }
    @GetMapping("/testDescription") public List<GeotechnicalEntry> getByTestDescription(@RequestParam String testDescription) { return geotechnicalEntryRepository.findByTestDescriptionContaining(testDescription); }
}
