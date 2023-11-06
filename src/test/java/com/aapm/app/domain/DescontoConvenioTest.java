package com.aapm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DescontoConvenioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescontoConvenio.class);
        DescontoConvenio descontoConvenio1 = new DescontoConvenio();
        descontoConvenio1.setId(1L);
        DescontoConvenio descontoConvenio2 = new DescontoConvenio();
        descontoConvenio2.setId(descontoConvenio1.getId());
        assertThat(descontoConvenio1).isEqualTo(descontoConvenio2);
        descontoConvenio2.setId(2L);
        assertThat(descontoConvenio1).isNotEqualTo(descontoConvenio2);
        descontoConvenio1.setId(null);
        assertThat(descontoConvenio1).isNotEqualTo(descontoConvenio2);
    }
}
