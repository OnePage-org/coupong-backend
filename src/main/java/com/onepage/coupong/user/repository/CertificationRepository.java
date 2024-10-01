package com.onepage.coupong.user.repository;

import com.onepage.coupong.user.domain.Certification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, String> {

    /* 해당 username을 가지는 Certification을 반환해준다. */
    Certification findCertificationByUsername(String username);

    /* 해당 username을 가지는 Certification을 삭제해준다. */
    @Transactional
    void deleteByUsername(String username);
}
