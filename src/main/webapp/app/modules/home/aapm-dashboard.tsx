import React from 'react';
import { Link } from 'react-router-dom';
import { Card, CardContent, Typography, Grid } from '@mui/material';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/swiper-bundle.css';

import { AccessTime, CreditCard, LocalHospital } from '@mui/icons-material';
import { write } from 'xlsx';

const cardStyle = {
  background: '#e63946', // Cor de fundo vermelha
  color: 'white', // Cor do texto branca
  position: 'relative', // Para permitir posicionamento absoluto dentro do relativo
};

const iconStyle = {
  fontSize: 48, // Ajuste o tamanho do ícone conforme necessário
  // position: 'absolute', // Posicionamento absoluto
  color: 'write',
  marginTop: 1, // Distância do topo
  marginLeft: 1, // Distância da esquerda
};

const SwiperBanner = () => {
  const newsData = [
    {
      title: 'Novidade 1',
      description: 'Descrição da novidade 1.',
      link: '/detalhes/novidade-1',
    },
    {
      title: 'Novidade 2',
      description: 'Descrição da novidade 2.',
      link: '/detalhes/novidade-2',
    },
    {
      title: 'Novidade 3',
      description: 'Descrição da novidade 2.',
      link: '/detalhes/novidade-2',
    },
    {
      title: 'Novidade 4',
      description: 'Descrição da novidade 2.',
      link: '/detalhes/novidade-2',
    },
    {
      title: 'Novidade 5',
      description: 'Descrição da novidade 2.',
      link: '/detalhes/novidade-2',
    },
    // Adicione mais notícias conforme necessário
  ];

  return (
    <Swiper
      spaceBetween={50}
      slidesPerView={1}
      // loop={true}
      // pagination={{ clickable: true }}
      onAutoplay={true}
      autoplay={{ delay: 3000 }}
      navigation={true}
    >
      {newsData.map((news, index) => (
        <SwiperSlide key={index}>
          <Link to={news.link} style={{ textDecoration: 'none' }}>
            <div
              style={{
                background: '#323232', // Cor de fundo azul escuro
                padding: '20px',
                borderRadius: '8px',
                color: 'white',
                textAlign: 'center',
              }}
            >
              <Typography variant="h5" gutterBottom>
                {news.title}
              </Typography>
              <Typography>{news.description}</Typography>
              <Typography style={{ marginTop: '10px', textDecoration: 'underline' }}>Ver detalhes</Typography>
            </div>
          </Link>
        </SwiperSlide>
      ))}
    </Swiper>
  );
};

const AAPMDashboard = () => {
  return (
    <div>
      <Typography variant="h4" gutterBottom sx={{ color: 'black' }}>
        Bem-vindos ao App da AAPM
      </Typography>
      <Typography variant="subtitle1" paragraph sx={{ color: 'black' }}>
        Explore os recursos exclusivos para membros da AAPM e aproveite ao máximo sua associação.
      </Typography>
      <Grid container spacing={3}>
        {/* Reservas */}
        <Grid item xs={12} sm={6} md={4}>
          <Link to="/cabanas" style={{ textDecoration: 'none' }}>
            <Card sx={cardStyle}>
              <AccessTime sx={iconStyle} />
              <CardContent sx={{ ...cardStyle }}>
                <Typography variant="h5">Reservas</Typography>
                <Typography>Faça reservas para eventos e atividades da AAPM aqui.</Typography>
              </CardContent>
            </Card>
          </Link>
        </Grid>

        {/* Cartões da AAPM */}
        <Grid item xs={12} sm={6} md={4}>
          <Link to="/cartao" style={{ textDecoration: 'none' }}>
            <Card sx={cardStyle}>
              <CreditCard sx={iconStyle} />
              <CardContent sx={{ ...cardStyle }}>
                <Typography variant="h5">Cartões da AAPM</Typography>
                <Typography>Gerencie seus cartões de associação da AAPM nesta seção.</Typography>
              </CardContent>
            </Card>
          </Link>
        </Grid>

        {/* Convênios */}
        <Grid item xs={12} sm={6} md={4}>
          <Link to="/convenio/list" style={{ textDecoration: 'none' }}>
            <Card sx={cardStyle}>
              <LocalHospital sx={iconStyle} />
              <CardContent sx={{ ...cardStyle }}>
                <Typography variant="h5">Convênios</Typography>
                <Typography>Descubra os convênios disponíveis para os membros da AAPM.</Typography>
              </CardContent>
            </Card>
          </Link>
        </Grid>
      </Grid>

      <Grid container spacing={3} style={{ marginTop: '20px' }}>
        <Grid item xs={12}>
          <SwiperBanner />
        </Grid>
      </Grid>
    </div>
  );
};

export default AAPMDashboard;
