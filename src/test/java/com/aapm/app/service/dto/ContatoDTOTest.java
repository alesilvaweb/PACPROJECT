package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContatoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContatoDTO.class);
        ContatoDTO contatoDTO1 = new ContatoDTO();
        contatoDTO1.setId(1L);
        ContatoDTO contatoDTO2 = new ContatoDTO();
        assertThat(contatoDTO1).isNotEqualTo(contatoDTO2);
        contatoDTO2.setId(contatoDTO1.getId());
        assertThat(contatoDTO1).isEqualTo(contatoDTO2);
        contatoDTO2.setId(2L);
        assertThat(contatoDTO1).isNotEqualTo(contatoDTO2);
        contatoDTO1.setId(null);
        assertThat(contatoDTO1).isNotEqualTo(contatoDTO2);
    }
}
