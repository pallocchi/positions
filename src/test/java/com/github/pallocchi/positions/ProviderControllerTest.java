package com.github.pallocchi.positions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pallocchi.positions.controllers.ProviderController;
import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.repositories.ProviderRepository;
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

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProviderController.class)
public class ProviderControllerTest {

    @Value("${test.authorization.admin}")
    private String authorization;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private ProviderRepository providerRepository;

    @Test
    public void getProvidersShouldReturn200() throws Exception {

        final Provider provider = newProvider();

        final Pageable pageable = PageRequest.of(0, 10);

        when(providerRepository.findAll(pageable)).thenReturn(new PageImpl<>(singletonList(provider)));

        mvc.perform(MockMvcRequestBuilders
            .get("/providers")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(provider.getId())))
            .andExpect(jsonPath("$[0].maxPositions", is(provider.getMaxPositions())))
            .andExpect(jsonPath("$[0].name", is(provider.getName())))
            .andExpect(jsonPath("$[0].url", is(provider.getUrl())));

        verify(providerRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(providerRepository);
    }

    @Test
    public void getProvidersWithBigSizeShouldReturn400() throws Exception {

        mvc.perform(MockMvcRequestBuilders
            .post("/providers")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .param("size", "101")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(providerRepository, never()).findAll();
    }

    @Test
    public void createProviderShouldReturn200() throws Exception {

        final Provider provider = newProvider();
        provider.setId(null);

        when(providerRepository.save(provider)).thenReturn(provider);

        mvc.perform(MockMvcRequestBuilders
            .post("/providers")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(provider)))
            .andExpect(status().isOk());

        verify(providerRepository, times(1)).save(provider);
    }

    @Test
    public void createProviderWithNullNameShouldReturn400() throws Exception {

        final Provider provider = newProvider();
        provider.setName(null);

        mvc.perform(MockMvcRequestBuilders
            .post("/providers")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(provider)))
            .andExpect(status().isBadRequest());

        verify(providerRepository, never()).save(any());
    }

    @Test
    public void createProviderWithNullUrlShouldReturn400() throws Exception {

        final Provider provider = newProvider();
        provider.setUrl(null);

        mvc.perform(MockMvcRequestBuilders
            .post("/providers")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(provider)))
            .andExpect(status().isBadRequest());

        verify(providerRepository, never()).save(any());
    }

    @Test
    public void createProviderWithNullMaxPositionsShouldReturn400() throws Exception {

        final Provider provider = newProvider();
        provider.setMaxPositions(null);

        when(providerRepository.save(provider)).thenReturn(provider);

        mvc.perform(MockMvcRequestBuilders
            .post("/providers")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(provider)))
            .andExpect(status().isBadRequest());

        verify(providerRepository, never()).save(any());
    }

    @Test
    public void createProviderWithNullKeysShouldReturn400() throws Exception {

        final Provider provider = newProvider();
        provider.setKey(null);

        when(providerRepository.save(provider)).thenReturn(provider);

        mvc.perform(MockMvcRequestBuilders
            .post("/providers")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(provider)))
            .andExpect(status().isBadRequest());

        verify(providerRepository, never()).save(any());
    }

    @Test
    public void updateProviderShouldReturn200() throws Exception {

        final Provider provider = newProvider();

        mvc.perform(MockMvcRequestBuilders
            .put("/providers/1")
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(provider)))
            .andExpect(status().isOk());

        verify(providerRepository, times(1)).update(provider.getId(), provider);
    }

    private Provider newProvider() {

        final Provider provider = new Provider();
        provider.setId(1);
        provider.setMaxPositions(10);
        provider.setName("Github");
        provider.setUrl("https://jobs.github.com/positions.json");
        provider.setKey(Provider.Key.GITHUB);
        return provider;
    }

}