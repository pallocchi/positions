package com.github.pallocchi.positions.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "hunt")
@ApiModel(description = "The active search of open positions of the client")
public class Hunt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty("The hunt id")
    private Integer id;

    @Column(name = "type")
    @ApiModelProperty("The position type")
    private String type;

    @Column(name = "location")
    @ApiModelProperty("The position location")
    private String location;

    @Column(name = "name")
    @ApiModelProperty("The position name")
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hunt)) return false;
        Hunt hunt = (Hunt) o;
        return Objects.equals(id, hunt.id) &&
            Objects.equals(type, hunt.type) &&
            Objects.equals(location, hunt.location) &&
            Objects.equals(name, hunt.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, location, name);
    }

}
