package com.onepage.coupong.sign.repository;

import com.onepage.coupong.sign.entity.Certification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, String> {

    Certification findCertificationByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
}
