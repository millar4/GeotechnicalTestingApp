package com.example.accessingdatamysql;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity 
@Table(name = "RocksTable")
public class RocksUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @Column(name = "test", nullable = true, unique = false)
    private String test;

    @Column(name = "testAlsoKnownAs", nullable = true, unique = false)
    private String testAlsoKnownAs;

    @Column(name = "myGroup", nullable = true, unique = false)
    private String myGroup;

    @Column(name = "classification", nullable = true, unique = false)
    private String classification;

    @Column(name = "symbol", nullable = true, unique = true)
    private String symbol;

    @Column(name = "parameters", nullable = true, unique = false)
    private String parameters;

    @Column(name = "testMethod", nullable = true, unique = false)
    private String testMethod;

    @Column(name = "alt1", nullable = true, unique = false)
    private String alt1;

    @Column(name = "alt2", nullable = true, unique = false)
    private String alt2;

    @Column(name = "alt3", nullable = true, unique = false)
    private String alt3;

    @Column(name = "sampleType", nullable = true, unique = false)
    private String sampleType;

    @Column(name = "fieldSampleMass", nullable = true, unique = false)
    private String fieldSampleMass;

    @Column(name = "specimenType", nullable = true, unique = false)
    private String specimenType;

    @Column(name = "specimenMass", nullable = true, unique = false)
    private String specimenMass;

    @Column(name = "specimenNumbers", nullable = true, unique = false)
    private String specimenNumbers;

    @Column(name = "specimenD", nullable = true, unique = false)
    private String specimenD;

    @Column(name = "specimenL", nullable = true, unique = false)
    private String specimenL;

    @Column(name = "specimenW", nullable = true, unique = false)
    private String specimenW;

    @Column(name = "specimenH", nullable = true, unique = false)
    private String specimenH;

    @Column(name = "specimenMaxGrainSize", nullable = true, unique = false)
    private String specimenMaxGrainSize;

    @Column(name = "specimenMaxGrainFraction", nullable = true, unique = false)
    private String specimenMaxGrainFraction;

    @Column(name = "schedulingNotes", nullable = true, unique = false)
    private String schedulingNotes;

    @Column(name = "databaseBelongsTo", nullable = true, unique = false)
    private String databaseBelongsTo;

    @Column(name = "imagePath", nullable = true, unique = false)
    private String imagePath;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTest() { return test; }
    public void setTest(String test) { this.test = test; }

    public String getTestAlsoKnownAs() { return test; }
    public void setTestAlsoKnownAs(String testAlsoKnownAs) { this.testAlsoKnownAs = testAlsoKnownAs; }

    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }

    public String getmyGroup() { return myGroup; }
    public void setmyGroup(String myGroup) { this.myGroup = myGroup; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }

    public String getTestMethod() { return testMethod; }
    public void setTestMethod(String testMethod) { this.testMethod = testMethod; }

    public String getAlt1() { return alt1; }
    public void setAlt1(String alt1) { this.alt1 = alt1; }

    public String getAlt2() { return alt2; }
    public void setAlt2(String alt2) { this.alt2 = alt2; }

    public String getAlt3() { return alt3; }
    public void setAlt3(String alt3) { this.alt3 = alt3; }

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

    public String getSpecimenD() { return specimenD; }
    public void setSpecimenD(String specimenD) { this.specimenD = specimenD; }

    public String getSpecimenL() { return specimenL; }
    public void setSpecimenL(String specimenL) { this.specimenL = specimenL; }

    public String getSpecimenW() { return specimenW; }
    public void setSpecimenW(String specimenW) { this.specimenW = specimenW; }

    public String getSpecimenH() { return specimenH; }
    public void setSpecimenH(String specimenH) { this.specimenH = specimenH; }

    public String getSpecimenMaxGrainSize() { return specimenMaxGrainSize; }
    public void setSpecimenMaxGrainSize(String specimenMaxGrainSize) { this.specimenMaxGrainSize = specimenMaxGrainSize; }

    public String getSpecimenMaxGrainFraction() { return specimenMaxGrainFraction; }
    public void setSpecimenMaxGrainFraction(String specimenMaxGrainFraction) { this.specimenMaxGrainFraction = specimenMaxGrainFraction; }

    public String getSchedulingNotes() { return schedulingNotes; }
    public void setSchedulingNotes(String schedulingNotes) { this.schedulingNotes = schedulingNotes; }

    public String getDatabaseBelongsTo() { return databaseBelongsTo; }
    public void setDatabaseBelongsTo(String databaseBelongsTo) { this.databaseBelongsTo = databaseBelongsTo; }

    public String getImagePath() { return databaseBelongsTo; }
    public void setImagePath(String databaseBelongsTo) { this.databaseBelongsTo = databaseBelongsTo; }
}
