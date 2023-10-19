package com.aapm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConvenioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Convenio.class);
        Convenio convenio1 = new Convenio();
        convenio1.setId(1L);
        Convenio convenio2 = new Convenio();
        convenio2.setId(convenio1.getId());
        assertThat(convenio1).isEqualTo(convenio2);
        convenio2.setId(2L);
        assertThat(convenio1).isNotEqualTo(convenio2);
        convenio1.setId(null);
        assertThat(convenio1).isNotEqualTo(convenio2);
    }
}
