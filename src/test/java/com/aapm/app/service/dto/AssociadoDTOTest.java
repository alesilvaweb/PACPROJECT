package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssociadoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssociadoDTO.class);
        AssociadoDTO associadoDTO1 = new AssociadoDTO();
        associadoDTO1.setId(1L);
        AssociadoDTO associadoDTO2 = new AssociadoDTO();
        assertThat(associadoDTO1).isNotEqualTo(associadoDTO2);
        associadoDTO2.setId(associadoDTO1.getId());
        assertThat(associadoDTO1).isEqualTo(associadoDTO2);
        associadoDTO2.setId(2L);
        assertThat(associadoDTO1).isNotEqualTo(associadoDTO2);
        associadoDTO1.setId(null);
        assertThat(associadoDTO1).isNotEqualTo(associadoDTO2);
    }
}
