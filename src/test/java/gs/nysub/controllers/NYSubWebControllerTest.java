package gs.nysub.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NYSubWebControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private NYSubWebController nySubWebController;

    @Before
    public void  setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(nySubWebController).build();
    }

    @Test
    public void get_receivesARequest_returnsSuccessfulResponse() throws Exception {
        mockMvc.perform(get("/status")).andExpect(status().isOk());
    }

    @Test
    public void post_receivesARequest_returnsMethodNotAllowedResponse() throws Exception {
        mockMvc.perform(post("/status")).andExpect(status().isMethodNotAllowed());
    }
}
