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
@RequestMapping(path = "/concrete")
public class ConcreteUserController {

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

    @PostMapping(path = "/add")
    @ResponseBody
    public ResponseEntity<ConcreteUser> addUser(@RequestBody ConcreteUser newEntry) {
        try {
            ConcreteUser savedEntry = ConcreteUserRepository.save(newEntry);
            return ResponseEntity.ok(savedEntry);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
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

}
