package com.aapm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aapm.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImagensConvenioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagensConvenioDTO.class);
        ImagensConvenioDTO imagensConvenioDTO1 = new ImagensConvenioDTO();
        imagensConvenioDTO1.setId(1L);
        ImagensConvenioDTO imagensConvenioDTO2 = new ImagensConvenioDTO();
        assertThat(imagensConvenioDTO1).isNotEqualTo(imagensConvenioDTO2);
        imagensConvenioDTO2.setId(imagensConvenioDTO1.getId());
        assertThat(imagensConvenioDTO1).isEqualTo(imagensConvenioDTO2);
        imagensConvenioDTO2.setId(2L);
        assertThat(imagensConvenioDTO1).isNotEqualTo(imagensConvenioDTO2);
        imagensConvenioDTO1.setId(null);
        assertThat(imagensConvenioDTO1).isNotEqualTo(imagensConvenioDTO2);
    }
}
