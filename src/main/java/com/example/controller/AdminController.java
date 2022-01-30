package com.example.controller;

import com.example.model.Course;
import com.example.model.User;
import com.example.model.UserForm;
import com.example.model.CourseStudent;
import com.example.model.CourseStudentKey;
import com.example.service.CourseService;
import com.example.service.FileStorageService;
import com.example.service.StudentCourseService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    UserService userService;

    @Autowired
    StudentCourseService studentCourseService;

    @Autowired
    CourseService courseService;

    @Autowired
    private FileStorageService fileStorageService;

//  ===================USER CRUD=====================

    @GetMapping("/admin/userList")
    public ModelAndView getUserList(User user){
        return userService.getAllUsers();
    }

    @GetMapping("/admin/image/{id}")
    public String adminEditUserPicturePage(@PathVariable("id") Long id, Model model){
        return userService.editUsersImagePage(id, model);
    }

    @PostMapping("/admin/image/{id}")
    public String adminEditUserPicture(@PathVariable("id") Long id, Model model, @RequestParam("image") MultipartFile image){
        return fileStorageService.adminChangePicture(id, model, image);
    }


    @GetMapping("/admin/updateUser")
    public String showUpdateForm(@RequestParam Long userId, Model model){
        return userService.findUserById(userId, model, true);
    }

    @PostMapping("/admin/updateUser")
    public String saveUser(@Valid UserForm userForm, BindingResult result, User user, Model model, @RequestParam Long userId){
        return userService.updateUser(user, userForm, result, userId, true);
    }

    @GetMapping("/admin/addUser")
    public ModelAndView showAddForm(){
        return userService.showAddUserForm();
    }

    @PostMapping("/admin/addUser")
    public String addUser(@Valid UserForm userForm, BindingResult result, User user){
        return userService.addUser(userForm,result, true);
    }

    @GetMapping("admin/deleteUser")
    public String deleteUser(@RequestParam Long userId){
        return userService.deleteUser(userId);
    }

//  ======================COURSE CRUD==========================

    @GetMapping("/admin/course-list")
    public String adminCoursePage(Model model, Course course) {
        model.addAttribute("courseList", courseService.getAllCoursesAdmin());
        model.addAttribute("course", new Course());
        return "admin/course_list";
    }

    @GetMapping("/admin/selectTeacher/{id}")
    public String adminSelectTeacher(Model model,@PathVariable("id") Long id){
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
        }
         model.addAttribute("teacherList",userService.findAllTeachers());
         return "admin/choose_teacher";
    }

    @PostMapping("/admin/selectTeacher/{id}")
    public String adminAddTeacher(@RequestParam("teacher") Long teacherId,@PathVariable("id") Long courseId,Model model){
        courseService.addTeacherInCourse(teacherId,courseId);
        model.addAttribute("success","Add a new teacher in this course");
        return "redirect:/admin/course-list";
    }

    @GetMapping("/admin/course/{id}")
    public String adminCoursePage(Model model, @PathVariable("id") Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
        }
        return "admin/update_course";
    }

    @GetMapping("/admin/deleteStudentFormCourse")
    public String deleteStudent(@RequestParam("studentId") Long studentId, @RequestParam("courseId") Long courseId){
        return studentCourseService.deleteStudentsFromCourse(studentId, courseId);
    }

    @GetMapping("/admin/add-course")
    public String adminAddCoursePage(Model model) {
        model.addAttribute("course", new Course());
        return "admin/add_course";
    }

    @PostMapping("/admin/add-course")
    public String adminAddCourse(@Valid Course course, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/add_course";
        }
        courseService.addCourse(course);
        return "redirect:/admin/course-list/";
    }
    @PostMapping("/admin/course/{id}")
    public String adminCourseUpdate(@Valid Course course, BindingResult result, @PathVariable("id") Long id) {
        if (result.hasErrors()) {
            return "admin/update_course";
        }
        courseService.updateCourse(course, id);
        return "redirect:/admin/course-list/";
    }

    @PostMapping("/admin/delete-course")
    public String adminDeleteCourse(Model model, @RequestParam("id") Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/course-list";
    }

    @GetMapping("/admin/addStudentInCourse")
    public String addStudent(@RequestParam Long courseId, Model model){
        model.addAttribute("student",new User());
        model.addAttribute("course",courseService.getCourseById(courseId).get());
        return "admin/add_student";
    }

    @PostMapping("/admin/addStudentInCourse")
    public String addStudentInCourse(@RequestParam String email,@RequestParam("courseId") Long courseId,Model model){
        Optional<User> user = userService.findUserByEmail(email);
        Course course = courseService.getCourseById(courseId).get();
        if(!user.isPresent()){
            model.addAttribute("err","cannot find this email");
            return "admin/add_student";
        }
        CourseStudent courseStudent = new CourseStudent(new CourseStudentKey(user.get().getId(), course.getId()),user.get(),course);
        studentCourseService.addStudentInCourse(courseStudent);
        return "redirect:/studentList?courseId="+course.getId().toString();
    }

}
