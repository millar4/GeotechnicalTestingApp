package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InSituUserRepository extends CrudRepository<InSituUser, Long> {

    @Query("SELECT DISTINCT g.myGroup FROM InSituUser g ORDER BY g.myGroup ASC")
    List<String> findAllGroups();

    // The following methods all use fuzzy queries

    List<InSituUser> findByMyGroupContaining(String myGroup);
    List<InSituUser> findByTestContaining(String test);
    List<InSituUser> findBySymbolContaining(String symbol);
    List<InSituUser> findByParametersContaining(String parameters);
    List<InSituUser> findByTestMethodContaining(String testMethod);
    List<InSituUser> findByAlt1Containing(String alt1);
    List<InSituUser> findByAlt2Containing(String alt2);
    List<InSituUser> findByAlt3Containing(String alt3);
    List<InSituUser> findBySampleTypeContaining(String sampleType);
    List<InSituUser> findByFieldSampleMassContaining(String mass);
    List<InSituUser> findBySpecimenTypeContaining(String specimenType);
    List<InSituUser> findBySpecimenMassContaining(String mass);
    List<InSituUser> findBySpecimenNumbersContaining(String numbers);
    List<InSituUser> findBySpecimenDContaining(String diameter);
    List<InSituUser> findBySpecimenLContaining(String length);
    List<InSituUser> findBySpecimenWContaining(String width);
    List<InSituUser> findBySpecimenHContaining(String height);
    List<InSituUser> findBySpecimenMaxGrainSizeContaining(String grainSize);
    List<InSituUser> findBySpecimenMaxGrainFractionContaining(String fraction);
    List<InSituUser> findBySchedulingNotesContainingIgnoreCase(String schedulingNotes);

    // ORDER BY
    List<InSituUser> findAllByOrderByIdAsc();
    List<InSituUser> findAllByOrderByMyGroupAsc();
    List<InSituUser> findAllByOrderByTestMethodAsc();
    List<InSituUser> findAllByOrderByParametersAsc();

    List<InSituUser> findByDatabaseBelongsToContainingIgnoreCase(String schedulingNotes);
}
