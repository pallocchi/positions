package com.github.pallocchi.positions.services;

import com.github.pallocchi.positions.exceptions.ProviderNotRegisteredException;
import com.github.pallocchi.positions.model.Position;
import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link ProviderService} to import positions from {@link Provider.Key#GITHUB} provider.
 */
@Service("GITHUB")
class GithubProviderService implements ProviderService {

    private final ProviderRepository repository;

    private final RestTemplate rest;

    @Autowired
    GithubProviderService(RestTemplate rest, ProviderRepository repository) {
        this.rest = rest;
        this.repository = repository;
    }

    @Override
    public List<Position> findOpenPositions() {

        // Since the import runs as a job (and not in query time)
        // does not make sense to cache the provider, so it is retrieved
        // from the database on each execution, getting the updated parameters.

        final Provider provider = repository.findByKey(Provider.Key.GITHUB)
            .orElseThrow(ProviderNotRegisteredException::new);

        final String url = provider.getUrl();

        final int limit = provider.getMaxPositions();

        int page = 1;

        final List<Position> positions = new ArrayList<>();

        while (positions.size() < limit) {

            final List<Position> positionsInPage = findOpenPositionsByPage(url, page, limit);

            if (!positionsInPage.isEmpty()) {

                positions.addAll(positionsInPage);
                page++;

            } else {

                // No more positions
                return positions;
            }
        }

        // Max positions reached
        return positions;
    }

    /**
     * Retrieves open positions in given page.
     *
     * @param url the service url
     * @param page the page number
     * @param limit the max positions count
     * @return the open positions found
     */
    private List<Position> findOpenPositionsByPage(String url, int page, int limit) {

        final ResponseEntity<GithubPosition[]> response = rest.getForEntity(url, GithubPosition[].class, page);

        if (response.getBody() != null) {

            return Stream.of(response.getBody())
                .map(GithubProviderService::transform)
                .limit(limit)
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * Transforms a {@link GithubPosition} instance to a {@link Position} one.
     *
     * @param source the Github position
     * @return the position
     */
    private static Position transform(GithubPosition source) {

        final Position target = new Position();
        target.setExternalId(source.getId());
        target.setName(source.getTitle());
        target.setType(source.getType());
        target.setCompany(source.getCompany());
        target.setLocation(source.getLocation());
        return target;
    }

    /**
     * DTO to map Github position.
     */
    static class GithubPosition {

        private String id;
        private String type;
        private String location;
        private String title;
        private String company;

        String getId() {
            return id;
        }

        void setId(String id) {
            this.id = id;
        }

        String getType() {
            return type;
        }

        void setType(String type) {
            this.type = type;
        }

        String getLocation() {
            return location;
        }

        void setLocation(String location) {
            this.location = location;
        }

        String getTitle() {
            return title;
        }

        void setTitle(String title) {
            this.title = title;
        }

        String getCompany() {
            return company;
        }

        void setCompany(String company) {
            this.company = company;
        }

    }

}
