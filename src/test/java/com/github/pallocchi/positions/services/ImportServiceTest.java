package com.github.pallocchi.positions.services;

import com.github.pallocchi.positions.exceptions.ProviderNotRegisteredException;
import com.github.pallocchi.positions.model.Position;
import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.repositories.PositionRepository;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImportServiceTest {

    @Test
    public void importShouldSavePositions() {

        final Position position = new Position();
        position.setExternalId("713a561d-aa39-4f52-af3e-3fe5da9a3952");
        position.setType("Full Time");
        position.setName("Backend Engineer");
        position.setLocation("Dublin");
        position.setCompany("Amazon");

        final ProviderService github = mock(ProviderService.class);

        when(github.findOpenPositions()).thenReturn(singletonList(position));

        final Map<String, ProviderService> services = ImmutableMap.of(Provider.Key.GITHUB.name(), github);

        final PositionRepository repository = mock(PositionRepository.class);

        new ImportService(services, repository).execute(Provider.Key.GITHUB);

        verify(repository, times(1)).save(position);
    }

    @Test(expected = ProviderNotRegisteredException.class)
    public void importWithNonRegisteredProviderShouldFail() {

        final Map<String, ProviderService> services = ImmutableMap.of();

        final PositionRepository repository = mock(PositionRepository.class);

        new ImportService(services, repository).execute(Provider.Key.GITHUB);
    }

}
