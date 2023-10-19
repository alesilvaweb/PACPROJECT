package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DependenteMapperTest {

    private DependenteMapper dependenteMapper;

    @BeforeEach
    public void setUp() {
        dependenteMapper = new DependenteMapperImpl();
    }
}
