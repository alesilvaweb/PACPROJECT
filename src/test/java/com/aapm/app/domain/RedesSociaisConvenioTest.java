package com.aapm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RedesSociaisConvenioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedesSociaisConvenio.class);
        RedesSociaisConvenio redesSociaisConvenio1 = new RedesSociaisConvenio();
        redesSociaisConvenio1.setId(1L);
        RedesSociaisConvenio redesSociaisConvenio2 = new RedesSociaisConvenio();
        redesSociaisConvenio2.setId(redesSociaisConvenio1.getId());
        assertThat(redesSociaisConvenio1).isEqualTo(redesSociaisConvenio2);
        redesSociaisConvenio2.setId(2L);
        assertThat(redesSociaisConvenio1).isNotEqualTo(redesSociaisConvenio2);
        redesSociaisConvenio1.setId(null);
        assertThat(redesSociaisConvenio1).isNotEqualTo(redesSociaisConvenio2);
    }
}
