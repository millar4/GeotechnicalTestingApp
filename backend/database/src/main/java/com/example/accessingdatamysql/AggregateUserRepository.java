package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AggregateUserRepository extends JpaRepository<AggregateUser, Long> {

    // Query methods for each field, supporting case-insensitive, partial string matching
    List<AggregateUser> findByGroupContainingIgnoreCase(String group);

    List<AggregateUser> findBySymbolContainingIgnoreCase(String symbol);

    List<AggregateUser> findByParametersContainingIgnoreCase(String parameters);

    List<AggregateUser> findByTestMethodContainingIgnoreCase(String testMethod);

    List<AggregateUser> findBySampleTypeContainingIgnoreCase(String sampleType);

    List<AggregateUser> findByFieldSampleMassContainingIgnoreCase(String fieldSampleMass);

    List<AggregateUser> findBySpecimenTypeContainingIgnoreCase(String specimenType);

    List<AggregateUser> findBySpecimenMassContainingIgnoreCase(String specimenMass);

    List<AggregateUser> findBySpecimenNumbersContainingIgnoreCase(String specimenNumbers);

    List<AggregateUser> findBySpecimenMaxGrainSizeContainingIgnoreCase(String specimenMaxGrainSize);

    List<AggregateUser> findBySpecimenMaxGrainFractionContainingIgnoreCase(String specimenMaxGrainFraction);

    List<AggregateUser> findBySchedulingNotesContainingIgnoreCase(String schedulingNotes);

    //New interface: query all distinct group tags
    @Query("SELECT DISTINCT r.group FROM AggregateUser r")
    List<String> findAllGroups();
}