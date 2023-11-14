package com.hospital.Repository;

import com.hospital.Domain.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, String>{
    boolean existsByCode(String code);
    Nurse findByCode(String code);
}
