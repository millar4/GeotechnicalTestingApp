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
public class RocksUserController {

    @Autowired
    private RocksUserRepository rocksUserRepository;

    // Add new record
    @PostMapping("/add")
    public RocksUser addRocksUser(@RequestBody RocksUser rocksUser) {
        return rocksUserRepository.save(rocksUser);
    }

    // Update existing record
    @PutMapping("/update/{id}")
    public RocksUser updateRocksUser(@PathVariable Long id, @RequestBody RocksUser updatedUser) {
        Optional<RocksUser> optionalUser = rocksUserRepository.findById(id);
        if (optionalUser.isPresent()) {
            RocksUser existingUser = optionalUser.get();

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

            return rocksUserRepository.save(existingUser);
        } else {
            throw new RuntimeException("RocksUser not found with id " + id);
        }
    }

    // Delete by ID
    @DeleteMapping("/delete/{id}")
    public void deleteRocksUser(@PathVariable Long id) {
        rocksUserRepository.deleteById(id);
    }

    // Get all records, optional sort
    @GetMapping("/all")
    public List<RocksUser> getAllRocksUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return rocksUserRepository.findAll();
        }
        switch (sort) {
            case "default":
                return rocksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "name":
                return rocksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "group"));
            case "method":
                return rocksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "testMethod"));
            case "parameter":
                return rocksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "parameters"));
            default:
                return rocksUserRepository.findAll();
        }
    }

    // Precise search by ID
    @GetMapping("/id")
    public List<RocksUser> getById(@RequestParam String id) {
        try {
            Long longId = Long.parseLong(id);
            Optional<RocksUser> opt = rocksUserRepository.findById(longId);
            return opt.map(List::of).orElse(List.of());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    // Fuzzy search endpoints
    @GetMapping("/group")
    public List<RocksUser> getByGroup(@RequestParam String group) {
        return rocksUserRepository.findByGroupContainingIgnoreCase(group);
    }

    @GetMapping("/symbol")
    public List<RocksUser> getBySymbol(@RequestParam String symbol) {
        return rocksUserRepository.findBySymbolContainingIgnoreCase(symbol);
    }

    @GetMapping("/parameters")
    public List<RocksUser> getByParameters(@RequestParam String parameters) {
        return rocksUserRepository.findByParametersContainingIgnoreCase(parameters);
    }

    @GetMapping("/testMethod")
    public List<RocksUser> getByTestMethod(@RequestParam String testMethod) {
        return rocksUserRepository.findByTestMethodContainingIgnoreCase(testMethod);
    }

    @GetMapping("/sampleType")
    public List<RocksUser> getBySampleType(@RequestParam String sampleType) {
        return rocksUserRepository.findBySampleTypeContainingIgnoreCase(sampleType);
    }

    @GetMapping("/fieldSampleMass")
    public List<RocksUser> getByFieldSampleMass(@RequestParam String fieldSampleMass) {
        return rocksUserRepository.findByFieldSampleMassContainingIgnoreCase(fieldSampleMass);
    }

    @GetMapping("/specimenType")
    public List<RocksUser> getBySpecimenType(@RequestParam String specimenType) {
        return rocksUserRepository.findBySpecimenTypeContainingIgnoreCase(specimenType);
    }

    @GetMapping("/specimenMass")
    public List<RocksUser> getBySpecimenMass(@RequestParam String specimenMass) {
        return rocksUserRepository.findBySpecimenMassContainingIgnoreCase(specimenMass);
    }

    @GetMapping("/specimenNumbers")
    public List<RocksUser> getBySpecimenNumbers(@RequestParam String specimenNumbers) {
        return rocksUserRepository.findBySpecimenNumbersContainingIgnoreCase(specimenNumbers);
    }

    @GetMapping("/specimenMaxGrainSize")
    public List<RocksUser> getBySpecimenMaxGrainSize(@RequestParam String specimenMaxGrainSize) {
        return rocksUserRepository.findBySpecimenMaxGrainSizeContainingIgnoreCase(specimenMaxGrainSize);
    }

    @GetMapping("/specimenMaxGrainFraction")
    public List<RocksUser> getBySpecimenMaxGrainFraction(@RequestParam String specimenMaxGrainFraction) {
        return rocksUserRepository.findBySpecimenMaxGrainFractionContainingIgnoreCase(specimenMaxGrainFraction);
    }

    @GetMapping("/schedulingNotes")
    public List<RocksUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return rocksUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

    // Get all distinct groups
    @GetMapping("/groups")
    public List<String> getAllGroups() {
        return rocksUserRepository.findAllGroups();
    }
}
