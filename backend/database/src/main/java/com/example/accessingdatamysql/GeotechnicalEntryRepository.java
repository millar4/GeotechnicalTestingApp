package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface GeotechnicalEntryRepository extends JpaRepository<GeotechnicalEntry, Long> {

    @Query("SELECT DISTINCT g.myGroup FROM GeotechnicalEntry g ORDER BY g.myGroup ASC")
    List<String> findAllGroups();

     // The following methods all use fuzzy queries
    List<GeotechnicalEntry> findByMyGroupContaining(String myGroup);
    List<GeotechnicalEntry> findByTestContaining(String test);
    List<GeotechnicalEntry> findByTestAlsoKnownAsContaining(String test);
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
    List<GeotechnicalEntry> findAllByOrderByMyGroupAsc();
    List<GeotechnicalEntry> findAllByOrderByTestMethodAsc();
    List<GeotechnicalEntry> findAllByOrderByParametersAsc();
    List<GeotechnicalEntry> findByDatabaseBelongsToContaining(String databaseBelongsTo);
    List<GeotechnicalEntry> findByImagePathContaining(String imagePath);
    List<GeotechnicalEntry> findByTestDescriptionContaining(String testDescription);
}
