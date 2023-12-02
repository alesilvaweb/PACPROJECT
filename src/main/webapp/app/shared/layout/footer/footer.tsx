import React from 'react';
import IconButton from '@mui/material/IconButton';
import FacebookIcon from '@mui/icons-material/Facebook';
import { Copyright, Instagram, Mail, Phone } from '@mui/icons-material';
import { formatDate } from '@fullcalendar/core';
import { now } from 'lodash';
import './footer.scss';

export const Footer = () => {
  const ano = formatDate(new Date(now()), { year: 'numeric' });
  const iconsFooter = {
    fontSize: '10px',
    float: 'right',
    color: '#e2e2e2',
  };
  return (
    <footer className={'footer-app'}>
      <div className={'copyright'}>
        &nbsp;
        <Copyright sx={{ fontSize: '20px' }} /> <span style={{ fontSize: '14px' }}>&nbsp;AAPM&nbsp;{ano}</span>&nbsp;
      </div>
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <IconButton color="primary" href={'https://www.facebook.com/aapmscs'} target="_blank" sx={iconsFooter}>
          <FacebookIcon />
          &nbsp;
          {/*/aapmscs*/}
        </IconButton>
        &nbsp;&nbsp;
        <IconButton color="primary" href={'https://www.instagram.com/aapmscs/'} target="_blank" sx={iconsFooter}>
          <Instagram />
          &nbsp;
          {/*/aapmscs*/}
        </IconButton>
        &nbsp;&nbsp;
        <IconButton
          color="primary"
          // href={""}
          // target="_blank"
          sx={iconsFooter}
        >
          <Mail />
          &nbsp;
          {/*aapm.diretoria@pmi.com*/}
        </IconButton>
        &nbsp;&nbsp;
        <IconButton
          color="primary"
          // href={""}
          // target="_blank"
          sx={iconsFooter}
        >
          <Phone />
          &nbsp;
          {/*51999999999*/}
        </IconButton>
      </div>
      <div className={'versao'}>
        &nbsp;<span style={{ fontSize: '14px' }}>Versão : {VERSION}</span>&nbsp;
      </div>
    </footer>
  );
};

export default Footer;
