package com.nhan.sp2.repository;

import com.nhan.sp2.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u where u.status = 'ACTIVE' and " +
            "( lower(u.firstName) like :keyword " +
            "or lower(u.lastName) like :keyword " +
            "or lower(u.email) like :keyword " +
            "or lower(u.phone) like :keyword " +
            "or lower(u.username) like :keyword )")
    Page<User> searchByKeyword(String keyword, Pageable pageable);

    UserDetails findByUsername(String s);
}
