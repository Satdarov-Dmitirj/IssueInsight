package integretion.controller;

import com.example.demo.controller.KeywordController;
import com.example.demo.DemoApplication;
import com.example.demo.dto.KeywordWeightDto;
import com.example.demo.dto.KeywordWeightRequest;
import com.example.demo.service.KeywordWeightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = KeywordController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = DemoApplication.class)
@Import(IntegretionTest.TestConfig.class)
class IntegretionTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KeywordWeightService keywordWeightService;

    @Test
    void getAllKeywords_shouldReturnOk() throws Exception {
        when(keywordWeightService.getAll()).thenReturn(List.of(new KeywordWeightDto()));

        mockMvc.perform(get("/api/keywords"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteKeyword_shouldReturnNoContent() throws Exception {
        doNothing().when(keywordWeightService).removeById(1L);

        mockMvc.perform(delete("/api/keywords/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createKeyword_shouldReturnCreated() throws Exception {
        KeywordWeightRequest request = new KeywordWeightRequest();
        request.setCategoryName("Оплата");
        request.setKeyword("оплата");
        request.setWeight(10.0);

        when(keywordWeightService.createWeight(any(KeywordWeightRequest.class)))
                .thenReturn(new KeywordWeightDto());

        mockMvc.perform(post("/api/keywords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        KeywordWeightService keywordWeightService() {
            return Mockito.mock(KeywordWeightService.class);
        }
    }
}
