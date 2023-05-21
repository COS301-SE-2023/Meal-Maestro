package fellowship.mealmaestro;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloWorldTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloWorldTest() throws Exception {
        this.mockMvc.perform(get("/hello").with(user("user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello World!")));
    }

    @Test
    public void helloNameTest() throws Exception {
        this.mockMvc.perform(get("/hello").param("name", "Dolly").with(user("user")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello Dolly!")));
    }
}
