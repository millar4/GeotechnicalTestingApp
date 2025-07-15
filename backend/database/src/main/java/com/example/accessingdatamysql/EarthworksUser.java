package com.example.accessingdatamysql;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity 
@Table(name = "EarthworksTable")
public class EarthworksUser {

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    @Column(name = "myGroup", nullable = true, unique = false)
    private String group;

    @Column(name = "classification", nullable = true, unique = false)
    private String classification;

    @Column(name = "test", nullable = true, unique = false)
    private String test;

    @Column(name = "testAlsoKnownAs", nullable = true, unique = false)
    private String testAlsoKnownAs;

    @Column(name = "symbol", nullable = true, unique = true)
    private String symbol;

    @Column(name = "parameters", nullable = true, unique = false)
    private String parameters;

    @Column(name = "testMethod", nullable = true, unique = false)
    private String testMethod;

    @Column(name = "sampleType", nullable = true, unique = false)
    private String sampleType;

    @Column(name = "fieldSampleMass", nullable = true, unique = false)
    private String fieldSampleMass;  // Changed to String

    @Column(name = "specimenType", nullable = true, unique = false)
    private String specimenType;

    @Column(name = "specimenMass", nullable = true, unique = false)
    private String specimenMass;  // Changed to String

    @Column(name = "specimenNumbers", nullable = true, unique = false)
    private String specimenNumbers;  // Changed to String

    @Column(name = "specimenMaxGrainSize", nullable = true, unique = false)
    private String specimenMaxGrainSize;  // Changed to String

    @Column(name = "specimenMaxGrainFraction", nullable = true, unique = false)
    private String specimenMaxGrainFraction;

    @Column(name = "schedulingNotes", nullable = true, unique = false)
    private String schedulingNotes;

    @Column(name = "databaseBelongsTo", nullable = true, unique = false)
    private String databaseBelongsTo;

    @Column(name = "imagePath", nullable = true, unique = false)
    private String imagePath;

    @Column(name = "testDescription", nullable = true, unique = false)
    private String testDescription;

    // Getters and setters for all fields
    public Long getId() {return id;}
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getTest() { return test; }
    public void setTest(String test) { this.test= test; }

    public String getAlsoKnownAs() { return test; }
    public void setTestAlsoKnownAs(String testAlsoKnownAs) { this.testAlsoKnownAs= testAlsoKnownAs; }

    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.test= classification; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }

    public String getTestMethod() { return testMethod; }
    public void setTestMethod(String testMethod) { this.testMethod = testMethod; }

    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }

    public String getFieldSampleMass() { return fieldSampleMass; }
    public void setFieldSampleMass(String fieldSampleMass) { this.fieldSampleMass = fieldSampleMass; }

    public String getSpecimenType() { return specimenType; }
    public void setSpecimenType(String specimenType) { this.specimenType = specimenType; }

    public String getSpecimenMass() { return specimenMass; }
    public void setSpecimenMass(String specimenMass) { this.specimenMass = specimenMass; }

    public String getSpecimenNumbers() { return specimenNumbers; }
    public void setSpecimenNumbers(String specimenNumbers) { this.specimenNumbers = specimenNumbers; }

    public String getSpecimenMaxGrainSize() { return specimenMaxGrainSize; }
    public void setSpecimenMaxGrainSize(String specimenMaxGrainSize) { this.specimenMaxGrainSize = specimenMaxGrainSize; }

    public String getSpecimenMaxGrainFraction() { return specimenMaxGrainFraction; }
    public void setSpecimenMaxGrainFraction(String specimenMaxGrainFraction) { this.specimenMaxGrainFraction = specimenMaxGrainFraction; }

    public String getSchedulingNotes() { return schedulingNotes; }
    public void setSchedulingNotes(String schedulingNotes) { this.schedulingNotes = schedulingNotes; }


    public String getDatabaseBelongsTo() { return databaseBelongsTo; }
    public void setDatabaseBelongsTo(String databaseBelongsTo) { this.databaseBelongsTo = databaseBelongsTo; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getTestDescription() { return imagePath; }
    public void setTestDescription(String imagePath) { this.imagePath = imagePath; }

}

