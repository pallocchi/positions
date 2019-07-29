package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Provider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = ProviderRepository.class)
@EntityScan(basePackageClasses = Provider.class)
public class ProviderRepositoryTest {

    @Autowired
    private ProviderRepository repository;

    @Test
    public void findAllShouldReturnSavedProvider() {

        final Provider provider = new Provider();
        provider.setMaxPositions(10);
        provider.setName("Github");
        provider.setUrl("https://jobs.github.com/positions.json");
        provider.setKey(Provider.Key.GITHUB);

        final Provider persisted = repository.save(provider);

        assertThat(persisted.getId()).isNotNull();

        final Pageable pageable = PageRequest.of(0, 10);

        final List<Provider> providers = repository.findAll(pageable).getContent();

        assertThat(providers).hasSize(1);
        assertThat(providers).containsOnly(persisted);
    }

    @Test
    public void saveExistingShouldUpdateOnlyNotNullAttributes() {

        final Provider create = new Provider();
        create.setMaxPositions(10);
        create.setName("Github");
        create.setUrl("https://jobs.github.com/positions.json");

        final Provider created = repository.save(create);

        assertThat(created.getId()).isNotNull();

        final Provider update = new Provider();
        update.setId(created.getId());
        update.setMaxPositions(20);

        final Provider updated = repository.update(update.getId(), update);

        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(updated.getMaxPositions()).isEqualTo(20);
        assertThat(updated.getName()).isEqualTo("Github");
        assertThat(updated.getUrl()).isEqualTo("https://jobs.github.com/positions.json");
    }

}
