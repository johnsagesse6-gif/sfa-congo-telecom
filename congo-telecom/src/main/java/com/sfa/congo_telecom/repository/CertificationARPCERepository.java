package com.sfa.congo_telecom.repository;

import com.sfa.congo_telecom.model.CertificationARPCE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationARPCERepository extends JpaRepository<CertificationARPCE, Long> {
}