package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudent implements Serializable {

    @EmbeddedId
    @Column(unique = true)
    private CourseStudentKey courseStudentKey;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseStudent(User user,Course course){
        this.student = user;
        this.course = course;
    }
}
