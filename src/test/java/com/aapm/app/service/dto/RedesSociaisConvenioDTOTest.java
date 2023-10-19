package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RedesSociaisConvenioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedesSociaisConvenioDTO.class);
        RedesSociaisConvenioDTO redesSociaisConvenioDTO1 = new RedesSociaisConvenioDTO();
        redesSociaisConvenioDTO1.setId(1L);
        RedesSociaisConvenioDTO redesSociaisConvenioDTO2 = new RedesSociaisConvenioDTO();
        assertThat(redesSociaisConvenioDTO1).isNotEqualTo(redesSociaisConvenioDTO2);
        redesSociaisConvenioDTO2.setId(redesSociaisConvenioDTO1.getId());
        assertThat(redesSociaisConvenioDTO1).isEqualTo(redesSociaisConvenioDTO2);
        redesSociaisConvenioDTO2.setId(2L);
        assertThat(redesSociaisConvenioDTO1).isNotEqualTo(redesSociaisConvenioDTO2);
        redesSociaisConvenioDTO1.setId(null);
        assertThat(redesSociaisConvenioDTO1).isNotEqualTo(redesSociaisConvenioDTO2);
    }
}
