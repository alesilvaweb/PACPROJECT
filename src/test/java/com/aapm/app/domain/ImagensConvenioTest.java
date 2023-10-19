package com.aapm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImagensConvenioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagensConvenio.class);
        ImagensConvenio imagensConvenio1 = new ImagensConvenio();
        imagensConvenio1.setId(1L);
        ImagensConvenio imagensConvenio2 = new ImagensConvenio();
        imagensConvenio2.setId(imagensConvenio1.getId());
        assertThat(imagensConvenio1).isEqualTo(imagensConvenio2);
        imagensConvenio2.setId(2L);
        assertThat(imagensConvenio1).isNotEqualTo(imagensConvenio2);
        imagensConvenio1.setId(null);
        assertThat(imagensConvenio1).isNotEqualTo(imagensConvenio2);
    }
}
