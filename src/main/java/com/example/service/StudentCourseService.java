package com.example.service;

import com.example.model.Course;
import com.example.model.CourseStudent;
import com.example.model.CourseStudentKey;
import com.example.model.User;
import com.example.repository.CourseRepository;
import com.example.repository.StudentCoursesRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentCourseService {

    @Autowired
    private StudentCoursesRepository studentCoursesRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private CourseRepository courseRepo;

    public String selectCourse(Long courseId, Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Course course = courseRepo.findById(courseId).get();//validate if it is present
        Optional<CourseStudentKey> key = studentCoursesRepo.findCourseStudentByCourseAndStudent(user,course);
        List<Course> list = courseRepo.findAll();
        List<Integer> numList = new ArrayList<>();
        for(Course course1 : list){
            numList.add(course1.getCapacity() - studentCoursesRepo.getNumberStudentOfOneCourse(course1));
        }
        model.addAttribute("courseList",list);
        model.addAttribute("numList",numList);
        if(key.isPresent()){
            model.addAttribute("err","You have already taken this course");
            return "course_list";
        }
        CourseStudent courseStudent = new CourseStudent(new CourseStudentKey(user.getId(),course.getId()), user, course);
        studentCoursesRepo.save(courseStudent);
        emailService.sendConfirmEmail(user.getEmail(),"Confirm to take course: " + course.getCourseName(),"Thank you for your registering this course: "+course.getCourseName()+"\nStart Date: " +course.getStartDate() +"\nPlace: "+course.getCourseRoom());
        model.addAttribute("success","take this course successfully");
        return "course_list";
    }

    public String deleteCourseByStudent(Long courseId,Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        studentCoursesRepo.deleteCourseStudentByStudent_IdAndCourse_Id(user.getId(),courseId);
        model.addAttribute("user",user);
        model.addAttribute("course",studentCoursesRepo.findAllCoursesByStudent(user));
        model.addAttribute("success", "You have deleted the course");
        return "user_info";
    }

    public String findAllStudentsByCourse(Long courseId,Model model){
        List<User> studentList = studentCoursesRepo.findAllStudentsByCourseId(courseId);
        model.addAttribute("student",studentList);
        model.addAttribute("courseId",courseId);
        return "student_list";
    }

    public String deleteStudentsFromCourse(Long studentId, Long courseId){
        Course course = courseRepo.getCourseById(courseId).get();
        studentCoursesRepo.deleteCourseStudentByStudent_IdAndCourse_Id(studentId,course.getId());
        return "redirect:/studentList?courseId=" + courseId.toString();
    }

    public Optional<CourseStudentKey> findStudentByCourse(User student, Course course){
       return studentCoursesRepo.findCourseStudentByCourseAndStudent(student,course);
    }

    public void addStudentInCourse(CourseStudent courseStudent){
        studentCoursesRepo.save(courseStudent);
    }
}
