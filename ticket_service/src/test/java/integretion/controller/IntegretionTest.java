package integretion.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // ← добавить
@SpringBootConfiguration

public class IntegretionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUserById_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/keywords"))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_ok() throws  Exception{
        mockMvc.perform(delete("/api/keywords/1"))
                .andExpect(status().isOk());
    }

    public void create_ok() throws  Exception{
        String Json """
                
                """;
        mockMvc.perform(post("/api/keywords").contentType(MediaType.APPLICATION_JSON).content())
                .andExpect(status().isOk());
    }
}