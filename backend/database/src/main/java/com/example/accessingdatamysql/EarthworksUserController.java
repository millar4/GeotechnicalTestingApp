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
@RequestMapping("/earthworks")
public class EarthworksUserController {

    @Autowired
    private EarthworksUserRepository EarthworksUserRepository;

    // Add new record
    @PostMapping("/add")
    public EarthworksUser addEarthworksUser(@RequestBody EarthworksUser EarthworksUser) {
        return EarthworksUserRepository.save(EarthworksUser);
    }

    @GetMapping(path = "/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return EarthworksUserRepository.findAllGroups();
    }

    // Update existing record
    @PutMapping("/update/{id}")
    public EarthworksUser updateEarthworksUser(@PathVariable Long id, @RequestBody EarthworksUser updatedUser) {
        Optional<EarthworksUser> optionalUser = EarthworksUserRepository.findById(id);
        if (optionalUser.isPresent()) {
            EarthworksUser existingUser = optionalUser.get();
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

            return EarthworksUserRepository.save(existingUser);
        } else {
            throw new RuntimeException("EarthworksUser not found with id " + id);
        }
    }

    // Delete by ID
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<EarthworksUser> entryOpt = EarthworksUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            EarthworksUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    // Get all records, optional sort
    @GetMapping("/all")
    public List<EarthworksUser> getAllEarthworksUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return EarthworksUserRepository.findAll();
        }
        switch (sort) {
            case "default":
                return EarthworksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            case "name":
                return EarthworksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "group"));
            case "method":
                return EarthworksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "testMethod"));
            case "parameter":
                return EarthworksUserRepository.findAll(Sort.by(Sort.Direction.ASC, "parameters"));
            default:
                return EarthworksUserRepository.findAll();
        }
    }

    // Precise search by ID
    @GetMapping("/id")
    public List<EarthworksUser> getById(@RequestParam String id) {
        try {
            Long longId = Long.parseLong(id);
            Optional<EarthworksUser> opt = EarthworksUserRepository.findById(longId);
            return opt.map(List::of).orElse(List.of());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    // Fuzzy search endpoints
    @GetMapping("/group")
    public List<EarthworksUser> getByGroup(@RequestParam String group) {
        return EarthworksUserRepository.findByGroupContainingIgnoreCase(group);
    }

    @GetMapping("/test")
    public List<EarthworksUser> getByTest(@RequestParam String test) {
        return EarthworksUserRepository.findByTestContainingIgnoreCase(test);
    }

    @GetMapping("/testAlsoKnownAs")
    public List<EarthworksUser> getByTestAlsoKnownAs(@RequestParam String test) {
        return EarthworksUserRepository.findByTestAlsoKnownAs(test);
    }

    @GetMapping("/symbol")
    public List<EarthworksUser> getBySymbol(@RequestParam String symbol) {
        return EarthworksUserRepository.findBySymbolContainingIgnoreCase(symbol);
    }

    @GetMapping("/parameters")
    public List<EarthworksUser> getByParameters(@RequestParam String parameters) {
        return EarthworksUserRepository.findByParametersContainingIgnoreCase(parameters);
    }

    @GetMapping("/testMethod")
    public List<EarthworksUser> getByTestMethod(@RequestParam String testMethod) {
        return EarthworksUserRepository.findByTestMethodContainingIgnoreCase(testMethod);
    }

    @GetMapping("/classification")
    public List<EarthworksUser> getByClassification(@RequestParam String classification) {
        return EarthworksUserRepository.findByClassification(classification);
    }

    @GetMapping("/sampleType")
    public List<EarthworksUser> getBySampleType(@RequestParam String sampleType) {
        return EarthworksUserRepository.findBySampleTypeContainingIgnoreCase(sampleType);
    }

    @GetMapping("/fieldSampleMass")
    public List<EarthworksUser> getByFieldSampleMass(@RequestParam String fieldSampleMass) {
        return EarthworksUserRepository.findByFieldSampleMassContainingIgnoreCase(fieldSampleMass);
    }

    @GetMapping("/specimenType")
    public List<EarthworksUser> getBySpecimenType(@RequestParam String specimenType) {
        return EarthworksUserRepository.findBySpecimenTypeContainingIgnoreCase(specimenType);
    }

    @GetMapping("/specimenMass")
    public List<EarthworksUser> getBySpecimenMass(@RequestParam String specimenMass) {
        return EarthworksUserRepository.findBySpecimenMassContainingIgnoreCase(specimenMass);
    }

    @GetMapping("/specimenNumbers")
    public List<EarthworksUser> getBySpecimenNumbers(@RequestParam String specimenNumbers) {
        return EarthworksUserRepository.findBySpecimenNumbersContainingIgnoreCase(specimenNumbers);
    }

    @GetMapping("/specimenMaxGrainSize")
    public List<EarthworksUser> getBySpecimenMaxGrainSize(@RequestParam String specimenMaxGrainSize) {
        return EarthworksUserRepository.findBySpecimenMaxGrainSizeContainingIgnoreCase(specimenMaxGrainSize);
    }

    @GetMapping("/specimenMaxGrainFraction")
    public List<EarthworksUser> getBySpecimenMaxGrainFraction(@RequestParam String specimenMaxGrainFraction) {
        return EarthworksUserRepository.findBySpecimenMaxGrainFractionContainingIgnoreCase(specimenMaxGrainFraction);
    }

    @GetMapping("/schedulingNotes")
    public List<EarthworksUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return EarthworksUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

    @GetMapping("/databaseBelongsTo")
    public List<EarthworksUser> getByDatabaseBelongsTo(@RequestParam String databaseBelongsTo) {
        return EarthworksUserRepository.findByDatabaseBelongsToContainingIgnoreCase(databaseBelongsTo);
    }

    @GetMapping("/imagePath")
    public List<EarthworksUser> getByImagePath(@RequestParam String imagePath) {
        return EarthworksUserRepository.findByDatabaseBelongsToContainingIgnoreCase(imagePath);
    }

     @GetMapping("/testDescription")
    public List<EarthworksUser> getByTestDescription(@RequestParam String testDescription) {
        return EarthworksUserRepository.findByTestDescriptionContainingIgnoreCase(testDescription);
    }
}
