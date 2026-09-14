package com.mentoai.mentoaiapi.shared.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringJUnitWebConfig(CorsConfigTest.WebConfig.class)
@TestPropertySource(properties = "mentoai.cors.allowed-origins=http://localhost:3000, https://mentoai.example")
class CorsConfigTest {

    @Configuration
    @EnableWebMvc
    @Import({CorsConfig.class, TestController.class})
    static class WebConfig {}

    @RestController
    static class TestController {
        @GetMapping("/api/v1/analises/fila")
        String fila() {
            return "ok";
        }
    }

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc() {
        return MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void permitePollingDasOrigensConfiguradasSemCredenciais() throws Exception {
        for (String origin : new String[]{"http://localhost:3000", "https://mentoai.example"}) {
            mvc().perform(get("/api/v1/analises/fila").header("Origin", origin))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Origin", origin))
                    .andExpect(header().doesNotExist("Access-Control-Allow-Credentials"));
        }
    }

    @Test
    void permitePreflightComHeadersParaTodosOsMetodosConfigurados() throws Exception {
        for (String method : new String[]{"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"}) {
            mvc().perform(options("/api/v1/analises/fila")
                            .header("Origin", "http://localhost:3000")
                            .header("Access-Control-Request-Method", method)
                            .header("Access-Control-Request-Headers", "content-type,authorization"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                    .andExpect(header().exists("Access-Control-Allow-Headers"));
        }
    }

    @Test
    void rejeitaOrigemNaoConfigurada() throws Exception {
        mvc().perform(options("/api/v1/analises/fila")
                        .header("Origin", "https://outra.example")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void permiteChamadaInternaSemOrigin() throws Exception {
        mvc().perform(get("/api/v1/analises/fila"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
