package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DescontoConvenioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescontoConvenioDTO.class);
        DescontoConvenioDTO descontoConvenioDTO1 = new DescontoConvenioDTO();
        descontoConvenioDTO1.setId(1L);
        DescontoConvenioDTO descontoConvenioDTO2 = new DescontoConvenioDTO();
        assertThat(descontoConvenioDTO1).isNotEqualTo(descontoConvenioDTO2);
        descontoConvenioDTO2.setId(descontoConvenioDTO1.getId());
        assertThat(descontoConvenioDTO1).isEqualTo(descontoConvenioDTO2);
        descontoConvenioDTO2.setId(2L);
        assertThat(descontoConvenioDTO1).isNotEqualTo(descontoConvenioDTO2);
        descontoConvenioDTO1.setId(null);
        assertThat(descontoConvenioDTO1).isNotEqualTo(descontoConvenioDTO2);
    }
}
