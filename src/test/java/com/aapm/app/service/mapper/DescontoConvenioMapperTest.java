package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DescontoConvenioMapperTest {

    private DescontoConvenioMapper descontoConvenioMapper;

    @BeforeEach
    public void setUp() {
        descontoConvenioMapper = new DescontoConvenioMapperImpl();
    }
}
