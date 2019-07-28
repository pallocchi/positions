package com.github.pallocchi.positions.repositories;

import com.github.pallocchi.positions.model.Client;
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
@EnableJpaRepositories(basePackageClasses = ClientRepository.class)
@EntityScan(basePackageClasses = Client.class)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    @Test
    public void findAllShouldReturnSavedClient() {

        final Client client = new Client();
        client.setName("Green Future");

        final Client persisted = repository.save(client);

        assertThat(persisted.getId()).isNotNull();

        final Pageable pageable = PageRequest.of(0, 10);

        final List<Client> clients = repository.findAll(pageable).getContent();

        assertThat(clients).hasSize(1);
        assertThat(clients).containsOnly(persisted);
    }

}
