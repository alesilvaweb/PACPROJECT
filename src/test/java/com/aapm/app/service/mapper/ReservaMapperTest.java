package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservaMapperTest {

    private ReservaMapper reservaMapper;

    @BeforeEach
    public void setUp() {
        reservaMapper = new ReservaMapperImpl();
    }
}
