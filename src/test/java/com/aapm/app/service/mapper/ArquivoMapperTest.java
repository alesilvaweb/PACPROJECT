package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArquivoMapperTest {

    private ArquivoMapper arquivoMapper;

    @BeforeEach
    public void setUp() {
        arquivoMapper = new ArquivoMapperImpl();
    }
}
