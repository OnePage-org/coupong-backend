package com.onepage.coupong.persistence.user;

import com.onepage.coupong.jpa.user.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, String> {

    /* 해당 username을 가지는 Certification을 반환해준다. */
    Optional<Certification> findByUsername(String username);

}
