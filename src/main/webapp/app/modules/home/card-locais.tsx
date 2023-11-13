import * as React from 'react';
import './home.scss';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { useNavigate } from 'react-router-dom';

import { Card, CardActionArea, Grid } from '@mui/material';

export default function CardLocais({ link, title, icon, subtitle }) {
  const navigate = useNavigate();
  // @ts-ignore
  return (
    <Grid item xs={4} sm={4} md={4} sx={{ marginTop: '5%' }}>
      <Card
        sx={{
          backgroundColor: '#1975d1',
          display: 'flex',
          justifyContent: 'center',
          padding: 0.5,
          marginTop: '-15px',
          marginLeft: '15px',
          position: 'absolute',
          borderRadius: 2,
          boxShadow: 5,
          zIndex: 10,
          width: '13vh',
        }}
      >
        {icon}
      </Card>
      <Card
        className={'hand'}
        sx={{
          backgroundColor: 'gray-100',
          borderRadius: 2,
          borderWidth: '1px',
          borderStyle: 'solid',
          borderColor: '#a1a1a1',
          ':hover': {
            boxShadow: 5,
            borderColor: '#1975d1',
          },
        }}
      >
        <CardContent
          onClick={() => navigate(link)}
          sx={{
            height: '30vh',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            flexWrap: 'wrap',
          }}
        >
          <Typography
            gutterBottom
            className={'MuiTypography-h1'}
            sx={{
              fontSize: '1.3rem',
              fontWeight: '600',
              textTransform: 'uppercase',
              color: '#242424',
            }}
          >
            {title}
          </Typography>

          <CardActionArea
            sx={{
              borderRadius: 2,
              padding: '10px',
              height: '15vh',

              ':hover': {
                backgroundColor: 'rgba(255,255,255,0.33)',
              },
            }}
          >
            <Typography>{subtitle}</Typography>
          </CardActionArea>
        </CardContent>
      </Card>
    </Grid>
  );
}
