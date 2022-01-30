package com.example.controller;

import com.example.model.User;
import com.example.model.UserForm;
import com.example.service.CourseService;
import com.example.service.FileStorageService;
import com.example.service.StudentCourseService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    private StudentCourseService studentCourseService;
    @Autowired
    private FileStorageService storageService;


    @PostMapping("/register")
    public String saveUser(@Valid UserForm userForm, BindingResult result, User user){
        return userService.addUser(userForm, result, false);
    }

    @PostMapping("/modifyProfile")
    public String modifyUser(@Valid UserForm userForm, BindingResult result, User user){
        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.updateUser(user, userForm, result, thisUser.getId(), false);
    }

    @PostMapping("/courseList")
    public String takeCourse(@RequestParam Long courseId, Model model){
        return studentCourseService.selectCourse(courseId,model);
    }

    @GetMapping("/profile-page/image")
    public String profilePicturePage(Model model) {
        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("image", thisUser.getImage());
        return "profile_image";
    }

    @PostMapping("/profile-page/image")
    public String changeProfilePicture(Model model, @RequestParam("image") MultipartFile image) {
        return storageService.userChangePicture(image, model);
    }

//    ========== TEACHERS ===========
}
