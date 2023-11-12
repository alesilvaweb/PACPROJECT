import React from 'react';
import { Button, Grid, Typography } from '@mui/material';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import { CorporateFare, CreditCard } from '@mui/icons-material';
import Login from 'app/modules/login/login';
import { useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import CardLocais from 'app/modules/home/card-locais';
import ModalInicial from 'app/modules/home/nodal-inicial';
import Banner from 'app/modules/home/banner';
import './home.scss';
import Link from '@mui/material/Link';

const PaginaInicial = () => {
  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

  return (
    <div>
      {isAuthenticated ? (
        <>
          <div>
            <Typography gutterBottom variant={'h5'} sx={{ fontWeight: 500 }}>
              Bem vindo ao APP da AAPM
            </Typography>
            <Typography gutterBottom variant={'subtitle1'}>
              Aqui você pode fazer reservas para sedes, consultar convênios e gerar o seu cartão de associado, veja como utilizar o sistema{' '}
              <Link href={'/guia'}>aqui ...</Link>
            </Typography>
          </div>
          <ModalInicial />
          <Grid container>
            <Grid container spacing={{ xs: 2, md: 3, sm: 1 }} columns={{ xs: 4, sm: 12, md: 12 }}>
              <CardLocais
                link={'/cabanas'}
                title={'Reservas'}
                subtitle={'Realizar e consultar reservas para as sedes da AAPM >'}
                icon={<EventAvailableIcon sx={{ fontSize: '5vh', color: 'white' }} />}
              />
              <CardLocais
                link={'/cartao'}
                title={'Cartão de Sócio'}
                subtitle={'Visualizar o cartões de associado e dependentes >'}
                icon={<CreditCard sx={{ fontSize: '5vh', color: 'white' }} />}
              />
              <CardLocais
                link={'/convenio/list'}
                title={'Convênios'}
                subtitle={'Consultar Convênios da AAPM >'}
                icon={<CorporateFare sx={{ fontSize: '5vh', color: 'white' }} />}
              />
            </Grid>

            <Grid item xs={12} sm={12} md={12}>
              <br />

              <h5>Convênios</h5>
              <Banner />
            </Grid>
          </Grid>
          <br />
        </>
      ) : (
        <Login />
      )}
    </div>
  );
};

export default PaginaInicial;
