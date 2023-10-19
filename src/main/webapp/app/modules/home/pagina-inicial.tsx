import React from 'react';

import { Grid } from '@mui/material';

import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import { CorporateFare, CreditCard, Event, Info, ManageAccounts } from '@mui/icons-material';
import Login from 'app/modules/login/login';
import { useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import CardLocais from 'app/modules/home/card-locais';
import ModalInicial from 'app/modules/home/nodal-inicial';

const PaginaInicial = () => {
  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();
  if (!account?.login) {
    navigate('/login');
  }
  return (
    <div>
      {account?.login ? (
        <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
          <ModalInicial />
          <CardLocais link={'/agenda'} title={'Agendamento'} icon={<EventAvailableIcon sx={{ fontSize: '7vh', color: 'white' }} />} />
          <CardLocais link={'/cartao'} title={'Cartão de Sócio'} icon={<CreditCard sx={{ fontSize: '7vh', color: 'white' }} />} />
          <CardLocais link={'/convenio'} title={'Convênios'} icon={<CorporateFare sx={{ fontSize: '7vh', color: 'white' }} />} />
          <CardLocais link={'/account/settings'} title={'Perfil'} icon={<ManageAccounts sx={{ fontSize: '7vh', color: 'white' }} />} />
          <CardLocais link={'/events'} title={'Eventos AAPM'} icon={<Event sx={{ fontSize: '7vh', color: 'white' }} />} />
          <CardLocais link={'/sobre'} title={'Sobre'} icon={<Info sx={{ fontSize: '7vh', color: 'white' }} />} />
        </Grid>
      ) : (
        <Login />
      )}
    </div>
  );
};

export default PaginaInicial;
