import React, { useEffect, useState } from 'react';
import IconButton from '@mui/material/IconButton';
import FacebookIcon from '@mui/icons-material/Facebook';
import { Copyright, Instagram, Mail, Phone } from '@mui/icons-material';
import { formatDate } from '@fullcalendar/core';
import { now } from 'lodash';
import './footer.scss';
import Drawer from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import axios from 'axios';
import { useAppSelector } from 'app/config/store';

export const Footer = () => {
  const ano = formatDate(new Date(now()), { year: 'numeric' });
  const [parametro, setParametro] = useState([]);
  const [toogle, setToogle] = useState(false);
  const [email, setEmail] = useState('');
  const [telefone, setTelefone] = useState('');
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const iconsFooter = {
    fontSize: '10px',
    float: 'right',
    color: '#e2e2e2',
  };

  const toggleDrawer = (open: boolean) => (event: React.KeyboardEvent | React.MouseEvent) => {
    if (isAuthenticated) {
      fetchParametros().then(() => {
        if (event.type === 'keydown' && ((event as React.KeyboardEvent).key === 'Tab' || (event as React.KeyboardEvent).key === 'Shift')) {
          return;
        }
        setToogle(!toogle);
      });
    }
  };
  async function fetchParametros() {
    try {
      const response = await axios.get(`api/parametros?chave.in=mail-aapm&chave.in=fone-aapm&page=0&size=20`);
      setParametro(response.data);
    } catch (error) {
      console.error('Erro ao buscar Parametros:', error);
    }
  }

  /* Carrega as entidades do banco de dados */
  useEffect(() => {
    if (isAuthenticated) {
      fetchParametros();
    }
  }, []);

  /* Define a quantidade de dias permitida de acordo com os parametros "limite-start inicio e limite-end final" */
  useEffect(() => {
    if (isAuthenticated) {
      parametro.map(a => {
        if (a.chave.toString() === 'mail-aapm') {
          setEmail(a.valor);
        }
        if (a.chave.toString() === 'fone-aapm') {
          setTelefone(a.valor);
        }
      });
    }
  }, [parametro]);

  return (
    <div>
      <footer className={'footer-app'}>
        <div className={'copyright'}>
          &nbsp;
          <Copyright sx={{ fontSize: '20px' }} />
          <span style={{ fontSize: '14px' }}>&nbsp;{ano}&nbsp;AAPM</span>&nbsp;
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
          </IconButton>
          &nbsp;&nbsp;
          <IconButton color="primary" href={'https://www.instagram.com/aapmscs/'} target="_blank" sx={iconsFooter}>
            <Instagram />
          </IconButton>
          &nbsp;&nbsp;
          <IconButton color="primary" sx={iconsFooter} onClick={toggleDrawer(true)}>
            <Mail />
          </IconButton>
          &nbsp;&nbsp;
          <IconButton color="primary" onClick={toggleDrawer(true)} sx={iconsFooter}>
            <Phone />
          </IconButton>
        </div>
        <div className={'versao'}>
          &nbsp;<span style={{ fontSize: '14px' }}>{VERSION}</span>&nbsp;
        </div>
      </footer>
      <Drawer anchor={'bottom'} open={toogle} onClose={toggleDrawer(false)}>
        <Box
          sx={{
            height: '100px',
            textAlign: 'center',
            backgroundColor: '#353535',
            color: '#ffffff',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
          role="presentation"
        >
          <p>
            <span>
              Entre em contato com a AAPM pelo número {telefone} ou envie um e-mail para {email}
            </span>
          </p>
        </Box>
      </Drawer>
    </div>
  );
};

export default Footer;
