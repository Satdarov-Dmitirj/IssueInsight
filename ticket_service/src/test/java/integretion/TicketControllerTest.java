package integretion;

import com.example.demo.repository.TicketServiceInterface;
import com.example.demo.service.EmailNotificationSendlerService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.web.bind.annotation.GetMapping;
import tools.jackson.databind.ObjectMapper;

public class TicketControllerTest {

    @Mock
    private TicketServiceInterface ticketService;

    @Mock
    private EmailNotificationSendlerService emailNotificationSender;

    GetMapping getMapping;

    ObjectMapper objectMapper;

    @BeforeEach
    public  void  setUp(){
        this.objectMapper = new ObjectMapper();
    }
}
