package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MensagemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MensagemDTO.class);
        MensagemDTO mensagemDTO1 = new MensagemDTO();
        mensagemDTO1.setId(1L);
        MensagemDTO mensagemDTO2 = new MensagemDTO();
        assertThat(mensagemDTO1).isNotEqualTo(mensagemDTO2);
        mensagemDTO2.setId(mensagemDTO1.getId());
        assertThat(mensagemDTO1).isEqualTo(mensagemDTO2);
        mensagemDTO2.setId(2L);
        assertThat(mensagemDTO1).isNotEqualTo(mensagemDTO2);
        mensagemDTO1.setId(null);
        assertThat(mensagemDTO1).isNotEqualTo(mensagemDTO2);
    }
}
