package com.example.service;

import com.example.model.Course;
import com.example.model.User;
import com.example.repository.CourseRepository;
import com.example.repository.StudentCoursesRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    CourseRepository courseRepo;

    @Autowired
    StudentCoursesRepository studentCoursesRepo;

    public String getAllCourses(Model model){
        List<Course> list = courseRepo.findAll();
        List<Integer> numList = new ArrayList<>();
        for(Course course : list){
            numList.add(course.getCapacity() - studentCoursesRepo.getNumberStudentOfOneCourse(course));
        }
        model.addAttribute("courseList",list);
        model.addAttribute("numList",numList);
        return "course_list";
    }

    public List<Course> getAllCoursesAdmin(){
        return courseRepo.findAll();
    }

    public void deleteCourse(Long id) {
        courseRepo.deleteById(id);
    }

    public Optional<Course> getCourseById(Long id) {
        Optional<Course> course = courseRepo.getCourseById(id);
        return course;
    }

    public void updateCourse(Course course, Long id) {
        courseRepo.updateCourse(
                course.getCourseName(), course.getDescription(),
                course.getStartDate(), course.getCourseRoom(),
                course.getCapacity(), id
        );
    }

    public void addCourse(Course course) {
        courseRepo.save(course);
    }

    public ModelAndView getAllCoursesByStudent(User user){
        ModelAndView mav = new ModelAndView("user_info");
        List<Course> list = studentCoursesRepo.findAllCoursesByStudent(user);
        mav.addObject("course",list);
        return mav;
    }

    public void addTeacherInCourse(Long teacherId,Long courseId){
        Optional<User> teacher = usersRepo.findUserById(teacherId);
        if (teacher.isPresent()) {
            courseRepo.addTeacherInCourse(teacher.get(),courseId);
        }
    }

}
