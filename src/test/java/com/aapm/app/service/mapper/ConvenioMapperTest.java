package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConvenioMapperTest {

    private ConvenioMapper convenioMapper;

    @BeforeEach
    public void setUp() {
        convenioMapper = new ConvenioMapperImpl();
    }
}
