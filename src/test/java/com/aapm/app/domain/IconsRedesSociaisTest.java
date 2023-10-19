package com.aapm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IconsRedesSociaisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IconsRedesSociais.class);
        IconsRedesSociais iconsRedesSociais1 = new IconsRedesSociais();
        iconsRedesSociais1.setId(1L);
        IconsRedesSociais iconsRedesSociais2 = new IconsRedesSociais();
        iconsRedesSociais2.setId(iconsRedesSociais1.getId());
        assertThat(iconsRedesSociais1).isEqualTo(iconsRedesSociais2);
        iconsRedesSociais2.setId(2L);
        assertThat(iconsRedesSociais1).isNotEqualTo(iconsRedesSociais2);
        iconsRedesSociais1.setId(null);
        assertThat(iconsRedesSociais1).isNotEqualTo(iconsRedesSociais2);
    }
}
