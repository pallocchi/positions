package com.github.pallocchi.positions.services;

import com.github.pallocchi.positions.exceptions.ProviderNotRegisteredException;
import com.github.pallocchi.positions.model.Position;
import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service to import positions from multiple {@link com.github.pallocchi.positions.model.Provider}.
 */
@Service
public class ImportService {

    /**
     * {@link Provider.Key#name()} -> {@link ProviderService} implementation
     *
     * <p>If you add a new implementation and you want it to be mapped and available for execution,
     * you just have to annotate your service with {@link org.springframework.stereotype.Service},
     * passing the {@link Provider.Key#name()} as the value, so it will be used as entry key here.
     */
    private final Map<String, ProviderService> services;

    private final PositionRepository repository;

    @Autowired
    public ImportService(Map<String, ProviderService> services, PositionRepository repository) {
        this.services = services;
        this.repository = repository;
    }

    /**
     * Starts to import the open positions from given provider, using its registered {@link ProviderService}.
     *
     * @param providerKey the provider key
     * @see ProviderService
     */
    public void execute(Provider.Key providerKey) {

        if (services.containsKey(providerKey.name())) {

            final List<Position> positions = services.get(providerKey.name()).findOpenPositions();

            for (Position position : positions) {

                final boolean notExists = !repository.existsByExternalId(position.getExternalId());

                if (notExists) {
                    repository.save(position);
                }
            }

        } else {

            throw new ProviderNotRegisteredException();
        }
    }

}
