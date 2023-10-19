package com.aapm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalMapperTest {

    private LocalMapper localMapper;

    @BeforeEach
    public void setUp() {
        localMapper = new LocalMapperImpl();
    }
}
