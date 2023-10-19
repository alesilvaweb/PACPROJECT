package com.aapm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContatoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contato.class);
        Contato contato1 = new Contato();
        contato1.setId(1L);
        Contato contato2 = new Contato();
        contato2.setId(contato1.getId());
        assertThat(contato1).isEqualTo(contato2);
        contato2.setId(2L);
        assertThat(contato1).isNotEqualTo(contato2);
        contato1.setId(null);
        assertThat(contato1).isNotEqualTo(contato2);
    }
}
