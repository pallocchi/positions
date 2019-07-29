package com.github.pallocchi.positions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pallocchi.positions.controllers.PositionController;
import com.github.pallocchi.positions.exceptions.ProviderNotFoundException;
import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.services.ImportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PositionController.class)
public class PositionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private ImportService importService;

    @Test
    public void importPositionsShouldReturn2xx() throws Exception {

        mvc.perform(MockMvcRequestBuilders
            .post("/positions")
            .param("provider", "GITHUB")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(importService, times(1)).execute(Provider.Key.GITHUB);
    }

    @Test
    public void importPositionsWithInvalidKeyShouldReturn4xx() throws Exception {

        mvc.perform(MockMvcRequestBuilders
            .post("/positions")
            .param("provider", "WHATEVER")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());

        verify(importService, never()).execute(any());
    }

    @Test
    public void importPositionsWithNonRegisteredKeyShouldReturn4xx() throws Exception {

        doThrow(new ProviderNotFoundException()).when(importService).execute(Provider.Key.GITHUB);

        mvc.perform(MockMvcRequestBuilders
            .post("/positions")
            .param("provider", "GITHUB")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());

        verify(importService, times(1)).execute(any());
    }

}
