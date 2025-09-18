package com.booleanuk;

import com.booleanuk.cohorts.models.*;
import com.booleanuk.cohorts.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CohortRepository cohortRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SpecialisationRepository specialisationRepository;
    @Autowired
    PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        Role teacherRole;
        if (!this.roleRepository.existsByName(ERole.ROLE_TEACHER)) {
            teacherRole = this.roleRepository.save(new Role(ERole.ROLE_TEACHER));
        } else {
            teacherRole = this.roleRepository.findByName(ERole.ROLE_TEACHER).orElse(null);
        }
        Set<Role> teacherRoles = new HashSet<>();
        teacherRoles.add(teacherRole);
        Role studentRole;
        if (!this.roleRepository.existsByName(ERole.ROLE_STUDENT)) {
            studentRole = this.roleRepository.save(new Role(ERole.ROLE_STUDENT));
        } else {
            studentRole = this.roleRepository.findByName(ERole.ROLE_STUDENT).orElse(null);
        }
        Set<Role> studentRoles = new HashSet<>();
        studentRoles.add(studentRole);
        if (!this.roleRepository.existsByName(ERole.ROLE_ADMIN)) {
            this.roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
        // Create some specialisations
        Specialisation specialisation;
        if (!this.specialisationRepository.existsById(1)) {
            specialisation = this.specialisationRepository.save(new Specialisation("Software Development"));
        } else {
            specialisation = this.specialisationRepository.findById(1).orElse(null);
        }
        if (!this.specialisationRepository.existsById(2)) {
            specialisationRepository.save(new Specialisation("Front-end Development"));
        }
        if (!this.specialisationRepository.existsById(3)) {
            specialisationRepository.save(new Specialisation("Data Analytics"));
        }
        // Create a cohort.
        Cohort cohort;
        if (!this.cohortRepository.existsById(1)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            cohort = this.cohortRepository.save(new Cohort("Cohort 2", LocalDate.parse("04-08-2025", formatter) , LocalDate.parse("22-12-2025", formatter) ,specialisation));
        } else {
            cohort = this.cohortRepository.findById(1).orElse(null);
        }
        // Create some users
        User studentUser;
        if (!this.userRepository.existsByEmail("student@test.com")) {
            studentUser = new User("student@test.com", this.encoder.encode("Testpassword1!"), cohort);
            studentUser.setRoles(studentRoles);
            studentUser = this.userRepository.save(studentUser);
        } else {
            studentUser = this.userRepository.findByEmail("student@test.com").orElse(null);
        }
        Profile studentProfile;
        if (!this.profileRepository.existsByUser(studentUser)) {
            studentProfile = this.profileRepository.save(new Profile(studentUser, "Joe", "Bloggs", "+31612345678", "Hello world!", "student1"));
        } else {
            studentProfile = this.profileRepository.findByUser(studentUser).orElse(null);
        }

        User teacherUser;
        if (!this.userRepository.existsByEmail("dave@email.com")) {
            teacherUser = new User("dave@email.com", this.encoder.encode("password"));
            teacherUser.setRoles(teacherRoles);
            teacherUser.setSpecialisation(specialisation);
            teacherUser = this.userRepository.save(teacherUser);
        } else {
            teacherUser = this.userRepository.findByEmail("dave@email.com").orElse(null);
        }
        Profile teacherProfile;
        if (!this.profileRepository.existsByUser(teacherUser)) {
            teacherProfile = this.profileRepository.save(new Profile(teacherUser, "Rick", "Sanchez","+31612345687", "Hello there!", "teacher1"));
        } else {
            teacherProfile = this.profileRepository.findByUser(teacherUser).orElse(null);
        }

        if (!this.postRepository.existsById(1)) {
            this.postRepository.save(new Post(studentUser, "My first post!"));
        }
        if (!this.postRepository.existsById(2)) {
            this.postRepository.save(new Post(teacherUser, "Hello, students!"));
        }
    }
}
