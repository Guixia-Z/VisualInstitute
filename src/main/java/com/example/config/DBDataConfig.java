package com.example.config;

import com.example.model.*;
import com.example.repository.CourseRepository;
import com.example.repository.FileDBRepository;
import com.example.repository.StudentCoursesRepository;
import com.example.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DBDataConfig {
    @Bean
    CommandLineRunner commandLineRunner(UsersRepository userRepo, CourseRepository courseRepo, StudentCoursesRepository studentCoursesRepo, FileDBRepository fileDBRepository) {
        if(userRepo.findAll().isEmpty() && courseRepo.findAll().isEmpty()) {
            return args -> {
                File file = (new File("src/main/resources/static/default.png"));

                byte[] fileContent = Files.readAllBytes(file.toPath());
                FileDB img = new FileDB("default.png", "image/png", fileContent);
                fileDBRepository.save(img);

                User phil = new User(
                        "phil@email.com",
                        Role.ADMIN,
                        "Phil",
                        "Arceno",
                        "123-456-7890",
                        new BCryptPasswordEncoder().encode("Test123!"),
                        "123 Address",
                        img
                );
                User guixia = new User(
                        "guixia@email.com",
                        Role.ADMIN,
                        "Guixia",
                        "Zhao",
                        "123-456-7890",
                        new BCryptPasswordEncoder().encode("Test123!"),
                        "123 Address",
                        img
                );
                User lily = new User(
                        "Lily@email.com",
                        Role.TEACHER,
                        "Lily",
                        "Wu",
                        "123-456-7890",
                        new BCryptPasswordEncoder().encode("Test123!"),
                        "123 Address",
                        img
                );
                User emma = new User(
                        "Emma@email.com",
                        Role.TEACHER,
                        "Emma",
                        "Lee",
                        "123-456-7890",
                        new BCryptPasswordEncoder().encode("Test123!"),
                        "123 Address",
                        img
                );
                User amy = new User(
                        "Amy@email.com",
                        Role.STUDENT,
                        "Amy",
                        "Zhu",
                        "123-456-7890",
                        new BCryptPasswordEncoder().encode("Test123!"),
                        "123 Address",
                        img
                );
                User marc = new User(
                        "Marc@email.com",
                        Role.STUDENT,
                        "Marc",
                        "Wang",
                        "123-456-7890",
                        new BCryptPasswordEncoder().encode("Test123!"),
                        "123 Address",
                        img
                );
                User lucy = new User(
                        "lucy@email.com",
                        Role.STUDENT,
                        "Lucy",
                        "Smith",
                        "123-456-7890",
                        new BCryptPasswordEncoder().encode("Test123!"),
                        "123 Address",
                        img
                );
                User xixi = new User(
                        "brightway258@163.com", //a real email address
                        Role.STUDENT,
                        "Xixi",
                        "Zhao",
                        "123-456-7890",
                        new BCryptPasswordEncoder().encode("Test123!"),
                        "123 Address",
                        img
                );
                List<User> userList = List.of(phil, guixia, lily, emma, amy, marc, lucy,xixi);


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Course guitar = new Course("Guitar",
                        "Guitar for beginner",
                        sdf.parse("2022-03-05"),
                        "room-112",
                        22,
                        emma);
                Course piano = new Course("Piano",
                        "Piano for intermediate",
                        sdf.parse("2022-01-22"),
                        "room-306",
                        15,
                        emma);
                Course oboe = new Course("Oboe",
                        "Oboe for intermediate",
                        sdf.parse("2022-01-26"),
                        "room-331",
                        10,
                        lily);
                Course oilPainting = new Course("Oil Painting",
                        "Oil Painting for advance",
                        sdf.parse("2022-02-16"),
                        "room-227",
                        10);
                Course sketch = new Course("Sketch",
                        "Sketch for advance",
                        sdf.parse("2021-11-16"),
                        "room-221",
                        10,
                        emma);
                Course jewelryDesign = new Course("Jewelry Design",
                        "Jewelry Design for advance",
                        sdf.parse("2021-10-22"),
                        "room-125",
                        10,
                        lily);

                List<Course> coursesList = List.of(guitar, piano, oboe, oilPainting,sketch,jewelryDesign);
                userRepo.saveAll(
                        userList
                );

                courseRepo.saveAll(
                    coursesList
                );

                List<CourseStudent> csList = new ArrayList<>();
                csList.add(addStudentToCourse(amy, oboe));
                csList.add(addStudentToCourse(amy, piano));
                csList.add(addStudentToCourse(amy, oilPainting));
                csList.add(addStudentToCourse(marc, oboe));
                csList.add(addStudentToCourse(marc, guitar));
                csList.add(addStudentToCourse(marc, oilPainting));
                csList.add(addStudentToCourse(lucy, oboe));
                csList.add(addStudentToCourse(lucy, piano));
                csList.add(addStudentToCourse(lucy, guitar));
                csList.add(addStudentToCourse(lucy, oilPainting));
                csList.add(addStudentToCourse(lucy, sketch));
                csList.add(addStudentToCourse(lucy, jewelryDesign));

//                 must be done after user and course tables were created because their ID's
//                 will be null, and set only after put into the DB
                studentCoursesRepo.saveAll(
                        csList
                );
            };
        }
        return args -> {};
    }

    private CourseStudent addStudentToCourse (User user, Course course) {
        return new CourseStudent(new CourseStudentKey(user.getId(), course.getId()),user, course);
    }
}
