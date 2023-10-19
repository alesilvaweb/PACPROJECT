package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConvenioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConvenioDTO.class);
        ConvenioDTO convenioDTO1 = new ConvenioDTO();
        convenioDTO1.setId(1L);
        ConvenioDTO convenioDTO2 = new ConvenioDTO();
        assertThat(convenioDTO1).isNotEqualTo(convenioDTO2);
        convenioDTO2.setId(convenioDTO1.getId());
        assertThat(convenioDTO1).isEqualTo(convenioDTO2);
        convenioDTO2.setId(2L);
        assertThat(convenioDTO1).isNotEqualTo(convenioDTO2);
        convenioDTO1.setId(null);
        assertThat(convenioDTO1).isNotEqualTo(convenioDTO2);
    }
}
