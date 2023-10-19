package com.aapm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MensagemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mensagem.class);
        Mensagem mensagem1 = new Mensagem();
        mensagem1.setId(1L);
        Mensagem mensagem2 = new Mensagem();
        mensagem2.setId(mensagem1.getId());
        assertThat(mensagem1).isEqualTo(mensagem2);
        mensagem2.setId(2L);
        assertThat(mensagem1).isNotEqualTo(mensagem2);
        mensagem1.setId(null);
        assertThat(mensagem1).isNotEqualTo(mensagem2);
    }
}
