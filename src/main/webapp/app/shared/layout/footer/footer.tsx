import React, { useEffect, useState } from 'react';
import IconButton from '@mui/material/IconButton';
import FacebookIcon from '@mui/icons-material/Facebook';
import { Copyright, Instagram, Mail, Phone, Update } from '@mui/icons-material';
import { formatDate } from '@fullcalendar/core';
import { now } from 'lodash';
import './footer.scss';
import Drawer from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import axios from 'axios';
import { useAppSelector } from 'app/config/store';
import HomeIcon from '@mui/icons-material/Home';
import { useNavigate } from 'react-router-dom';
import { Col } from 'reactstrap';
import Link from '@mui/material/Link';

export const Footer = () => {
  const ano = formatDate(new Date(now()), { year: 'numeric' });
  const [parametro, setParametro] = useState([]);
  const [toogle, setToogle] = useState(false);
  const [email, setEmail] = useState('');
  const [telefone, setTelefone] = useState('');
  const [facebook, setFacebook] = useState('');
  const [instagram, setInstagram] = useState('');
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const navigate = useNavigate();

  const iconsFooter = {
    color: '#e2e2e2',
    ':hover': {
      color: '#1974d0',
    },
  };

  const icon = {
    fontSize: '3vh',
    '@media (min-width: 200px) and (max-width: 600px)': {
      fontSize: '4vh',
    },
  };
  const linkContato = {
    color: '#ffffff',
    textDecoration: 'none',
    ':hover': {
      color: '#1974d0',
    },
  };

  const handleClick = () => {
    const link = `tel:${telefone}`;
    window.location.href = link;
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
    if (isAuthenticated) {
      const url = `api/parametros?chave.in=mail&chave.in=fone&chave.in=facebook&chave.in=instagram&status.equals=Ativo&page=0&size=20`;
      try {
        const response = await axios.get(url);
        setParametro(response.data);
      } catch (error) {
        console.error('Erro ao buscar Parametros:', error);
      }
    }
  }

  /* Carrega as entidades do banco de dados */
  useEffect(() => {
    if (isAuthenticated) {
      fetchParametros();
    }
  }, [isAuthenticated]);

  /* Define a quantidade de dias permitida de acordo com os parametros "limite-start inicio e limite-end final" */
  useEffect(() => {
    if (isAuthenticated) {
      parametro.map(a => {
        if (a.chave.toString() === 'mail') {
          setEmail(a.valor);
        }
        if (a.chave.toString() === 'fone') {
          setTelefone(a.valor);
        }
        if (a.chave.toString() === 'facebook') {
          setFacebook(a.valor);
        }
        if (a.chave.toString() === 'instagram') {
          setInstagram(a.valor);
        }
      });
    }
  }, [parametro]);

  return (
    <>
      <footer className={'footer-app'}>
        <div className={'copyright'}>
          &nbsp;
          <Copyright sx={{ fontSize: '18px' }} />
          <span style={{ fontSize: '12px' }}>&nbsp;{ano}&nbsp;PAC Smart Solutions</span>&nbsp;
        </div>
        {isAuthenticated ? (
          <Col
            xs={10}
            sm={4}
            xl={3}
            style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}
          >
            <IconButton color="primary" onClick={() => navigate('/')} sx={iconsFooter}>
              <HomeIcon sx={icon} />
            </IconButton>
            &nbsp;&nbsp;
            <IconButton color="primary" href={facebook} target="_blank" onClick={fetchParametros} sx={iconsFooter}>
              <FacebookIcon sx={icon} />
            </IconButton>
            &nbsp;&nbsp;
            <IconButton color="primary" href={instagram} target="_blank" onClick={fetchParametros} sx={iconsFooter}>
              <Instagram sx={icon} />
            </IconButton>
            &nbsp;&nbsp;
            <IconButton color="primary" sx={iconsFooter} onClick={toggleDrawer(true)}>
              <Mail sx={icon} />
            </IconButton>
            &nbsp;&nbsp;
            <IconButton color="primary" onClick={toggleDrawer(true)} sx={iconsFooter}>
              <Phone sx={icon} />
            </IconButton>
          </Col>
        ) : null}

        <div className={'versao'}>
          <Update sx={{ fontSize: '18px' }} />
          &nbsp;<span style={{ fontSize: '12px' }}>{VERSION}</span>&nbsp;
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
          <Col xs={10} sm={4}>
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
              }}
            >
              <Link color="primary" href={`mailto:${email}`} sx={linkContato}>
                <Mail />
                &nbsp;{email}
              </Link>
              <Link color="primary" href={`tel:${telefone}`} onClick={handleClick} sx={linkContato}>
                {' '}
                <Phone />
                &nbsp;{telefone}{' '}
              </Link>
            </div>
          </Col>
        </Box>
      </Drawer>
    </>
  );
};

export default Footer;
