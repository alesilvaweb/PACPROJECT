package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedesSociaisConvenioMapperTest {

    private RedesSociaisConvenioMapper redesSociaisConvenioMapper;

    @BeforeEach
    public void setUp() {
        redesSociaisConvenioMapper = new RedesSociaisConvenioMapperImpl();
    }
}
