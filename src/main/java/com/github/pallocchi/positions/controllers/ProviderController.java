package com.github.pallocchi.positions.controllers;

import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.repositories.ProviderRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@Api(tags = {"Providers"})
public class ProviderController {

    private final ProviderRepository providerRepository;

    @Autowired
    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @GetMapping("/providers")
    @ApiOperation(value = "Retrieves all the providers of positions")
    public List<Provider> getProviders(
        @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
        @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size) {

        return providerRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @PostMapping("/providers")
    @ApiOperation(value = "Creates a new provider of positions")
    public void createProvider(
        @RequestBody @Valid Provider provider) {

        providerRepository.save(provider);
    }

    @PutMapping("/providers/{id}")
    @ApiOperation(value = "Updates an existing provider of positions")
    public void updateProvider(
        @PathVariable(value = "id") int id,
        @RequestBody Provider provider) {

        providerRepository.update(id, provider);
    }

}
