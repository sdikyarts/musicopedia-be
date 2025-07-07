package musicopedia.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.*;

@WebMvcTest(controllers = AdminTokenFilterTest.DummyController.class)
@Import(AdminTokenFilter.class)
@TestPropertySource(properties = "admin.token=test-token")
class AdminTokenFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @RestController
    @RequestMapping("/dummy")
    static class DummyController {
        @PostMapping
        public String post() { return "ok"; }
        @PutMapping
        public String put() { return "ok"; }
        @DeleteMapping
        public String delete() { return "ok"; }
        @GetMapping
        public String get() { return "ok"; }
    }

    @Test
    void postWithoutTokenShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dummy"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void postWithWrongTokenShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dummy")
                .header("Authorization", "Bearer wrong-token"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void postWithCorrectTokenShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dummy")
                .header("Authorization", "Bearer test-token"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getWithoutTokenShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dummy"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void putWithoutTokenShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/dummy"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void putWithWrongTokenShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/dummy")
                .header("Authorization", "Bearer wrong-token"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void putWithCorrectTokenShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/dummy")
                .header("Authorization", "Bearer test-token"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteWithoutTokenShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/dummy"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void deleteWithWrongTokenShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/dummy")
                .header("Authorization", "Bearer wrong-token"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void deleteWithCorrectTokenShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/dummy")
                .header("Authorization", "Bearer test-token"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
