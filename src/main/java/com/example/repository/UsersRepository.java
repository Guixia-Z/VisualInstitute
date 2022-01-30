package com.example.repository;

import com.example.model.FileDB;
import com.example.model.Role;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email=?1")
    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.firstName=?1 AND u.lastName=?2")
    Optional<User> findUserByName(String firstName, String lastName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email=?1,u.role=?2,u.firstName=?3,u.lastName=?4,u.phone=?5,u.address=?6 WHERE u.id=?7")
    void updateUserWithoutPassword(String email, Role role, String firstName, String lastName, String phone, String address, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email=?1,u.role=?2,u.firstName=?3,u.lastName=?4,u.phone=?5,u.address=?6,u.password=?7 WHERE u.id=?8")
    void updateUser(String email, Role role, String firstName, String lastName, String phone, String address, String password, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email=?1,u.phone=?2,u.address=?3 WHERE u.id=?4")
    void modifyUser(String email,String phone, String address, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password=?1 WHERE u.id=?2")
    void changePassword(String password, Long id);

    @Query("SELECT u FROM User u WHERE u.id=?1")
    Optional<User> findUserById(Long id);

    @Query("SELECT u FROM User u WHERE u.role=?1")
    List<User> findAllByRole(Role TEACHER);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.image=?1 WHERE u.id=?2")
    void updateImage(FileDB uploadedFile, Long id);
}
