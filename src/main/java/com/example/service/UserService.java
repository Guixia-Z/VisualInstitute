package com.example.service;

import com.example.model.*;
import com.example.repository.CourseRepository;
import com.example.repository.FileDBRepository;
import com.example.repository.StudentCoursesRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    CourseRepository courseRepo;

    @Autowired
    private StudentCoursesRepository studentCoursesRepo;

    @Autowired
    private FileDBRepository fileDBRepository;

    public ModelAndView getAllUsers() {
        ModelAndView mav = new ModelAndView("admin/user_list");
        List<User> list = usersRepository.findAll();
        mav.addObject("userList", list);
        return mav;
    }

    public List<User> findAllTeachers(){
        return usersRepository.findAllByRole(Role.TEACHER);
    }

    public String findUserById(Long userId, Model model, boolean isAdmin) {
        Optional<User> user = usersRepository.findUserById(userId);
        if (user.isPresent()) {
            model.addAttribute("userForm", new UserForm());
            model.addAttribute("user", user.get());
        }
        if (isAdmin) {
            return "admin/update_user";
        } else {
            return "profile";
        }
    }

    public Optional<User> findUserByEmail(String email){
        return usersRepository.findUserByEmail(email);
    }

    public String getProfile(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Course> teacherCourseList = courseRepo.findCourseByUser(user);
        List<Course> list = studentCoursesRepo.findAllCoursesByStudent(user);
        model.addAttribute("user",usersRepository.findUserById(user.getId()).get());
        model.addAttribute("course",list);
        model.addAttribute("tcourse",teacherCourseList);
        return "user_info";
    }

    public String updateUser(User user, UserForm userForm, BindingResult result, Long id, boolean isAdmin) {
        user.setId(id);
        validateUserForm(user, userForm, result);
        if (result.hasErrors()) {
            if (isAdmin) {
                return "admin/update_user";
            } else {
                return "profile";
            }
        }

        if (userForm.getPassword().isEmpty() && userForm.getPasswordConfirm().isEmpty()) {
            //no password change
            usersRepository.updateUserWithoutPassword(user.getEmail(), user.getRole(), user.getFirstName(), user.getLastName(), user.getPhone(), user.getAddress(), id);
        } else {
            //password was changed
            user.setPassword(new BCryptPasswordEncoder().encode(userForm.getPassword()));
            usersRepository.updateUser(user.getEmail(), user.getRole(), user.getFirstName(), user.getLastName(), user.getPhone(), user.getAddress(), user.getPassword(), id);
        }
        if (isAdmin) {
            return "redirect:/admin/userList";
        } else {
            return "redirect:/profile-page";
        }
    }


    public ModelAndView showAddUserForm() {
        ModelAndView mav = new ModelAndView("admin/add_user");
        User newUser = new User();
        mav.addObject("user", newUser);
        mav.addObject("userForm", new UserForm());
        return mav;
    }

    public String addUser(UserForm userForm, BindingResult result, boolean isAdmin) {
        FileDB defaultImg = fileDBRepository.getDefaultImage();

        User user = new User(
                userForm.getEmail(),
                userForm.getRole(),
                userForm.getFirstName(),
                userForm.getLastName(),
                userForm.getPhone(),
                new BCryptPasswordEncoder().encode(userForm.getPassword()),
                userForm.getAddress(),
                defaultImg
        );

        validateUserForm(user, userForm, result, true);
        if (result.hasErrors()) {
            if (isAdmin) {
                return "admin/add_user";
            } else {
                return "register";
            }
        }
        usersRepository.save(user);
        if (isAdmin) {
            return "redirect:/admin/userList";
        } else {
            return "redirect:/login";
        }
    }


    public String deleteUser(Long userId) {
        studentCoursesRepo.deleteCourseStudentByStudent_Id(userId);
        usersRepository.deleteById(userId);
        return "redirect:/admin/userList";
    }

    private void validateUserForm(User user, UserForm userForm, BindingResult result) {
        validateUserForm(user,userForm,result,false);
    }

    private void validateUserForm(User user, UserForm userForm, BindingResult result, @Nullable boolean creatingUser) {
        Optional<User> userFound = usersRepository.findUserByEmail(userForm.getEmail());
        //email exists error if found, unless the id matches our user for update
        if (userFound.isPresent() && user.getId() != userFound.get().getId()) {
            result.rejectValue("email", "email", "Email already exists!");
        }
        //make sure passwords are matching
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            result.rejectValue("password", "password", "Passwords are not matching");
        }
        //password must be greater than 6, unless empty (to leave unchanged) OR creating a user
        if (userForm.getPassword().length() < 6 && (!userForm.getPassword().isEmpty() || creatingUser)) {
            result.rejectValue("password", "password", "Password must be 6-64 characters long");
        }
    }

    public String editUsersImagePage(Long id, Model model) {
        User userToEdit = usersRepository.getById(id);
        model.addAttribute("image", userToEdit.getImage());
        model.addAttribute("id", id);
        return "admin/profile_image";
    }
}
