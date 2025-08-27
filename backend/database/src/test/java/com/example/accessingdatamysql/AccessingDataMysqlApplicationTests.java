package com.example.accessingdatamysql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccessingDataMysqlApplicationTests {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_GeotechnicalTests")
            .withUsername("myuser")
            .withPassword("secret");

    @Autowired
    private GeotechnicalEntryRepository geotechnicalEntryRepository;

    @Autowired
    private AggregateUserRepository AggregateUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    void testInsertEntry() {
        GeotechnicalEntry entry = new GeotechnicalEntry();
        entry.setTest("Soil Analysis");
        entry.setGroup("Geotech_Group_Test_Default");
        entry.setSymbol("TEST");
        entry.setParameters("Moisture Content, Density");
        entry.setTestMethod("GENERICTEST1243");
        entry.setAlt1("Alt Test Default");
        entry.setSampleType("Undisturbed");
        entry.setFieldSampleMass("500kg");

        GeotechnicalEntry savedEntry = geotechnicalEntryRepository.save(entry);

        assertThat(savedEntry).isNotNull();
        assertThat(savedEntry.getId()).isNotNull();
        assertThat(savedEntry.getSymbol()).isEqualTo("TEST");
    }

    @Test
    @Order(2)
    void testGroup() {
        List<GeotechnicalEntry> entries = StreamSupport.stream(geotechnicalEntryRepository.findAll().spliterator(), false)
                                        .collect(Collectors.toList());
        GeotechnicalEntry savedEntry = entries.get(0);
        assertThat(savedEntry.getGroup()).isEqualTo("Geotech_Group_Test_Default");
    }

    @Test
    @Order(3)
    void testDeleteEntry() {
        List<GeotechnicalEntry> entries = StreamSupport.stream(geotechnicalEntryRepository.findAll().spliterator(), false)
                                        .collect(Collectors.toList());
        GeotechnicalEntry entryToDelete = entries.get(0);
        geotechnicalEntryRepository.deleteById(entryToDelete.getId());

        Optional<GeotechnicalEntry> deletedEntry = geotechnicalEntryRepository.findById(entryToDelete.getId());
        assertThat(deletedEntry).isEmpty();
    }

    @Test
    @Order(4)
    void testInsertRockEntry() {
        AggregateUser entry = new AggregateUser();
        entry.setGroup("Rock_Group_Test_Default");
        entry.setSymbol("ROCK1");
        entry.setParameters("Porosity, Density");
        entry.setTestMethod("ROCK_TEST_5678");
        entry.setSampleType("Core");
        entry.setFieldSampleMass("1000kg");
        entry.setSpecimenType("Granite");
        entry.setSpecimenMass("2kg");
        entry.setSpecimenNumbers("3");
        entry.setSpecimenMaxGrainSize("5mm");
        entry.setSpecimenMaxGrainFraction("30%");
        entry.setSchedulingNotes("Standard rock test");

        AggregateUser savedEntry = AggregateUserRepository.save(entry);

        assertThat(savedEntry).isNotNull();
        assertThat(savedEntry.getId()).isNotNull();
        assertThat(savedEntry.getSymbol()).isEqualTo("ROCK1");
    }

    @Test
    @Order(5)
    void testRockGroup() {
        List<AggregateUser> entries = StreamSupport.stream(AggregateUserRepository.findAll().spliterator(), false)
                                        .collect(Collectors.toList());
        AggregateUser savedEntry = entries.get(0);
        assertThat(savedEntry.getGroup()).isEqualTo("Rock_Group_Test_Default");
    }

    @Test
    @Order(6)
    void testDeleteRockEntry() {
        List<AggregateUser> entries = StreamSupport.stream(AggregateUserRepository.findAll().spliterator(), false)
                                        .collect(Collectors.toList());
        AggregateUser entryToDelete = entries.get(0);
        AggregateUserRepository.deleteById(entryToDelete.getId());

        Optional<AggregateUser> deletedEntry = AggregateUserRepository.findById(entryToDelete.getId());
        assertThat(deletedEntry).isEmpty();
    }

    @Test
    @Order(7)
    void testSaveUser() {
        User user = new User();
        user.setUsername("User1");
        user.setPassword("Password");
        user.setRole(User.Role.USER);

        User savedUser = userService.saveUser(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(passwordEncoder.matches("Password", savedUser.getPassword())).isTrue();
        assertThat(savedUser.getUsername()).isEqualTo("User1");
        assertThat(savedUser.getRole()).isEqualTo(User.Role.USER);
    }

    @Test
    @Order(8)
    void testFindByUsername() {
        String username = "Admin";
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("Password4924808!"));
        user.setRole(User.Role.ADMIN);

        userRepository.save(user);

        Optional<User> foundUser = userService.findByUsername(username);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(username);
        assertThat(foundUser.get().getRole()).isEqualTo(User.Role.ADMIN);
    }

    @Test
    @Order(9)
    void testCreateUser() {
        String username = "User";
        String rawPassword = "pass480298402";
        User.Role role = User.Role.USER;

        User createdUser = userService.createUser(username, rawPassword, role);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(username);
        assertThat(passwordEncoder.matches(rawPassword, createdUser.getPassword())).isTrue();
        assertThat(createdUser.getRole()).isEqualTo(role);
    }

    @Test
    @Order(10)
    void testDeleteUser() {
        User user = new User();
        user.setUsername("UserToBeDeleted");
        user.setPassword(passwordEncoder.encode("pass5908202"));
        user.setRole(User.Role.USER);

        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser).isEmpty();
    }
    
    @Test
    @Order(14)
    void testGetAllUsers() {
        List<GeotechnicalEntry> users = (List<GeotechnicalEntry>) geotechnicalEntryRepository.findAll();
        assertThat(users).isNotEmpty();
    }

    @Test
    @Order(11)
    void testGetUserById() {
        GeotechnicalEntry entry = new GeotechnicalEntry();
        entry.setTest("Test");
        entry.setGroup("Test Group");
        entry.setSymbol("TEST");
        entry.setParameters("param1, param2");
        entry.setTestMethod("method");
        entry.setAlt1("Alt");
        entry.setSampleType("type");
        entry.setFieldSampleMass("100kg");
        geotechnicalEntryRepository.save(entry);

        Optional<GeotechnicalEntry> userOpt = geotechnicalEntryRepository.findById(entry.getId());
        assertThat(userOpt).isPresent();
    }

    @Test
    @Order(12)
    void testGetUserByGroup() {
        GeotechnicalEntry entry = new GeotechnicalEntry();
        entry.setTest("BasicTest1");
        entry.setGroup("Electrochemical");
        entry.setSymbol("EXAMPLESYMBOL123");
        entry.setParameters("param1, param2");
        entry.setTestMethod("EXAMPLETESTMETHOD123");
        entry.setAlt1("Alt");
        entry.setSampleType("type");
        entry.setFieldSampleMass("100kg");
        geotechnicalEntryRepository.save(entry);

        List<GeotechnicalEntry> users = geotechnicalEntryRepository.findByMyGroupContaining("Electrochemical");
        assertThat(users).isNotEmpty();
    }

    @Test
    @Order(13)
    void testGetUsersByTestMethod() {
        GeotechnicalEntry entry = new GeotechnicalEntry();
        entry.setTest("BasicTest2");
        entry.setGroup("Group");
        entry.setSymbol("SYMBOL");
        entry.setParameters("param1, param2");
        entry.setTestMethod("BS EN ISO 17892-1");
        entry.setAlt1("Alt");
        entry.setSampleType("type");
        entry.setFieldSampleMass("100kg");
        geotechnicalEntryRepository.save(entry);

        List<GeotechnicalEntry> users = geotechnicalEntryRepository.findByTestMethodContaining("BS EN ISO 17892-1");
        assertThat(users).isNotEmpty();
    }
}

