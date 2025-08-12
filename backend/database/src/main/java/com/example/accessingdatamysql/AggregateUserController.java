package com.example.accessingdatamysql;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3100")
@RequestMapping("/aggregate")
public class AggregateUserController {

    @Autowired
    private AggregateUserRepository AggregateUserRepository;

    // Add new record
    @PostMapping("/add")
    public AggregateUser addAggregateUser(@RequestBody AggregateUser AggregateUser) {
        return AggregateUserRepository.save(AggregateUser);
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
