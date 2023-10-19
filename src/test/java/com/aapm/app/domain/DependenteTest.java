package com.aapm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependenteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dependente.class);
        Dependente dependente1 = new Dependente();
        dependente1.setId(1L);
        Dependente dependente2 = new Dependente();
        dependente2.setId(dependente1.getId());
        assertThat(dependente1).isEqualTo(dependente2);
        dependente2.setId(2L);
        assertThat(dependente1).isNotEqualTo(dependente2);
        dependente1.setId(null);
        assertThat(dependente1).isNotEqualTo(dependente2);
    }
}
