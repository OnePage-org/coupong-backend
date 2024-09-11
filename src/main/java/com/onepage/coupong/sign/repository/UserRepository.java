package com.onepage.coupong.sign.repository;

import com.onepage.coupong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /* 해당 username을 가진 User를 반환해준다. */
    User findUserByUsername(String username);

    /* 해당 username을 가진 User가 있는지 결과를 반환해준다. */
    boolean existsByUsername(String username);

}
