package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IconsRedesSociaisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IconsRedesSociaisDTO.class);
        IconsRedesSociaisDTO iconsRedesSociaisDTO1 = new IconsRedesSociaisDTO();
        iconsRedesSociaisDTO1.setId(1L);
        IconsRedesSociaisDTO iconsRedesSociaisDTO2 = new IconsRedesSociaisDTO();
        assertThat(iconsRedesSociaisDTO1).isNotEqualTo(iconsRedesSociaisDTO2);
        iconsRedesSociaisDTO2.setId(iconsRedesSociaisDTO1.getId());
        assertThat(iconsRedesSociaisDTO1).isEqualTo(iconsRedesSociaisDTO2);
        iconsRedesSociaisDTO2.setId(2L);
        assertThat(iconsRedesSociaisDTO1).isNotEqualTo(iconsRedesSociaisDTO2);
        iconsRedesSociaisDTO1.setId(null);
        assertThat(iconsRedesSociaisDTO1).isNotEqualTo(iconsRedesSociaisDTO2);
    }
}
