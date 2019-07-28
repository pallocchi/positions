package com.github.pallocchi.positions.controllers;

import com.github.pallocchi.positions.model.Client;
import com.github.pallocchi.positions.model.Hunt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {"Clients"})
public class ClientController {

    @GetMapping("/clients")
    @ApiOperation(value = "Retrieves all the clients which do the hunting")
    public List<Client> getClients() {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/clients")
    @ApiOperation(value = "Creates a new client which does the hunting")
    public void createClient() {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/clients/{id}")
    @ApiOperation(value = "Updates an existing client which does the hunting")
    public void updateClient(@PathVariable(value = "id") Integer id) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/clients/{id}/hunts")
    @ApiOperation(value = "Retrieves available hunts for logged client")
    public List<Hunt> getHunts() {
        throw new UnsupportedOperationException();
    }

}