package com.github.pallocchi.positions.controllers;

import com.github.pallocchi.positions.model.Position;
import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.services.ImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {"Positions"})
public class PositionController {

    private final ImportService importService;

    @Autowired
    public PositionController(ImportService importService) {
        this.importService = importService;
    }

    @GetMapping(value = "/positions")
    @ApiOperation(value = "Retrieves the open positions available for logged client")
    public List<Position> getPositions() {
        throw new UnsupportedOperationException();
    }

    @PostMapping(value = "/positions")
    @ApiOperation(value = "Starts to import of the open positions from given provider")
    public void importPositions(@RequestParam("provider") Provider.Key providerKey) {

        importService.execute(providerKey);
    }

}
