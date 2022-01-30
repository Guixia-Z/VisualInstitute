package com.example.service;

import com.example.model.Course;
import com.example.model.User;
import com.example.repository.CourseRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsersRepository usersRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Value("${spring.mail.username}")
    private String from;

    public String sendMail(String to, String subject, String text, Model model) {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Course> teacherCourseList = courseRepo.findCourseByUser(user);
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        model.addAttribute("user",usersRepo.findUserById(user.getId()).get());
        model.addAttribute("tcourse",teacherCourseList);
        model.addAttribute("success","You have send you email");
        return "user_info";
    }

    public void sendConfirmEmail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
