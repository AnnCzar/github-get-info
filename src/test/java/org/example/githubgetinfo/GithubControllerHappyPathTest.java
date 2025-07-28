package org.example.githubgetinfo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GithubControllerHappyPathTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnNonForkRepositoriesWithBranches_HappyPath() throws Exception {
        //given
        String username = "octocat"; // official GitHub test user

        mockMvc.perform(get("/getInfo/{username}", username)) //when
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyOrNullString())))
                .andExpect(content().string(containsString("name=")))
                .andExpect(content().string(containsString("userlogin=octocat")))
                .andExpect(content().string(containsString("branches=")))
                .andExpect(content().string(containsString("Hello-World")));
    }
}
