import React from 'react';
import { Grid } from '@mui/material';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import { CorporateFare, CreditCard } from '@mui/icons-material';
import Login from 'app/modules/login/login';
import { useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import CardLocais from 'app/modules/home/card-locais';
import ModalInicial from 'app/modules/home/nodal-inicial';
import Banner from 'app/modules/home/banner';
import './home.scss';

const PaginaInicial = () => {
  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();
  // if (!account?.login) {
  //   navigate('/login');
  // }
  return (
    <div>
      {account?.login ? (
        <>
          <div>
            <h4>Bem vindo ao APP da Associação Atlética Philip Morris</h4>
            <div
              className={'textoInicial'}
              style={{
                fontSize: '0.9rem',
                color: 'text.disabled',
              }}
            >
              Aqui você pode fazer reservas para as sedes da associação, consultar convênios, visualizar o seu cartão de associado entre
              outros recursos disponíveis, click <a href={'/guia'}>aqui</a> e descubra.
            </div>
          </div>
          <ModalInicial />
          <Grid container>
            <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
              <CardLocais link={'/cabanas'} title={'Reservas'} icon={<EventAvailableIcon sx={{ fontSize: '7vh', color: 'white' }} />} />

              <CardLocais link={'/cartao'} title={'Cartão de Sócio'} icon={<CreditCard sx={{ fontSize: '7vh', color: 'white' }} />} />

              <CardLocais link={'/convenio/list'} title={'Convênios'} icon={<CorporateFare sx={{ fontSize: '7vh', color: 'white' }} />} />
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
