package com.github.pallocchi.positions.services;


import com.github.pallocchi.positions.model.Position;
import com.github.pallocchi.positions.model.Provider;
import com.github.pallocchi.positions.repositories.ProviderRepository;
import com.github.pallocchi.positions.services.GithubProviderService.GithubPosition;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GithubProviderServiceTest {

    @Test
    public void findOpenPositionsWithMaxPositionsReached() {

        final Provider provider = new Provider();
        provider.setUrl("http://service.url/positions");
        provider.setMaxPositions(2);

        final ProviderRepository repository = mock(ProviderRepository.class);

        when(repository.findByKey(Provider.Key.GITHUB)).thenReturn(Optional.of(provider));

        final RestTemplate rest = mock(RestTemplate.class);

        final GithubPosition position1 = new GithubPosition();
        position1.setId("28ee607f-e81e-4d10-9df0-f82ce8aab6cc");
        position1.setType("Full Time");
        position1.setTitle("DevOps Engineer");
        position1.setLocation("London");
        position1.setCompany("Facebook");

        final GithubPosition position2 = new GithubPosition();
        position1.setId("713a561d-aa39-4f52-af3e-3fe5da9a3952");
        position1.setType("Full Time");
        position1.setTitle("Backend Engineer");
        position1.setLocation("Dublin");
        position1.setCompany("Amazon");

        final ResponseEntity<GithubPosition[]> response1 = mock(ResponseEntity.class);
        final ResponseEntity<GithubPosition[]> response2 = mock(ResponseEntity.class);

        when(response1.getBody()).thenReturn(new GithubPosition[]{position1});
        when(response2.getBody()).thenReturn(new GithubPosition[]{position2});

        when(rest.getForEntity(provider.getUrl(), GithubPosition[].class, 1)).thenReturn(response1);
        when(rest.getForEntity(provider.getUrl(), GithubPosition[].class, 2)).thenReturn(response2);

        final ProviderService service = new GithubProviderService(rest, repository);

        final List<Position> positions = service.findOpenPositions();

        assertThat(positions).hasSize(2);
        assertThat(positions.get(0).getExternalId()).isEqualTo(position1.getId());
        assertThat(positions.get(0).getType()).isEqualTo(position1.getType());
        assertThat(positions.get(0).getName()).isEqualTo(position1.getTitle());
        assertThat(positions.get(0).getLocation()).isEqualTo(position1.getLocation());
        assertThat(positions.get(0).getCompany()).isEqualTo(position1.getCompany());
        assertThat(positions.get(1).getExternalId()).isEqualTo(position2.getId());
        assertThat(positions.get(1).getType()).isEqualTo(position2.getType());
        assertThat(positions.get(1).getName()).isEqualTo(position2.getTitle());
        assertThat(positions.get(1).getLocation()).isEqualTo(position2.getLocation());
        assertThat(positions.get(1).getCompany()).isEqualTo(position2.getCompany());

        verify(rest, times(2)).getForEntity(any(), any(), anyInt());
    }

    @Test
    public void findOpenPositionsWithMaxPositionsNotReached() {

        final Provider provider = new Provider();
        provider.setUrl("http://service.url/positions");
        provider.setMaxPositions(10);

        final ProviderRepository repository = mock(ProviderRepository.class);

        when(repository.findByKey(Provider.Key.GITHUB)).thenReturn(Optional.of(provider));

        final RestTemplate rest = mock(RestTemplate.class);

        final GithubPosition position = new GithubPosition();
        position.setId("28ee607f-e81e-4d10-9df0-f82ce8aab6cc");
        position.setType("Full Time");
        position.setTitle("DevOps Engineer");
        position.setLocation("London");
        position.setCompany("Facebook");

        final ResponseEntity<GithubPosition[]> response1 = mock(ResponseEntity.class);
        final ResponseEntity<GithubPosition[]> response2 = mock(ResponseEntity.class);

        when(response1.getBody()).thenReturn(new GithubPosition[]{position});

        when(rest.getForEntity(provider.getUrl(), GithubPosition[].class, 1)).thenReturn(response1);
        when(rest.getForEntity(provider.getUrl(), GithubPosition[].class, 2)).thenReturn(response2);

        final ProviderService service = new GithubProviderService(rest, repository);

        final List<Position> positions = service.findOpenPositions();

        assertThat(positions).hasSize(1);
        assertThat(positions.get(0).getExternalId()).isEqualTo(position.getId());
        assertThat(positions.get(0).getType()).isEqualTo(position.getType());
        assertThat(positions.get(0).getName()).isEqualTo(position.getTitle());
        assertThat(positions.get(0).getLocation()).isEqualTo(position.getLocation());
        assertThat(positions.get(0).getCompany()).isEqualTo(position.getCompany());

        verify(rest, times(2)).getForEntity(any(), any(), anyInt());
    }

}
