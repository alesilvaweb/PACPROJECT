package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MensagemMapperTest {

    private MensagemMapper mensagemMapper;

    @BeforeEach
    public void setUp() {
        mensagemMapper = new MensagemMapperImpl();
    }
}
