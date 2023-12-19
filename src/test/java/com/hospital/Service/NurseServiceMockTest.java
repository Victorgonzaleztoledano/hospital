package com.hospital.Service;

import com.hospital.Repository.NurseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NurseServiceMockTest {
    @Mock
    private NurseRepository nurseRepository;

    private NurseService nurseService;

    @BeforeEach
    public void setup(){

    }
}