package com.github.pallocchi.positions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pallocchi.positions.config.JwtConfig;
import com.github.pallocchi.positions.controllers.ClientController;
import com.github.pallocchi.positions.model.Client;
import com.github.pallocchi.positions.model.Hunt;
import com.github.pallocchi.positions.repositories.ClientRepository;
import com.github.pallocchi.positions.repositories.HuntRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Value("${test.authorization.admin}")
    private String authorization;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private HuntRepository huntRepository;

    @Test
    public void getClientsShouldReturn200() throws Exception {

        final Client client = newClient();

        final Pageable pageable = PageRequest.of(0, 10);

        when(clientRepository.findAll(pageable)).thenReturn(new PageImpl<>(singletonList(client)));

        mvc.perform(MockMvcRequestBuilders
            .get("/clients")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(client.getId())))
            .andExpect(jsonPath("$[0].name", is(client.getName())));

        verify(clientRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void getClientsWithBigSizeShouldReturn400() throws Exception {

        mvc.perform(MockMvcRequestBuilders
            .post("/clients")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .param("size", "101")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(clientRepository, never()).findAll();
    }

    @Test
    public void createClientShouldReturn200() throws Exception {

        final Client client = newClient();
        client.setId(null);

        when(clientRepository.save(client)).thenReturn(client);

        mvc.perform(MockMvcRequestBuilders
            .post("/clients")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(client)))
            .andExpect(status().isOk());

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void createClientWithNullNameShouldReturn400() throws Exception {

        final Client client = newClient();
        client.setName(null);

        mvc.perform(MockMvcRequestBuilders
            .post("/clients")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(client)))
            .andExpect(status().isBadRequest());

        verify(clientRepository, never()).save(any());
    }

    @Test
    public void updateClientShouldReturn200() throws Exception {

        final Client client = newClient();

        when(clientRepository.save(client)).thenReturn(client);

        mvc.perform(MockMvcRequestBuilders
            .put("/clients/1")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(client)))
            .andExpect(status().isOk());

        verify(clientRepository, times(1)).update(client.getId(), client);
    }

    @Test
    public void getHuntsShouldReturn200() throws Exception {

        final Hunt hunt = newHunt();

        when(huntRepository.findByClientId(1)).thenReturn(singletonList(hunt));

        mvc.perform(MockMvcRequestBuilders
            .get("/clients/1/hunts")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type", is(hunt.getType())))
            .andExpect(jsonPath("$[0].name", is(hunt.getName())))
            .andExpect(jsonPath("$[0].location", is(hunt.getLocation())));

        verify(huntRepository, times(1)).findByClientId(1);
        verifyNoMoreInteractions(huntRepository);
    }

    @Test
    public void createHuntShouldReturn200() throws Exception {

        final Hunt hunt = newHunt();
        hunt.setId(null);

        final Client client = newClient();

        when(huntRepository.save(hunt)).thenReturn(hunt);
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));

        mvc.perform(MockMvcRequestBuilders
            .post("/clients/1/hunts")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(hunt)))
            .andExpect(status().isOk());

        verify(huntRepository, times(1)).save(hunt);
        verify(clientRepository, times(1)).findById(client.getId());
    }

    @Test
    public void createHuntWithInvalidClientShouldReturn404() throws Exception {

        final Hunt hunt = newHunt();
        hunt.setId(null);

        when(huntRepository.save(hunt)).thenReturn(hunt);
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders
            .post("/clients/1/hunts")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(hunt)))
            .andExpect(status().isNotFound());

        verify(huntRepository, never()).save(hunt);
        verify(clientRepository, times(1)).findById(1);
    }

    @Test
    public void updateHuntShouldReturn200() throws Exception {

        final Hunt hunt = newHunt();

        when(huntRepository.update(hunt.getId(), hunt)).thenReturn(hunt);

        mvc.perform(MockMvcRequestBuilders
            .put("/clients/1/hunts/1")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(hunt)))
            .andExpect(status().isOk());

        verify(huntRepository, times(1)).update(hunt.getId(), hunt);
    }

    private Client newClient() {

        final Client client = new Client();
        client.setId(1);
        client.setName("Green Future");
        return client;
    }

    private Hunt newHunt() {

        final Hunt hunt = new Hunt();
        hunt.setId(1);
        hunt.setType("Full Time");
        hunt.setName("Java Software Engineer");
        hunt.setLocation("Wilmington)");
        return hunt;
    }

}