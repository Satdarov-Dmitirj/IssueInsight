package integretion;

import com.example.demo.controller.AnalisController;
import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.repository.TicketServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalisControllerTest {

    @Mock
    private TicketServiceInterface ticketService;

    @InjectMocks
    private AnalisController analisController;

    private AnalisResultRespouns resultDto;

    @BeforeEach
    void setUp() {
        resultDto = new AnalisResultRespouns();
        resultDto.setId(1L);
        resultDto.setDetectedCause("Оплата");
    }

    @Test
    void getTicketAnalysis_ShouldReturnList() {
        when(ticketService.getAnalysisResults(1L)).thenReturn(List.of(resultDto));

        List<AnalisResultRespouns> result = analisController.getTicketAnalysis(1L);

        assertEquals(1, result.size());
        assertEquals("Оплата", result.get(0).getDetectedCause());
        verify(ticketService).getAnalysisResults(1L);
    }
}