package com.hospital.Repository;

import com.hospital.Domain.Nurse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;

@DataJpaTest
public class NurseRepositoryMockTest {

    private NurseRepository nurseRepository;
    @Autowired
    public NurseRepositoryMockTest(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Test
    public void whenFindByCode_thenReturnNurse(){
        Nurse nurse = new Nurse("12345678A", "Calle Blanco",
                "Veronica", "8f1b88d4-9cc4-4a38-b7c1-0026f2f24dab", LocalTime.parse("08:00:00"),
                LocalTime.parse("17:00:00"));

        nurseRepository.save(nurse);
        Nurse found = nurseRepository.findByCode(nurse.getCode());
        Assertions.assertThat(found.getCode().equals(nurse.getCode()));
    }

    @Test
    public void whenCheckIfExists_returnTrue(){
        Nurse nurse = new Nurse("12345678A", "Calle Blanco",
                "Veronica", "8f1b88d4-9cc4-4a38-b7c1-0026f2f24dab", LocalTime.parse("08:00:00"),
                LocalTime.parse("17:00:00"));

        nurseRepository.save(nurse);
        boolean exists = nurseRepository.existsByCode(nurse.getCode());
        Assertions.assertThat(exists).isTrue();
    }
}
