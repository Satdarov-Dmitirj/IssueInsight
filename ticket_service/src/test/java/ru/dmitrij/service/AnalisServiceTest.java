package ru.dmitrij.service;

import com.example.demo.repository.AnalisRepository;
import com.example.demo.service.AnalisService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnalisServiceTest {
    @Mock
    private AnalisRepository analisRepository;

    @InjectMocks
    AnalisService analisService;
}
