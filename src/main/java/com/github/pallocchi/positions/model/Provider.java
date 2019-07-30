package com.github.pallocchi.positions.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "provider")
@ApiModel(description = "The owner of the open positions who provide us the data")
public class Provider {

    public enum Key { GITHUB }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty("The provider id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    @ApiModelProperty("The provider name")
    private String name;

    @NotNull
    @Column(name = "max_positions")
    @ApiModelProperty("The max positions to collect")
    private Integer maxPositions;

    @NotEmpty
    @URL
    @Column(name = "url")
    @ApiModelProperty("The url to collect positions from")
    private String url;

    @NotNull
    @Enumerated
    @Column(name = "key", unique = true)
    private Key key;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxPositions() {
        return maxPositions;
    }

    public void setMaxPositions(Integer maxPositions) {
        this.maxPositions = maxPositions;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Provider)) return false;
        Provider provider = (Provider) o;
        return Objects.equals(id, provider.id) &&
            Objects.equals(name, provider.name) &&
            Objects.equals(maxPositions, provider.maxPositions) &&
            Objects.equals(url, provider.url) &&
            key == provider.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, maxPositions, url, key);
    }

}
