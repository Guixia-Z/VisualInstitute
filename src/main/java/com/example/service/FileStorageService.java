package com.example.service;

import com.example.model.FileDB;
import com.example.model.User;
import com.example.repository.FileDBRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    private List<String> validTypes = new ArrayList<>(List.of("image/gif", "image/jpeg", "image/png", "image/svg+xml", "image/webp"));

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

        return fileDBRepository.save(FileDB);
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    public void deletePicture(FileDB image) {
        if (image.getName().equals("default.png")) return;
        fileDBRepository.delete(image);
    }

    public String userChangePicture(MultipartFile file, Model model) {
        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!validTypes.contains(file.getContentType())) {
            model.addAttribute("error", "File must be a .png, .jpg, .jpeg, .svg, .gif, .webp");
            return "profile_image";
        }
            try {
                // upload the file, update the users image, save the old image to delete later,
                FileDB uploadedFile = store(file);
                usersRepository.updateImage(uploadedFile, thisUser.getId());
                FileDB oldImage = thisUser.getImage();

                // and set our saved authenticated user's image to the uploaded file
                thisUser.setImage(uploadedFile);
                deletePicture(oldImage);
            } catch (Exception e) {
                System.out.println(e);
                model.addAttribute("error", "An unexpected error occurred");
                return "profile_image";
            }

        return "redirect:/profile-page";
    }

    public String adminChangePicture(Long id, Model model, MultipartFile file) {
        User user = usersRepository.getById(id);

        if (!validTypes.contains(file.getContentType())) {
            model.addAttribute("error", "File must be a .png, .jpg, .jpeg, .svg, .gif, .webp");
            return "admin/profile_image";
        }
        try {
            // upload the file, update the users image, save the old image to delete later,
            FileDB uploadedFile = store(file);
            FileDB oldImage = user.getImage();
            usersRepository.updateImage(uploadedFile, user.getId());
            deletePicture(oldImage);
        } catch (Exception e) {
            System.out.println(e);
            model.addAttribute("error", "An unexpected error occurred");
            return "profile_image";
        }

        return "redirect:/admin/userList";
    }
}