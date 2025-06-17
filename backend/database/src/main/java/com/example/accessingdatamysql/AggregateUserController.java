package com.example.accessingdatamysql;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3100")
@RequestMapping("/rocks")
public class AggregateUserController {

    @Autowired
    private AggregateUserRepository AggregateUserRepository;

    // Add new record
    @PostMapping("/add")
    public AggregateUser addRocksUser(@RequestBody AggregateUser AggregateUser) {
        return AggregateUserRepository.save(AggregateUser);
    }

    // Update existing record
    @PutMapping("/update/{id}")
    public AggregateUser updateRocksUser(@PathVariable Long id, @RequestBody AggregateUser updatedUser) {
        Optional<AggregateUser> optionalUser = AggregateUserRepository.findById(id);
        if (optionalUser.isPresent()) {
            AggregateUser existingUser = optionalUser.get();

            existingUser.setGroup(updatedUser.getGroup());
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

            return AggregateUserRepository.save(existingUser);
        } else {
            throw new RuntimeException("RocksUser not found with id " + id);
        }
    }

    // Delete by ID
    @DeleteMapping("/delete/{id}")
    public void deleteRocksUser(@PathVariable Long id) {
        AggregateUserRepository.deleteById(id);
    }

    // Get all records, optional sort
    @GetMapping("/all")
    public List<AggregateUser> getAllRocksUsers(@RequestParam(required = false) String sort) {
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

    // Get all distinct groups
    @GetMapping("/groups")
    public List<String> getAllGroups() {
        return AggregateUserRepository.findAllGroups();
    }
}
