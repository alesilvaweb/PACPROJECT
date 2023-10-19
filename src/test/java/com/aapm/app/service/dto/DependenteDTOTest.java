package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependenteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DependenteDTO.class);
        DependenteDTO dependenteDTO1 = new DependenteDTO();
        dependenteDTO1.setId(1L);
        DependenteDTO dependenteDTO2 = new DependenteDTO();
        assertThat(dependenteDTO1).isNotEqualTo(dependenteDTO2);
        dependenteDTO2.setId(dependenteDTO1.getId());
        assertThat(dependenteDTO1).isEqualTo(dependenteDTO2);
        dependenteDTO2.setId(2L);
        assertThat(dependenteDTO1).isNotEqualTo(dependenteDTO2);
        dependenteDTO1.setId(null);
        assertThat(dependenteDTO1).isNotEqualTo(dependenteDTO2);
    }
}
