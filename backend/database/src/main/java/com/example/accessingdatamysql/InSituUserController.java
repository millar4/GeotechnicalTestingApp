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

    @GetMapping(path = "/testAlsoKnownAs")
    @ResponseBody
    public List<InSituUser> getUsersByTestAlsoKnownAs(@RequestParam String testAlsoKnownAs) {
        return InSituUserRepository.findByTestAlsoKnownAsContaining(testAlsoKnownAs);
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

    @GetMapping("/databaseBelongsTo")
    public List<InSituUser> getByDatabaseBelongsTo(@RequestParam String schedulingNotes) {
        return InSituUserRepository.findByDatabaseBelongsToContainingIgnoreCase(schedulingNotes);
    }

    @GetMapping("/imagePath")
    public List<InSituUser> getByImagePathBelongsTo(@RequestParam String imagePath) {
        return InSituUserRepository.findByImagePathContaining(imagePath);
    }

}
