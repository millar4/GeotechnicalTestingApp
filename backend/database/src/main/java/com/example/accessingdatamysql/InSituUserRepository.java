package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InSituUserRepository extends JpaRepository<InSituUser, Long> {

    @Query("SELECT DISTINCT r.myGroup FROM InSituUser r WHERE r.myGroup IS NOT NULL AND r.myGroup <> 'NULL' ORDER BY r.myGroup ASC")
    List<String> findAllGroups();

    @Query("SELECT r FROM InSituUser r WHERE r.parameters IS NOT NULL AND r.parameters <> 'NULL'")
    List<InSituUser> findByParametersNotNull();

    @Query("SELECT r FROM InSituUser r WHERE r.myGroup IS NOT NULL AND r.myGroup <> 'NULL' AND r.myGroup LIKE %?1%")
    List<InSituUser> findByMyGroupContaining(String myGroup);

    @Query("SELECT r FROM InSituUser r WHERE r.test IS NOT NULL AND r.test <> 'NULL' AND r.test LIKE %?1%")
    List<InSituUser> findByTestContaining(String test);

    @Query("SELECT r FROM InSituUser r WHERE r.test IS NOT NULL AND r.test <> 'NULL' AND r.test LIKE %?1%")
    List<InSituUser> findByTestAlsoKnownAsContaining(String testAlsoKnownAs);

    @Query("SELECT r FROM InSituUser r WHERE r.symbol IS NOT NULL AND r.symbol <> 'NULL' AND r.symbol LIKE %?1%")
    List<InSituUser> findBySymbolContaining(String symbol);

    @Query("SELECT r FROM InSituUser r WHERE r.parameters IS NOT NULL AND r.parameters <> 'NULL' AND r.parameters LIKE %?1%")
    List<InSituUser> findByParametersContaining(String parameters);

    @Query("SELECT r FROM InSituUser r WHERE r.testMethod IS NOT NULL AND r.testMethod <> 'NULL' AND r.testMethod LIKE %?1%")
    List<InSituUser> findByTestMethodContaining(String testMethod);

    @Query("SELECT r FROM InSituUser r WHERE r.alt1 IS NOT NULL AND r.alt1 <> 'NULL' AND r.alt1 LIKE %?1%")
    List<InSituUser> findByAlt1Containing(String alt1);

    @Query("SELECT r FROM InSituUser r WHERE r.alt2 IS NOT NULL AND r.alt2 <> 'NULL' AND r.alt2 LIKE %?1%")
    List<InSituUser> findByAlt2Containing(String alt2);

    @Query("SELECT r FROM InSituUser r WHERE r.alt3 IS NOT NULL AND r.alt3 <> 'NULL' AND r.alt3 LIKE %?1%")
    List<InSituUser> findByAlt3Containing(String alt3);

    @Query("SELECT r FROM InSituUser r WHERE r.databaseBelongsTo IS NOT NULL AND r.databaseBelongsTo <> 'NULL' AND LOWER(r.databaseBelongsTo) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<InSituUser> findByDatabaseBelongsToContaining(String databaseBelongsTo);

    @Query("SELECT r FROM InSituUser r WHERE r.imagePath IS NOT NULL AND r.databaseBelongsTo <> 'NULL' AND LOWER(r.imagePath) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<InSituUser> findByImagePathContaining(String imagePath);

    @Query("SELECT r FROM InSituUser r WHERE r.testDescription IS NOT NULL AND r.testDescription <> 'NULL' AND LOWER(r.testDescription) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<InSituUser> findByTestDescriptionContaining(String testDescription);

    @Query("SELECT r FROM InSituUser r WHERE r.materials IS NOT NULL AND r.materials <> 'NULL' AND LOWER(r.materials) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<InSituUser> findByMaterialsContaining(String materials);

    @Query("SELECT r FROM InSituUser r WHERE r.applications IS NOT NULL AND r.applications <> 'NULL' AND LOWER(r.applications) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<InSituUser> findByApplicationsContaining(String applications);



    List<InSituUser> findAllByOrderByIdAsc();
    List<InSituUser> findAllByOrderByMyGroupAsc();
    List<InSituUser> findAllByOrderByTestMethodAsc();
    List<InSituUser> findAllByOrderByParametersAsc();
}
