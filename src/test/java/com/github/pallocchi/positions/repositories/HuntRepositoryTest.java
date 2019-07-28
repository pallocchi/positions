package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Client;
import com.github.pallocchi.positions.model.Hunt;
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
@EnableJpaRepositories(basePackageClasses = HuntRepository.class)
@EntityScan(basePackageClasses = Hunt.class)
public class HuntRepositoryTest {

    @Autowired
    private HuntRepository huntRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void findAllShouldReturnSavedHunt() {

        Client client = new Client();
        client.setName("Green Future");

        clientRepository.save(client);

        final Hunt hunt = new Hunt();
        hunt.setType("Full Time");
        hunt.setName("Java Software Engineer");
        hunt.setLocation("Wilmington)");
        hunt.setClient(client);

        final Hunt persisted = huntRepository.save(hunt);

        assertThat(persisted.getId()).isNotNull();

        final Pageable pageable = PageRequest.of(0, 10);

        final List<Hunt> hunts = huntRepository.findAll(pageable).getContent();

        assertThat(hunts).hasSize(1);
        assertThat(hunts).containsOnly(persisted);
    }

    @Test
    public void findByClientShouldReturnSavedHunt() {

        Client client = new Client();
        client.setName("Green Future");

        clientRepository.save(client);

        final Hunt hunt = new Hunt();
        hunt.setType("Full Time");
        hunt.setName("Java Software Engineer");
        hunt.setLocation("Wilmington)");
        hunt.setClient(client);

        final Hunt persisted = huntRepository.save(hunt);

        assertThat(persisted.getId()).isNotNull();

        final List<Hunt> hunts = huntRepository.findByClientId(client.getId());

        assertThat(hunts).hasSize(1);
        assertThat(hunts).containsOnly(persisted);
    }

}
