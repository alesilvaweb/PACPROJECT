package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParametroMapperTest {

    private ParametroMapper parametroMapper;

    @BeforeEach
    public void setUp() {
        parametroMapper = new ParametroMapperImpl();
    }
}
