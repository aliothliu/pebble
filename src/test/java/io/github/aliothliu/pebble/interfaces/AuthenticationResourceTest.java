package io.github.aliothliu.pebble.interfaces;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class AuthenticationResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql("/scripts/admin_employee_auth.sql")
    void authorize() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/ops/authenticate").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"admin\",\"password\":\"reshuffle\"}")).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        String body = mvcResult.getResponse().getContentAsString();
        assertTrue(body.contains("token"));
    }

    @Test
    void authorizeFail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/ops/authenticate").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"admin\",\"password\":\"123456\"}")).andReturn();
        assertEquals(401, mvcResult.getResponse().getStatus());
        String body = mvcResult.getResponse().getContentAsString();
        assertTrue(body.contains("message"));
    }
}