package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AggregateUserRepository extends JpaRepository<AggregateUser, Long> {

    // Query methods for each field, supporting case-insensitive, partial string matching

    @Query("SELECT DISTINCT a.group FROM AggregateUser a WHERE a.group IS NOT NULL")
    List<String> findAllGroups();
    
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

    List<AggregateUser> findByTestContainingIgnoreCase(String test);

    List<AggregateUser> findByClassification(String classification);

    List<AggregateUser> findByDatabaseBelongsToContainingIgnoreCase(String databaseBelongsTo);

}