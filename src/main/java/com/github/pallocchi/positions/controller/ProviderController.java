package com.github.pallocchi.positions.controller;

import com.github.pallocchi.positions.model.Provider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {"Providers"})
public class ProviderController {

    @GetMapping("/providers")
    @ApiOperation(value = "Retrieves all the providers of positions")
    public List<Provider> get() {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/providers")
    @ApiOperation(value = "Creates a new provider of positions")
    public void post() {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/providers/{id}")
    @ApiOperation(value = "Updates an existing provider of positions")
    public void put(@PathVariable(value = "id") Integer id) {
        throw new UnsupportedOperationException();
    }

}