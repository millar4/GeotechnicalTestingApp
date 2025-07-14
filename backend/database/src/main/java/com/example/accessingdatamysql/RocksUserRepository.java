package com.example.accessingdatamysql;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RocksUserRepository extends CrudRepository<RocksUser, Long> {

    @Query("SELECT DISTINCT r.myGroup FROM RocksUser r WHERE r.myGroup IS NOT NULL AND r.myGroup <> 'NULL' ORDER BY r.myGroup ASC")
    List<String> findAllGroups();

    @Query("SELECT r FROM RocksUser r WHERE r.parameters IS NOT NULL AND r.parameters <> 'NULL'")
    List<RocksUser> findByParametersNotNull();

    @Query("SELECT r FROM RocksUser r WHERE r.myGroup IS NOT NULL AND r.myGroup <> 'NULL' AND r.myGroup LIKE %?1%")
    List<RocksUser> findByMyGroupContaining(String myGroup);

    @Query("SELECT r FROM RocksUser r WHERE r.test IS NOT NULL AND r.test <> 'NULL' AND r.test LIKE %?1%")
    List<RocksUser> findByTestContaining(String test);

    @Query("SELECT r FROM RocksUser r WHERE r.symbol IS NOT NULL AND r.symbol <> 'NULL' AND r.symbol LIKE %?1%")
    List<RocksUser> findBySymbolContaining(String symbol);

    @Query("SELECT r FROM RocksUser r WHERE r.parameters IS NOT NULL AND r.parameters <> 'NULL' AND r.parameters LIKE %?1%")
    List<RocksUser> findByParametersContaining(String parameters);

    @Query("SELECT r FROM RocksUser r WHERE r.testMethod IS NOT NULL AND r.testMethod <> 'NULL' AND r.testMethod LIKE %?1%")
    List<RocksUser> findByTestMethodContaining(String testMethod);

    @Query("SELECT r FROM RocksUser r WHERE r.alt1 IS NOT NULL AND r.alt1 <> 'NULL' AND r.alt1 LIKE %?1%")
    List<RocksUser> findByAlt1Containing(String alt1);

    @Query("SELECT r FROM RocksUser r WHERE r.alt2 IS NOT NULL AND r.alt2 <> 'NULL' AND r.alt2 LIKE %?1%")
    List<RocksUser> findByAlt2Containing(String alt2);

    @Query("SELECT r FROM RocksUser r WHERE r.alt3 IS NOT NULL AND r.alt3 <> 'NULL' AND r.alt3 LIKE %?1%")
    List<RocksUser> findByAlt3Containing(String alt3);

    @Query("SELECT r FROM RocksUser r WHERE r.sampleType IS NOT NULL AND r.sampleType <> 'NULL' AND r.sampleType LIKE %?1%")
    List<RocksUser> findBySampleTypeContaining(String sampleType);

    @Query("SELECT r FROM RocksUser r WHERE r.fieldSampleMass IS NOT NULL AND r.fieldSampleMass <> 'NULL' AND r.fieldSampleMass LIKE %?1%")
    List<RocksUser> findByFieldSampleMassContaining(String mass);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenType IS NOT NULL AND r.specimenType <> 'NULL' AND r.specimenType LIKE %?1%")
    List<RocksUser> findBySpecimenTypeContaining(String specimenType);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenMass IS NOT NULL AND r.specimenMass <> 'NULL' AND r.specimenMass LIKE %?1%")
    List<RocksUser> findBySpecimenMassContaining(String mass);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenNumbers IS NOT NULL AND r.specimenNumbers <> 'NULL' AND r.specimenNumbers LIKE %?1%")
    List<RocksUser> findBySpecimenNumbersContaining(String numbers);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenD IS NOT NULL AND r.specimenD <> 'NULL' AND r.specimenD LIKE %?1%")
    List<RocksUser> findBySpecimenDContaining(String diameter);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenL IS NOT NULL AND r.specimenL <> 'NULL' AND r.specimenL LIKE %?1%")
    List<RocksUser> findBySpecimenLContaining(String length);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenW IS NOT NULL AND r.specimenW <> 'NULL' AND r.specimenW LIKE %?1%")
    List<RocksUser> findBySpecimenWContaining(String width);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenH IS NOT NULL AND r.specimenH <> 'NULL' AND r.specimenH LIKE %?1%")
    List<RocksUser> findBySpecimenHContaining(String height);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenMaxGrainSize IS NOT NULL AND r.specimenMaxGrainSize <> 'NULL' AND r.specimenMaxGrainSize LIKE %?1%")
    List<RocksUser> findBySpecimenMaxGrainSizeContaining(String grainSize);

    @Query("SELECT r FROM RocksUser r WHERE r.specimenMaxGrainFraction IS NOT NULL AND r.specimenMaxGrainFraction <> 'NULL' AND r.specimenMaxGrainFraction LIKE %?1%")
    List<RocksUser> findBySpecimenMaxGrainFractionContaining(String fraction);

    @Query("SELECT r FROM RocksUser r WHERE r.schedulingNotes IS NOT NULL AND r.schedulingNotes <> 'NULL' AND LOWER(r.schedulingNotes) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<RocksUser> findBySchedulingNotesContainingIgnoreCase(String schedulingNotes);

    @Query("SELECT r FROM RocksUser r WHERE r.classification IS NOT NULL AND r.classification <> 'NULL' AND LOWER(r.classification) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<RocksUser> findByClassificationContaining(String classification);

    @Query("SELECT r FROM RocksUser r WHERE r.classification IS NOT NULL AND r.databaseBelongsTo <> 'NULL' AND LOWER(r.databaseBelongsTo) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<RocksUser> findByDatabaseBelongsToContaining(String databaseBelongsTo);

    @Query("SELECT r FROM RocksUser r WHERE r.classification IS NOT NULL AND r.databaseBelongsTo <> 'NULL' AND LOWER(r.databaseBelongsTo) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<RocksUser> findByImagePathContaining(String imagePath);

    List<RocksUser> findAllByOrderByIdAsc();
    List<RocksUser> findAllByOrderByMyGroupAsc();
    List<RocksUser> findAllByOrderByTestMethodAsc();
    List<RocksUser> findAllByOrderByParametersAsc();
}
