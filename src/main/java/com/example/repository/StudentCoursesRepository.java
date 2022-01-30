package com.example.repository;

import com.example.model.Course;
import com.example.model.CourseStudent;
import com.example.model.CourseStudentKey;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCoursesRepository extends JpaRepository<CourseStudent, CourseStudentKey> {

    @Transactional
    @Modifying
    void deleteCourseStudentByStudent_Id(Long studentId);

    @Transactional
    @Modifying
    void deleteCourseStudentByStudent_IdAndCourse_Id(Long studentId,Long courseId);

    @Query("SELECT COUNT(cs) FROM CourseStudent cs WHERE cs.course = ?1")
    Integer getNumberStudentOfOneCourse(Course course);

    @Query("SELECT cs.course FROM CourseStudent cs WHERE cs.student = ?1")
    List<Course> findAllCoursesByStudent(User user);

    @Query("SELECT cs.courseStudentKey FROM CourseStudent cs WHERE cs.student = ?1 AND cs.course = ?2")
    Optional<CourseStudentKey> findCourseStudentByCourseAndStudent(User student, Course course);


    @Query("SELECT cs.student FROM CourseStudent cs WHERE cs.course.id = ?1")
    List<User> findAllStudentsByCourseId(Long courseId);

    @Transactional
    void deleteCourseStudentByStudent(User user);

    CourseStudent getCourseStudentByStudent(User user);
}
