package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImagensConvenioMapperTest {

    private ImagensConvenioMapper imagensConvenioMapper;

    @BeforeEach
    public void setUp() {
        imagensConvenioMapper = new ImagensConvenioMapperImpl();
    }
}
