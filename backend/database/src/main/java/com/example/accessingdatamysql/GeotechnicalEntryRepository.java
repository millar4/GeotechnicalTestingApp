package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GeotechnicalEntryRepository extends CrudRepository<GeotechnicalEntry, Long> {

    @Query("SELECT DISTINCT g.group FROM GeotechnicalEntry g ORDER BY g.group ASC")
    List<String> findAllGroups();

     // The following methods all use fuzzy queries

    List<GeotechnicalEntry> findByGroupContaining(String group);
    List<GeotechnicalEntry> findByTestContaining(String test);
    List<GeotechnicalEntry> findByClassificationContaining(String classification);
    List<GeotechnicalEntry> findBySymbolContaining(String symbol);
    List<GeotechnicalEntry> findByParametersContaining(String parameters);
    List<GeotechnicalEntry> findByTestMethodContaining(String testMethod);
    List<GeotechnicalEntry> findByAlt1Containing(String alt1);
    List<GeotechnicalEntry> findByAlt2Containing(String alt2);
    List<GeotechnicalEntry> findByAlt3Containing(String alt3);
    List<GeotechnicalEntry> findBySampleTypeContaining(String sampleType);
    
    List<GeotechnicalEntry> findByFieldSampleMassContaining(String mass);
    List<GeotechnicalEntry> findBySpecimenTypeContaining(String specimenType);
    List<GeotechnicalEntry> findBySpecimenMassContaining(String mass);
    List<GeotechnicalEntry> findBySpecimenNumbersContaining(String numbers);
    List<GeotechnicalEntry> findBySpecimenDContaining(String diameter);
    List<GeotechnicalEntry> findBySpecimenLContaining(String length);
    List<GeotechnicalEntry> findBySpecimenWContaining(String width);
    List<GeotechnicalEntry> findBySpecimenHContaining(String height);
    List<GeotechnicalEntry> findBySpecimenMaxGrainSizeContaining(String grainSize);
    List<GeotechnicalEntry> findBySpecimenMaxGrainFractionContaining(String fraction);

    // ORDER BY
    List<GeotechnicalEntry> findAllByOrderByIdAsc();
    List<GeotechnicalEntry> findAllByOrderByGroupAsc();
    List<GeotechnicalEntry> findAllByOrderByTestMethodAsc();
    List<GeotechnicalEntry> findAllByOrderByParametersAsc();
    List<GeotechnicalEntry> findAllByOrderByClassificationAsc();

    List<GeotechnicalEntry> findByDatabaseBelongsToContaining(String databaseBelongsTo);
    List<GeotechnicalEntry> findByImagePathContaining(String imagePath);
}
