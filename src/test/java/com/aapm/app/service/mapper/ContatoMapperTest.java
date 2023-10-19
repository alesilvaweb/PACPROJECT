package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContatoMapperTest {

    private ContatoMapper contatoMapper;

    @BeforeEach
    public void setUp() {
        contatoMapper = new ContatoMapperImpl();
    }
}
