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
@RequestMapping(path = "/inSituTest")
public class InSituUserController{

    @Autowired
    private InSituUserRepository InSituUserRepository;

    @GetMapping(path = "/all/table")
    public String getAllUsersByTable(Model model) {
        Iterable<InSituUser> users = InSituUserRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(path = "/all")
    public @ResponseBody List<InSituUser> getAllUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return (List<InSituUser>) InSituUserRepository.findAll();
        }


        switch (sort) {
            case "default":
                return InSituUserRepository.findAllByOrderByIdAsc();
            case "name":
                return InSituUserRepository.findAllByOrderByMyGroupAsc();
            case "method":
                return InSituUserRepository.findAllByOrderByTestMethodAsc();
            case "parameter":
                return InSituUserRepository.findAllByOrderByParametersAsc();
            default:
                return (List<InSituUser>) InSituUserRepository.findAll();
        }
    }

    @GetMapping("/id")
    @ResponseBody
    public List<InSituUser> getUserById(@RequestParam String id) {
        Long longId;
        try {
            longId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }

        Optional<InSituUser> userOpt = InSituUserRepository.findById(longId);
        if (userOpt.isPresent()) {
            return Collections.singletonList(userOpt.get());
        } else {
            return Collections.emptyList();
        }
    }

    @PostMapping(path = "/add")
    @ResponseBody
    public ResponseEntity<InSituUser> addUser(@RequestBody InSituUser newEntry) {
        try {
            InSituUser savedEntry = InSituUserRepository.save(newEntry);
            return ResponseEntity.ok(savedEntry);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<InSituUser> entryOpt = InSituUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            InSituUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<InSituUser> updateInSituUser(
            @PathVariable Long id,
            @RequestBody InSituUser updatedEntry) {
        // 1. Find an existing record in the database based on the primary key id.
        Optional<InSituUser> existingOpt = InSituUserRepository.findById(id);
        if (existingOpt.isEmpty()) {
           // Returns 404 if the corresponding record is not found.
            return ResponseEntity.notFound().build();
        }
        // 2. Synchronize the updated fields passed by the front-end into the database.
        InSituUser existing = existingOpt.get();
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
        // ... More fields can be assigned here as well.

        // 3. Preservation of updated entities
        InSituUser saved = InSituUserRepository.save(existing);

    // 4. Return the updated entity to the front end
        return ResponseEntity.ok(saved);
    }

    @GetMapping(path = "/group")
    @ResponseBody
    public List<InSituUser> getUsersByGroup(@RequestParam String group) {
        return InSituUserRepository.findByMyGroupContaining(group);
    }

    @GetMapping(path = "/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return InSituUserRepository.findAllGroups();
    }

    @GetMapping(path = "/test")
    @ResponseBody
    public List<InSituUser> getUsersByTest(@RequestParam String test) {
        return InSituUserRepository.findByTestContaining(test);
    }

    @GetMapping(path = "/symbol")
    @ResponseBody
    public List<InSituUser> getUsersBySymbol(@RequestParam String symbol) {
        return InSituUserRepository.findBySymbolContaining(symbol);
    }

    @GetMapping(path = "/parameters")
    @ResponseBody
    public List<InSituUser> getUsersByParameters(@RequestParam String parameters) {
        return InSituUserRepository.findByParametersContaining(parameters);
    }

    @GetMapping(path = "/testMethod")
    @ResponseBody
    public List<InSituUser> getInSituUsersByTestMethod(@RequestParam String testMethod) {
        return InSituUserRepository.findByTestMethodContaining(testMethod);
    }

    @GetMapping(path = "/alt1")
    @ResponseBody
    public List<InSituUser> getUsersByAlt1(@RequestParam String alt1) {
        return InSituUserRepository.findByAlt1Containing(alt1);
    }

    @GetMapping(path = "/alt2")
    @ResponseBody
    public List<InSituUser> getUsersByAlt2(@RequestParam String alt2) {
        return InSituUserRepository.findByAlt2Containing(alt2);
    }

    @GetMapping(path = "/alt3")
    @ResponseBody
    public List<InSituUser> getUsersByAlt3(@RequestParam String alt3) {
        return InSituUserRepository.findByAlt3Containing(alt3);
    }

    @GetMapping(path = "/sampleType")
    @ResponseBody
    public List<InSituUser> getUsersBySampleType(@RequestParam String sampleType) {
        return InSituUserRepository.findBySampleTypeContaining(sampleType);
    }

    @GetMapping(path = "/fieldSampleMassGreaterThan")
    @ResponseBody
    public List<InSituUser> getUsersByFieldSampleMass(@RequestParam String mass) {
        return InSituUserRepository.findByFieldSampleMassContaining(mass);
    }

    @GetMapping(path = "/specimenType")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenType(@RequestParam String specimenType) {
        return InSituUserRepository.findBySpecimenTypeContaining(specimenType);
    }

    @GetMapping(path = "/specimenMassGreaterThan")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenMass(@RequestParam String mass) {
        return InSituUserRepository.findBySpecimenMassContaining(mass);
    }

    @GetMapping(path = "/specimenNumbers")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenNumbers(@RequestParam String numbers) {
        return InSituUserRepository.findBySpecimenNumbersContaining(numbers);
    }

    @GetMapping(path = "/specimenD")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenD(@RequestParam String diameter) {
        return InSituUserRepository.findBySpecimenDContaining(diameter);
    }

    @GetMapping(path = "/specimenL")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenL(@RequestParam String length) {
        return InSituUserRepository.findBySpecimenLContaining(length);
    }

    @GetMapping(path = "/specimenW")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenW(@RequestParam String width) {
        return InSituUserRepository.findBySpecimenWContaining(width);
    }

    @GetMapping(path = "/specimenH")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenH(@RequestParam String height) {
        return InSituUserRepository.findBySpecimenHContaining(height);
    }

    @GetMapping(path = "/specimenMaxGrainSize")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenMaxGrainSize(@RequestParam String grainSize) {
        return InSituUserRepository.findBySpecimenMaxGrainSizeContaining(grainSize);
    }

    @GetMapping(path = "/specimenMaxGrainFraction")
    @ResponseBody
    public List<InSituUser> getUsersBySpecimenMaxGrainFraction(@RequestParam String fraction) {
        return InSituUserRepository.findBySpecimenMaxGrainFractionContaining(fraction);
    }

    @GetMapping("/schedulingNotes")
    public List<InSituUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return InSituUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

    @GetMapping("/databaseBelongsTo")
    public List<InSituUser> getByDatabaseBelongsTo(@RequestParam String schedulingNotes) {
        return InSituUserRepository.findByDatabaseBelongsToContainingIgnoreCase(schedulingNotes);
    }

}
