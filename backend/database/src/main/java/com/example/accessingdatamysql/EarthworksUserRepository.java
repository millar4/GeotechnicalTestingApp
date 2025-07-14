package com.example.accessingdatamysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EarthworksUserRepository extends JpaRepository<EarthworksUser, Long> {

    // Query methods for each field, supporting case-insensitive, partial string matching

    @Query("SELECT DISTINCT a.group FROM EarthworksUser a WHERE a.group IS NOT NULL")
    List<String> findAllGroups();
    
    List<EarthworksUser> findByGroupContainingIgnoreCase(String group);

    List<EarthworksUser> findBySymbolContainingIgnoreCase(String symbol);

    List<EarthworksUser> findByParametersContainingIgnoreCase(String parameters);

    List<EarthworksUser> findByTestMethodContainingIgnoreCase(String testMethod);
    List<EarthworksUser> findByTestAlsoKnownAs(String testAlsoKnownAs);

    List<EarthworksUser> findBySampleTypeContainingIgnoreCase(String sampleType);

    List<EarthworksUser> findByFieldSampleMassContainingIgnoreCase(String fieldSampleMass);

    List<EarthworksUser> findBySpecimenTypeContainingIgnoreCase(String specimenType);

    List<EarthworksUser> findBySpecimenMassContainingIgnoreCase(String specimenMass);

    List<EarthworksUser> findBySpecimenNumbersContainingIgnoreCase(String specimenNumbers);

    List<EarthworksUser> findBySpecimenMaxGrainSizeContainingIgnoreCase(String specimenMaxGrainSize);

    List<EarthworksUser> findBySpecimenMaxGrainFractionContainingIgnoreCase(String specimenMaxGrainFraction);

    List<EarthworksUser> findBySchedulingNotesContainingIgnoreCase(String schedulingNotes);

    List<EarthworksUser> findByTestContainingIgnoreCase(String test);

    List<EarthworksUser> findByClassification(String classification);

    List<EarthworksUser> findByDatabaseBelongsToContainingIgnoreCase(String databaseBelongsTo);
    List<EarthworksUser> findByImagePathContainingIgnoreCase(String imagePath);

}