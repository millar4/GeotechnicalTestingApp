package com.example.accessingdatamysql;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "http://localhost:3100")
@Controller
@RequestMapping(path = "/database")
public class MainController {

    @Autowired
    private GeotechnicalEntryRepository userRepository;

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
        Long longId;
        try {
            longId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }

        Optional<GeotechnicalEntry> userOpt = userRepository.findById(longId);
        if (userOpt.isPresent()) {
            return Collections.singletonList(userOpt.get());
        } else {
            return Collections.emptyList();
        }
    }
        
    @GetMapping("/classification")
    @ResponseBody
    public List<GeotechnicalEntry> getUserByClassification(@RequestParam String classification) {
        return userRepository.findByClassificationContaining(classification);
    }

    @PostMapping("/add")
    @ResponseBody
    public GeotechnicalEntry addGeotechnicalEntry (@RequestBody GeotechnicalEntry GeotechnicalEntry) {
        return userRepository.save(GeotechnicalEntry);
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
        // 1. Find an existing record in the database based on the primary key id.
        Optional<GeotechnicalEntry> existingOpt = userRepository.findById(id);
        if (existingOpt.isEmpty()) {
           // Returns 404 if the corresponding record is not found.
            return ResponseEntity.notFound().build();
        }
        // 2. Synchronize the updated fields passed by the front-end into the database.
        GeotechnicalEntry existing = existingOpt.get();
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
        // ... More fields can be assigned here as well.

        // 3. Preservation of updated entities
        GeotechnicalEntry saved = userRepository.save(existing);

    // 4. Return the updated entity to the front end
        return ResponseEntity.ok(saved);
    }

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
    public List<GeotechnicalEntry> getUsersByTestDescripton(@RequestParam String imagePath) {
        return userRepository.findByImagePathContaining(imagePath);
    }
}
