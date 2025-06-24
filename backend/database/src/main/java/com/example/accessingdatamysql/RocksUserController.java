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
@RequestMapping(path = "/rocks")
public class RocksUserController {

    @Autowired
    private RocksUserRepository RocksUserRepository;

    @GetMapping(path = "/all/table")
    public String getAllUsersByTable(Model model) {
        Iterable<RocksUser> users = RocksUserRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping(path = "/all")
    public @ResponseBody List<RocksUser> getAllUsers(@RequestParam(required = false) String sort) {
        if (sort == null || sort.isEmpty()) {
            return (List<RocksUser>) RocksUserRepository.findAll();
        }


        switch (sort) {
            case "default":
                return RocksUserRepository.findAllByOrderByIdAsc();
            case "name":
                return RocksUserRepository.findAllByOrderByMyGroupAsc();
            case "method":
                return RocksUserRepository.findAllByOrderByTestMethodAsc();
            case "parameter":
                return RocksUserRepository.findAllByOrderByParametersAsc();
            default:
                return (List<RocksUser>) RocksUserRepository.findAll();
        }
    }

    @GetMapping("/id")
    @ResponseBody
    public List<RocksUser> getUserById(@RequestParam String id) {
        Long longId;
        try {
            longId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }

        Optional<RocksUser> userOpt = RocksUserRepository.findById(longId);
        if (userOpt.isPresent()) {
            return Collections.singletonList(userOpt.get());
        } else {
            return Collections.emptyList();
        }
    }

    @PostMapping(path = "/add")
    @ResponseBody
    public ResponseEntity<RocksUser> addUser(@RequestBody RocksUser newEntry) {
        try {
            RocksUser savedEntry = RocksUserRepository.save(newEntry);
            return ResponseEntity.ok(savedEntry);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<RocksUser> entryOpt = RocksUserRepository.findById(id);
        if (entryOpt.isPresent()) {
            RocksUserRepository.delete(entryOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<RocksUser> updateRocksUser(
            @PathVariable Long id,
            @RequestBody RocksUser updatedEntry) {
        // 1. Find an existing record in the database based on the primary key id.
        Optional<RocksUser> existingOpt = RocksUserRepository.findById(id);
        if (existingOpt.isEmpty()) {
           // Returns 404 if the corresponding record is not found.
            return ResponseEntity.notFound().build();
        }
        // 2. Synchronize the updated fields passed by the front-end into the database.
        RocksUser existing = existingOpt.get();
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
        RocksUser saved = RocksUserRepository.save(existing);

    // 4. Return the updated entity to the front end
        return ResponseEntity.ok(saved);
    }

    @GetMapping(path = "/group")
    @ResponseBody
    public List<RocksUser> getUsersByGroup(@RequestParam String group) {
        return RocksUserRepository.findByMyGroupContaining(group);
    }

    @GetMapping(path = "/groups")
    @ResponseBody
    public List<String> getAllGroups() {
        return RocksUserRepository.findAllGroups();
    }

    @GetMapping(path = "/test")
    @ResponseBody
    public List<RocksUser> getUsersByTest(@RequestParam String test) {
        return RocksUserRepository.findByTestContaining(test);
    }

    @GetMapping(path = "/symbol")
    @ResponseBody
    public List<RocksUser> getUsersBySymbol(@RequestParam String symbol) {
        return RocksUserRepository.findBySymbolContaining(symbol);
    }

    @GetMapping(path = "/classification")
    @ResponseBody
    public List<RocksUser> getUsersByClassification(@RequestParam String classification) {
        return RocksUserRepository.findByClassificationContaining(classification);
    }

    @GetMapping(path = "/parameters")
    @ResponseBody
    public List<RocksUser> getUsersByParameters(@RequestParam String parameters) {
        return RocksUserRepository.findByParametersContaining(parameters);
    }

    @GetMapping(path = "/testMethod")
    @ResponseBody
    public List<RocksUser> getRocksUsersByTestMethod(@RequestParam String testMethod) {
        return RocksUserRepository.findByTestMethodContaining(testMethod);
    }

    @GetMapping(path = "/alt1")
    @ResponseBody
    public List<RocksUser> getUsersByAlt1(@RequestParam String alt1) {
        return RocksUserRepository.findByAlt1Containing(alt1);
    }

    @GetMapping(path = "/alt2")
    @ResponseBody
    public List<RocksUser> getUsersByAlt2(@RequestParam String alt2) {
        return RocksUserRepository.findByAlt2Containing(alt2);
    }

    @GetMapping(path = "/alt3")
    @ResponseBody
    public List<RocksUser> getUsersByAlt3(@RequestParam String alt3) {
        return RocksUserRepository.findByAlt3Containing(alt3);
    }

    @GetMapping(path = "/sampleType")
    @ResponseBody
    public List<RocksUser> getUsersBySampleType(@RequestParam String sampleType) {
        return RocksUserRepository.findBySampleTypeContaining(sampleType);
    }

    @GetMapping(path = "/fieldSampleMassGreaterThan")
    @ResponseBody
    public List<RocksUser> getUsersByFieldSampleMass(@RequestParam String mass) {
        return RocksUserRepository.findByFieldSampleMassContaining(mass);
    }

    @GetMapping(path = "/specimenType")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenType(@RequestParam String specimenType) {
        return RocksUserRepository.findBySpecimenTypeContaining(specimenType);
    }

    @GetMapping(path = "/specimenMassGreaterThan")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenMass(@RequestParam String mass) {
        return RocksUserRepository.findBySpecimenMassContaining(mass);
    }

    @GetMapping(path = "/specimenNumbers")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenNumbers(@RequestParam String numbers) {
        return RocksUserRepository.findBySpecimenNumbersContaining(numbers);
    }

    @GetMapping(path = "/specimenD")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenD(@RequestParam String diameter) {
        return RocksUserRepository.findBySpecimenDContaining(diameter);
    }

    @GetMapping(path = "/specimenL")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenL(@RequestParam String length) {
        return RocksUserRepository.findBySpecimenLContaining(length);
    }

    @GetMapping(path = "/specimenW")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenW(@RequestParam String width) {
        return RocksUserRepository.findBySpecimenWContaining(width);
    }

    @GetMapping(path = "/specimenH")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenH(@RequestParam String height) {
        return RocksUserRepository.findBySpecimenHContaining(height);
    }

    @GetMapping(path = "/specimenMaxGrainSize")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenMaxGrainSize(@RequestParam String grainSize) {
        return RocksUserRepository.findBySpecimenMaxGrainSizeContaining(grainSize);
    }

    @GetMapping(path = "/specimenMaxGrainFraction")
    @ResponseBody
    public List<RocksUser> getUsersBySpecimenMaxGrainFraction(@RequestParam String fraction) {
        return RocksUserRepository.findBySpecimenMaxGrainFractionContaining(fraction);
    }

    @GetMapping("/schedulingNotes")
    public List<RocksUser> getBySchedulingNotes(@RequestParam String schedulingNotes) {
        return RocksUserRepository.findBySchedulingNotesContainingIgnoreCase(schedulingNotes);
    }

}
