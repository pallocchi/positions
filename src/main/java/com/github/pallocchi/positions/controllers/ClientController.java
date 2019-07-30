package com.github.pallocchi.positions.controllers;

import com.github.pallocchi.positions.model.Client;
import com.github.pallocchi.positions.model.Hunt;
import com.github.pallocchi.positions.repositories.ClientRepository;
import com.github.pallocchi.positions.repositories.HuntRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static com.github.pallocchi.positions.config.SwaggerConfig.AUTHORIZATION;

@RestController
@Api(tags = {"Clients"})
public class ClientController {

    private final ClientRepository clientRepository;
    private final HuntRepository huntRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository, HuntRepository huntRepository) {
        this.clientRepository = clientRepository;
        this.huntRepository = huntRepository;
    }

    @GetMapping("/clients")
    @ApiOperation(value = "Retrieves all the clients which do the hunting",
        authorizations = {@Authorization(AUTHORIZATION)})
    public List<Client> getClients(
        @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
        @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size) {

        return clientRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @PostMapping("/clients")
    @ApiOperation(value = "Creates a new client which does the hunting",
        authorizations = {@Authorization(AUTHORIZATION)})
    public void createClient(
        @RequestBody @Valid Client client) {

        clientRepository.save(client);
    }

    @PutMapping("/clients/{clientId}")
    @ApiOperation(value = "Updates an existing client which does the hunting",
        authorizations = {@Authorization(AUTHORIZATION)})
    public void updateClient(
        @PathVariable(value = "clientId") int clientId,
        @RequestBody Client client) {

        clientRepository.update(clientId, client);
    }

    @GetMapping("/clients/{clientId}/hunts")
    @ApiOperation(value = "Retrieves available hunts for given client",
        authorizations = {@Authorization(AUTHORIZATION)})
    public List<Hunt> getHunts(
        @PathVariable(value = "clientId") int clientId) {

        return huntRepository.findByClientId(clientId);
    }

    @PostMapping("/clients/{clientId}/hunts")
    @ApiOperation(value = "Creates a new hunt for given client",
        authorizations = {@Authorization(AUTHORIZATION)})
    public void createHunt(
        @PathVariable(value = "clientId") int clientId,
        @RequestBody Hunt hunt) {

        final Client client = clientRepository.findById(clientId).orElseThrow(EntityNotFoundException::new);

        hunt.setClient(client);
        huntRepository.save(hunt);
    }

    @PutMapping("/clients/{clientId}/hunts/{huntId}")
    @ApiOperation(value = "Updates an existing hunt of given client",
        authorizations = {@Authorization(AUTHORIZATION)})
    public void updateHunt(
        @PathVariable(value = "clientId") int clientId,
        @PathVariable(value = "huntId") int huntId,
        @RequestBody Hunt hunt) {

        huntRepository.update(huntId, hunt);
    }

}
