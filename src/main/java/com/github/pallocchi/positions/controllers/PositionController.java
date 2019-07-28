package com.github.pallocchi.positions.controllers;

import com.github.pallocchi.positions.model.Position;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {"Positions"})
public class PositionController {

    @GetMapping(value = "/positions")
    @ApiOperation(value = "Retrieves the open positions available for logged client")
    public List<Position> get() {
        throw new UnsupportedOperationException();
    }

    @PostMapping(value = "/positions")
    @ApiOperation(value = "Starts to import of the open positions from given provider")
    public void post(@RequestParam("provider_id") Integer providerId) {
        throw new UnsupportedOperationException();
    }

}