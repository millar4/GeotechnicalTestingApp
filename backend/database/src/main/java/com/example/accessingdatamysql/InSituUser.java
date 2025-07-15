package com.example.accessingdatamysql;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity 
@Table(name = "InSituTable")
public class InSituUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @Column(name = "myGroup", nullable = true, unique = false)
    private String myGroup;

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

    @Column(name = "alt1", nullable = true, unique = false)
    private String alt1;

    @Column(name = "alt2", nullable = true, unique = false)
    private String alt2;

    @Column(name = "alt3", nullable = true, unique = false)
    private String alt3;

    @Column(name = "databaseBelongsTo", nullable =true, unique = false)
    private String databaseBelongsTo;

    @Column(name = "materials", nullable =true, unique = false)
    private String materials;

    @Column(name = "applications", nullable =true, unique = false)
    private String applications;

    @Column(name = "imagePath", nullable =true, unique = false)
    private String imagePath;

    @Column(name = "testDescription", nullable =true, unique = false)
    private String testDescription;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTest() { return test; }
    public void setTest(String test) { this.test = test; }

    public String getTestAlsoKnownAs() { return testAlsoKnownAs; }
    public void setTestAlsoKnownAs(String testAlsoKnownAs) { this.testAlsoKnownAs = test; }

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

    public String getMaterials() { return materials; }
    public void setMaterials(String materials) { this.materials = materials; }

    public String getApplications() { return applications; }
    public void setApplications(String applications) { this.applications = applications; }

    public String getDatabaseBelongsTo() {
        return databaseBelongsTo;
    }
    public void setDatabaseBelongsTo(String databaseBelongsTo) {
        this.databaseBelongsTo = databaseBelongsTo;
    }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getTestDescription() { return testDescription; }
    public void setTestDescription(String testDescription) { this.testDescription = testDescription; }

}
