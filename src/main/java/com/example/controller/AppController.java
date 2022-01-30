package com.example.controller;

import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserForm;
import com.example.service.CourseService;
import com.example.service.EmailService;
import com.example.service.StudentCourseService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppController {

    @Autowired
    CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private EmailService emailService;

    @GetMapping("")
    public String homePage(){
        return "index";
    }

    @GetMapping("/courseList")
    public String showAllCourses(Model model){
        return courseService.getAllCourses(model);
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        User newUser = new User();
        newUser.setRole(Role.STUDENT);
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("user", newUser);
        return "register";
    }

    @GetMapping("/profile-page")
    public String profilePage(Model model){
        return userService.getProfile(model);
    }

    @GetMapping("/modifyProfile")
    public String showProfilePage(Model model){
        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findUserById(thisUser.getId(), model, false);
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin/admin";
    }

    @GetMapping("/student")
    public String studentPage(){
        return "student/student";
    }


    @GetMapping("/deleteCourse")
    public String removeCourseByStudent(@RequestParam Long courseId,Model model){
          return studentCourseService.deleteCourseByStudent(courseId,model);
    }

    @GetMapping("/studentList")
    public String findAllStudents(@RequestParam Long courseId,Model model){
        return studentCourseService.findAllStudentsByCourse(courseId,model);
    }

    @GetMapping("/email")
    public String sendEmail(Model model){
        return emailService.sendMail("a106907278@gmail.com","Request not to take the course","don't take the course",model);
    }
}
