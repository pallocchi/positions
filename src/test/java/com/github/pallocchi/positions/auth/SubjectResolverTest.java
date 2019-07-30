package com.github.pallocchi.positions.auth;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubjectResolverTest {

    @Test
    public void asParameterTypeShouldReturnString() {

        final Object value = SubjectResolver.asParameterType("john.doe", String.class);

        assertThat(value).isExactlyInstanceOf(String.class);
        assertThat(value).isEqualTo("john.doe");
    }

    @Test
    public void asParameterTypeShouldReturnInteger() {

        final Object value = SubjectResolver.asParameterType("1", Integer.class);

        assertThat(value).isExactlyInstanceOf(Integer.class);
        assertThat(value).isEqualTo(1);
    }

    @Test
    public void asParameterTypeShouldReturnLong() {

        final Object value = SubjectResolver.asParameterType("1", Long.class);

        assertThat(value).isExactlyInstanceOf(Long.class);
        assertThat(value).isEqualTo(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void asParameterTypeWithNonRegisteredTypeShouldFail() {

        SubjectResolver.asParameterType("true", Boolean.class);
    }

}
