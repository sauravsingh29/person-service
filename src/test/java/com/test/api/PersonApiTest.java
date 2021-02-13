package com.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.PersonServiceApplication;
import com.test.request.PersonRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = PersonServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonApiTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final String ROOT_URL = "/persons";

    private static PersonRequest personRequest;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Order(1)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void save() throws Exception {
        buildRequest();
        mockMvc.perform(
                MockMvcRequestBuilders.post(ROOT_URL)
                        .content(asJsonString(personRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Saurav"));
    }

    @Test
    @Order(2)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void findAllPerson() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].firstName").value("Saurav"));
    }

    @Test
    @Order(3)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void findPerson_without_param() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].firstName").value("Saurav"))
                .andExpect(jsonPath("$[0].lastName").value("Singh"));
    }

    @Test
    @Order(3)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void findPerson_firstName_found() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL + "/search?firstName=Sau")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].firstName").value("Saurav"));
    }

    @Test
    @Order(4)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void findPerson_firstName_notFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL + "/search?firstName=Singh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(5)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void findPerson_lastName_found() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL + "/search?lastName=Singh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @Order(6)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void findPerson_lastName_notFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL + "/search?lastName=Saurav")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(7)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void findPersonById_notFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void findPersonById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Saurav"));
    }

    @Test
    @Order(9)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void update_success() throws Exception {
        personRequest.setFirstName("John");
        personRequest.setLastName("Keynes");
        mockMvc.perform(
                MockMvcRequestBuilders.put(ROOT_URL + "/1")
                        .content(asJsonString(personRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL + "/1")
                        .content(asJsonString(personRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Keynes"));
    }

    @Test
    @Order(10)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void update_failed() throws Exception {
        personRequest.setFirstName("John");
        personRequest.setLastName("Keynes");
        mockMvc.perform(
                MockMvcRequestBuilders.put(ROOT_URL + "/2")
                        .content(asJsonString(this.personRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }

    @Test
    @Order(11)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void delete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete(ROOT_URL + "/1")
                        .content(asJsonString(this.personRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get(ROOT_URL + "/1")
                        .content(asJsonString(this.personRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }

    @Test
    @Order(12)
    @WithMockUser(value = "admin", password = "admin", roles = {"ADMIN"})
    void save_badRequest() throws Exception {
        personRequest.setLastName(null);
        mockMvc.perform(
                MockMvcRequestBuilders.post(ROOT_URL)
                        .content(asJsonString(personRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errors").exists());
    }

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void buildRequest() {
        personRequest = new PersonRequest();
        personRequest.setAge(30);
        personRequest.setFirstName("Saurav");
        personRequest.setLastName("Singh");
        personRequest.setFavouriteColour("Black");
    }
}