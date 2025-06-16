package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RocksUserRepository extends JpaRepository<RocksUser, Long> {

    // Query methods for each field, supporting case-insensitive, partial string matching
    List<RocksUser> findByGroupContainingIgnoreCase(String group);

    List<RocksUser> findBySymbolContainingIgnoreCase(String symbol);

    List<RocksUser> findByParametersContainingIgnoreCase(String parameters);

    List<RocksUser> findByTestMethodContainingIgnoreCase(String testMethod);

    List<RocksUser> findBySampleTypeContainingIgnoreCase(String sampleType);

    List<RocksUser> findByFieldSampleMassContainingIgnoreCase(String fieldSampleMass);

    List<RocksUser> findBySpecimenTypeContainingIgnoreCase(String specimenType);

    List<RocksUser> findBySpecimenMassContainingIgnoreCase(String specimenMass);

    List<RocksUser> findBySpecimenNumbersContainingIgnoreCase(String specimenNumbers);

    List<RocksUser> findBySpecimenMaxGrainSizeContainingIgnoreCase(String specimenMaxGrainSize);

    List<RocksUser> findBySpecimenMaxGrainFractionContainingIgnoreCase(String specimenMaxGrainFraction);

    List<RocksUser> findBySchedulingNotesContainingIgnoreCase(String schedulingNotes);

    //New interface: query all distinct group tags
    @Query("SELECT DISTINCT r.group FROM RocksUser r")
    List<String> findAllGroups();
}