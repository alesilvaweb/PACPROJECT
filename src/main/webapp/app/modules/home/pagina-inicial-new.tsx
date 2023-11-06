import React from 'react';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Unstable_Grid2';
import Typography from '@mui/material/Typography';
import AppWidgetSummary from 'app/components/AppWidgetSummary';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import { CorporateFare, CreditCard, ManageAccounts } from '@mui/icons-material';
import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';

// ----------------------------------------------------------------------

export default function PaginaInicialNew() {
  return (
    <Container maxWidth="xl">
      <div>
        <h4>Bem vindos ao app da Associação Atlética Philip Morris</h4>
        <div
          style={{
            fontSize: '0.9rem',
            color: 'text.disabled',
          }}
        >
          Aqui você pode fazer reservas para as sedes da associação, consultar convênios, visualizar o seu cartão de associado entre outros
          recursos disponíveis, click <a href={'/'}>aqui</a> e descubra.
        </div>
      </div>
      <br />

      <Grid container spacing={3}>
        <Grid xs={12} sm={6} md={3}>
          <AppWidgetSummary
            link={'/agenda'}
            title="Reservas"
            total={714000}
            color="success"
            icon={<EventAvailableIcon sx={{ fontSize: '8vh', color: '#1e77c5' }} />}
            sx={undefined}
          />
        </Grid>

        <Grid xs={12} sm={6} md={3}>
          <AppWidgetSummary
            link={'/cartao'}
            title="Cartão"
            total={1352831}
            color="info"
            icon={<CreditCard sx={{ fontSize: '8vh', color: '#1e77c5' }} />}
            sx={undefined}
          />
        </Grid>

        <Grid xs={12} sm={6} md={3}>
          <AppWidgetSummary
            link={'/convenio'}
            title="Convênios"
            total={1723315}
            color="warning"
            sx={undefined}
            icon={<CorporateFare sx={{ fontSize: '8vh', color: '#1e77c5' }} />}
          />
        </Grid>

        <Grid xs={12} sm={6} md={3}>
          <AppWidgetSummary
            link={'/account/settings'}
            title="Perfil"
            total={234}
            color="error"
            sx={undefined}
            icon={<ManageAccounts sx={{ fontSize: '8vh', color: '#1e77c5' }} />}
          />
        </Grid>
      </Grid>
      <br />
      <Grid xs={12} md={6} lg={8}>
        <Card component={Stack}>{/*<BannerCarousel/>*/}</Card>
      </Grid>
    </Container>
  );
}
// <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
//   <ModalInicial />
//   <CardLocais link={'/agenda'} title={'Agendamento'} icon={<EventAvailableIcon sx={{ fontSize: '7vh', color: 'white' }} />} />
//   <CardLocais link={'/cartao'} title={'Cartão de Sócio'} icon={<CreditCard sx={{ fontSize: '7vh', color: 'white' }} />} />
//   <CardLocais link={'/convenio'} title={'Convênios'} icon={<CorporateFare sx={{ fontSize: '7vh', color: 'white' }} />} />
//   <CardLocais link={'/account/settings'} title={'Perfil'} icon={<ManageAccounts sx={{ fontSize: '7vh', color: 'white' }} />} />
//   <CardLocais link={'/events'} title={'Eventos AAPM'} icon={<Event sx={{ fontSize: '7vh', color: 'white' }} />} />
//   <CardLocais link={'/sobre'} title={'Sobre'} icon={<Info sx={{ fontSize: '7vh', color: 'white' }} />} />
// </Grid>
