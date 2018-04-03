package gs.nysub.controllers;

import gs.nysub.components.NYSubWebService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NYSubWebControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NYSubWebService nySubWebService;

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

    @Test
    public void post_onValidRequest_callsTheServiceToFulfillTheRequest() throws Exception {
        String serviceResponse = "<HTML>Sample response</HTML>";

        when(nySubWebService.generateStatus()).thenReturn(serviceResponse);

        mockMvc.perform(get("/status"));

        verify(nySubWebService).generateStatus();
    }
}
