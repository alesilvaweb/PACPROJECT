import React from 'react';
import { Button, Grid, Typography } from '@mui/material';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import { CorporateFare, CreditCard, SpaceBar } from '@mui/icons-material';
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

  const iconStyle = {
    // fontSize:"5vh", // Ajuste o tamanho do ícone conforme necessário
    // // position: 'absolute', // Posicionamento absoluto
    // color: 'write',
    // marginTop: 1, // Distância do topo
    // marginLeft: 1, // Distância da esquerda

    fontSize: '7vh',
    color: 'white',
  };
  return (
    <div>
      {isAuthenticated ? (
        <>
          <div>
            {/*<Typography gutterBottom variant={'h5'} sx={{ fontWeight: 500 }}>*/}
            {/*  Bem vindo ao APP da AAPM*/}
            {/*</Typography>*/}
            {/*<Typography gutterBottom variant={'subtitle1'}>*/}
            {/*  Aqui você pode fazer reservas para sedes, consultar convênios e gerar o seu cartão de associado, veja como utilizar o sistema{' '}*/}
            {/*  <Link href={'/guia'}>aqui ...</Link>*/}
            {/*</Typography>*/}
            <div className={'tituloInicial'}>Bem-vindos ao App da AAPM</div>
            <div className={'textoInicial'}>Explore os recursos exclusivos para membros da AAPM e aproveite ao máximo sua associação.</div>
          </div>
          <ModalInicial />
          <Grid container>
            <Grid container spacing={{ xs: 2, md: 2, sm: 1 }} columns={{ xs: 4, sm: 4, md: 12 }} sx={{ display: 'flex', flexWrap: 'wrap' }}>
              <CardLocais
                link={'/cabanas'}
                title={'Reservas'}
                subtitle={'Faça reservas para eventos aqui.'}
                icon={<EventAvailableIcon sx={iconStyle} />}
              />
              <CardLocais
                link={'/cartao'}
                title={'Cartões'}
                subtitle={'Gerencie seus cartões da AAPM nesta seção.'}
                icon={<CreditCard sx={iconStyle} />}
              />
              <CardLocais
                link={'/convenio/list'}
                title={'Convênios'}
                subtitle={'Descubra os convênios disponíveis para os membros da AAPM.'}
                icon={<CorporateFare sx={iconStyle} />}
              />
            </Grid>

            <Grid item xs={12} sm={12} md={12}>
              <br />
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
