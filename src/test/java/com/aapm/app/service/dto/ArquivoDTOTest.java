package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArquivoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoDTO.class);
        ArquivoDTO arquivoDTO1 = new ArquivoDTO();
        arquivoDTO1.setId(1L);
        ArquivoDTO arquivoDTO2 = new ArquivoDTO();
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
        arquivoDTO2.setId(arquivoDTO1.getId());
        assertThat(arquivoDTO1).isEqualTo(arquivoDTO2);
        arquivoDTO2.setId(2L);
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
        arquivoDTO1.setId(null);
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
    }
}
