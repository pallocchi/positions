package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.exceptions.TooManyHuntsException;
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

        final Client client = newClient();

        clientRepository.save(client);

        final Hunt hunt = newHunt();
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

        final Client client = newClient();

        clientRepository.save(client);

        final Hunt hunt = newHunt();
        hunt.setClient(client);

        final Hunt persisted = huntRepository.save(hunt);

        assertThat(persisted.getId()).isNotNull();

        final List<Hunt> hunts = huntRepository.findByClientId(client.getId());

        assertThat(hunts).hasSize(1);
        assertThat(hunts).containsOnly(persisted);
    }

    @Test
    public void existsByClientShouldReturnTrue() {

        Client client = newClient();

        clientRepository.save(client);

        final Hunt hunt = newHunt();
        hunt.setClient(client);

        final Hunt persisted = huntRepository.save(hunt);

        assertThat(persisted.getId()).isNotNull();

        final boolean exists = huntRepository.existsByClientId(client.getId());

        assertThat(exists).isTrue();
    }

    @Test
    public void createNonExistingHuntShouldSaveHunt() {

        Client client = newClient();

        clientRepository.save(client);

        final Hunt hunt = newHunt();
        hunt.setClient(client);

        final Hunt persisted = huntRepository.create(hunt);

        assertThat(persisted.getId()).isNotNull();
    }

    @Test(expected = TooManyHuntsException.class)
    public void createExistingHuntShouldFail() {

        Client client = newClient();

        clientRepository.save(client);

        final Hunt hunt1 = newHunt();
        hunt1.setClient(client);

        huntRepository.create(hunt1);

        final Hunt hunt2 = newHunt();
        hunt2.setClient(client);

        huntRepository.create(hunt2);
    }

    private static Client newClient() {

        Client client = new Client();
        client.setName("Green Future");

        return client;
    }

    private static Hunt newHunt() {

        final Hunt hunt = new Hunt();
        hunt.setType("Full Time");
        hunt.setName("Java Software Engineer");
        hunt.setLocation("Wilmington)");

        return hunt;
    }

}
