package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConcreteUserRepository extends CrudRepository<ConcreteUser, Long> {

    @Query("SELECT DISTINCT g.myGroup FROM ConcreteUser g ORDER BY g.myGroup ASC")
    List<String> findAllGroups();

    // The following methods all use fuzzy queries

    List<ConcreteUser> findByMyGroupContaining(String myGroup);
    List<ConcreteUser> findByTestContaining(String test);
    List<ConcreteUser> findBySymbolContaining(String symbol);
    List<ConcreteUser> findByParametersContaining(String parameters);
    List<ConcreteUser> findByTestMethodContaining(String testMethod);
    List<ConcreteUser> findByAlt1Containing(String alt1);
    List<ConcreteUser> findByAlt2Containing(String alt2);
    List<ConcreteUser> findByAlt3Containing(String alt3);
    List<ConcreteUser> findBySampleTypeContaining(String sampleType);
    List<ConcreteUser> findByFieldSampleMassContaining(String mass);
    List<ConcreteUser> findBySpecimenTypeContaining(String specimenType);
    List<ConcreteUser> findBySpecimenMassContaining(String mass);
    List<ConcreteUser> findBySpecimenNumbersContaining(String numbers);
    List<ConcreteUser> findBySpecimenDContaining(String diameter);
    List<ConcreteUser> findBySpecimenLContaining(String length);
    List<ConcreteUser> findBySpecimenWContaining(String width);
    List<ConcreteUser> findBySpecimenHContaining(String height);
    List<ConcreteUser> findBySpecimenMaxGrainSizeContaining(String grainSize);
    List<ConcreteUser> findBySpecimenMaxGrainFractionContaining(String fraction);
    List<ConcreteUser> findBySchedulingNotesContainingIgnoreCase(String schedulingNotes);

    // ORDER BY
    List<ConcreteUser> findAllByOrderByIdAsc();
    List<ConcreteUser> findAllByOrderByMyGroupAsc();
    List<ConcreteUser> findAllByOrderByTestMethodAsc();
    List<ConcreteUser> findAllByOrderByParametersAsc();

    List<ConcreteUser> findByDatabaseBelongsToContainingIgnoreCase(String databaseBelongsTo);
}
