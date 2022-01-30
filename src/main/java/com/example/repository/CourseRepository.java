package com.example.repository;

import com.example.model.Course;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    @Query("SELECT c FROM Course c WHERE c.id=?1")
    Optional<Course> getCourseById(Long aLong);

    @Transactional
    @Modifying
    @Query("UPDATE Course c SET c.courseName=?1, c.description=?2, c.startDate=?3, c.courseRoom=?4, c.capacity=?5 WHERE c.id=?6")
    void updateCourse(String courseName, String description, Date startDate, String courseRoom, Integer capacity, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Course c SET c.user = ?1 WHERE c.id=?2")
    void addTeacherInCourse(User user,Long courseId);

    List<Course> findCourseByUser(User user);
}
