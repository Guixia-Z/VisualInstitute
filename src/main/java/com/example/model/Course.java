package com.example.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name can't be blank")
    @Pattern(regexp = "[a-zA-Z0-9 !@#$%^&*()_+=\\-`~{}\\[\\]<>,./?;':\"|\\\\]+",
            message = "Course name can only contain characters, numbers, and/or special characters")
    @Column(name = "course_name")
    @Length(min = 4, max = 20, message = "Course name must be between 4-20 characters long")
    private String courseName;

    @NotBlank(message = "Description can't be blank")
    @NotEmpty(message = "Description must not be empty")
    @Length(min = 5, max = 200, message = "Must be between 5-200 characters")
    private String description;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date can't be empty")
    private Date startDate;

    @Pattern(regexp = "[room]{4}-[0-9]{3}",
            message = "Course room must be formatted appropriately. (Ex. room-123)")
    @Column(name = "course_room",length = 20)
    private String courseRoom;


    @Range(min = 10, max = 50, message = "Capacity must be between 10-50")
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    private User user;

//    @Column(length = 200)
//    private String image;

    @OneToMany(mappedBy = "course")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<CourseStudent> UsersTakingCourse;

    public Course(String courseName,String description, Date startDate, String courseRoom, int capacity){
        this.courseName = courseName;
        this.description = description;
        this.startDate = startDate;
        this.courseRoom = courseRoom;
        this.capacity = capacity;
    }

    public Course(String courseName,String description, Date startDate, String courseRoom, int capacity,User teacher){
        this.courseName = courseName;
        this.description = description;
        this.startDate = startDate;
        this.courseRoom = courseRoom;
        this.capacity = capacity;
        this.user = teacher;
    }
}
