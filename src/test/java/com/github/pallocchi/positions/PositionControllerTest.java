package com.github.pallocchi.positions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pallocchi.positions.controllers.PositionController;
import com.github.pallocchi.positions.exceptions.NonAvailableHuntException;
import com.github.pallocchi.positions.exceptions.ProviderNotRegisteredException;
import com.github.pallocchi.positions.model.Position;
import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.services.ImportService;
import com.github.pallocchi.positions.services.PositionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PositionController.class)
public class PositionControllerTest {

    @Value("${test.authorization.admin}")
    private String adminAuthorization;

    @Value("${test.authorization.client}")
    private String clientAuthorization;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private ImportService importService;

    @MockBean
    private PositionService positionService;

    @Test
    public void getPositionsShouldReturn200() throws Exception {

        final Position position = newPosition();

        when(positionService.search(1)).thenReturn(singletonList(position));

        mvc.perform(MockMvcRequestBuilders
            .get("/positions")
            .header(HttpHeaders.AUTHORIZATION, clientAuthorization)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(position.getId())))
            .andExpect(jsonPath("$[0].type", is(position.getType())))
            .andExpect(jsonPath("$[0].name", is(position.getName())))
            .andExpect(jsonPath("$[0].location", is(position.getLocation())))
            .andExpect(jsonPath("$[0].company", is(position.getCompany())));
    }

    @Test
    public void getPositionsWithNoHuntShouldReturn404() throws Exception {

        when(positionService.search(1)).thenThrow(new NonAvailableHuntException());

        mvc.perform(MockMvcRequestBuilders
            .get("/positions")
            .header(HttpHeaders.AUTHORIZATION, clientAuthorization)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getPositionsWithWrongTokenShouldReturn401() throws Exception {

        mvc.perform(MockMvcRequestBuilders
            .get("/positions")
            .header(HttpHeaders.AUTHORIZATION, "Bearer bad.token")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void importPositionsShouldReturn200() throws Exception {

        mvc.perform(MockMvcRequestBuilders
            .post("/positions")
            .param("provider", "GITHUB")
            .header(HttpHeaders.AUTHORIZATION, adminAuthorization)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(importService, times(1)).execute(Provider.Key.GITHUB);
    }

    @Test
    public void importPositionsWithInvalidKeyShouldReturn400() throws Exception {

        mvc.perform(MockMvcRequestBuilders
            .post("/positions")
            .param("provider", "WHATEVER")
            .header(HttpHeaders.AUTHORIZATION, adminAuthorization)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(importService, never()).execute(any());
    }

    @Test
    public void importPositionsWithNonRegisteredKeyShouldReturn422() throws Exception {

        doThrow(new ProviderNotRegisteredException()).when(importService).execute(Provider.Key.GITHUB);

        mvc.perform(MockMvcRequestBuilders
            .post("/positions")
            .param("provider", "GITHUB")
            .header(HttpHeaders.AUTHORIZATION, adminAuthorization)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());

        verify(importService, times(1)).execute(any());
    }

    private Position newPosition() {

        final Position position = new Position();
        position.setExternalId("713a561d-aa39-4f52-af3e-3fe5da9a3952");
        position.setType("Full Time");
        position.setName("Backend Engineer");
        position.setLocation("Dublin");
        position.setCompany("Amazon");
        return position;
    }

}
