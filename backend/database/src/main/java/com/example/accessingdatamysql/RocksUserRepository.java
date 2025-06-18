package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RocksUserRepository extends CrudRepository<RocksUser, Long> {

    @Query("SELECT DISTINCT g.group FROM RocksUser g ORDER BY g.group ASC")
    List<String> findAllGroups();

     // The following methods all use fuzzy queries

    List<RocksUser> findByGroupContaining(String group);
    List<RocksUser> findByTestContaining(String test);
    List<RocksUser> findBySymbolContaining(String symbol);
    List<RocksUser> findByParametersContaining(String parameters);
    List<RocksUser> findByTestMethodContaining(String testMethod);
    List<RocksUser> findByAlt1Containing(String alt1);
    List<RocksUser> findByAlt2Containing(String alt2);
    List<RocksUser> findByAlt3Containing(String alt3);
    List<RocksUser> findBySampleTypeContaining(String sampleType);
    
    List<RocksUser> findByFieldSampleMassContaining(String mass);
    List<RocksUser> findBySpecimenTypeContaining(String specimenType);
    List<RocksUser> findBySpecimenMassContaining(String mass);
    List<RocksUser> findBySpecimenNumbersContaining(String numbers);
    List<RocksUser> findBySpecimenDContaining(String diameter);
    List<RocksUser> findBySpecimenLContaining(String length);
    List<RocksUser> findBySpecimenWContaining(String width);
    List<RocksUser> findBySpecimenHContaining(String height);
    List<RocksUser> findBySpecimenMaxGrainSizeContaining(String grainSize);
    List<RocksUser> findBySpecimenMaxGrainFractionContaining(String fraction);
    List<RocksUser> findBySchedulingNotesContainingIgnoreCase(String schedulingNotes);

    // ORDER BY
    List<RocksUser> findAllByOrderByIdAsc();
    List<RocksUser> findAllByOrderByGroupAsc();
    List<RocksUser> findAllByOrderByTestMethodAsc();
    List<RocksUser> findAllByOrderByParametersAsc();

}
