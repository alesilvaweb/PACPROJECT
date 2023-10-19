package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssociadoMapperTest {

    private AssociadoMapper associadoMapper;

    @BeforeEach
    public void setUp() {
        associadoMapper = new AssociadoMapperImpl();
    }
}
