import * as React from 'react';
import './home.scss';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { useNavigate } from 'react-router-dom';

import { Card, CardActionArea, Grid } from '@mui/material';

export default function CardLocais({ link, title, icon }) {
  const navigate = useNavigate();
  // @ts-ignore
  return (
    <Grid item xs={4} sm={4} md={4} sx={{ marginTop: '5%' }}>
      <Card
        sx={{
          backgroundColor: '#1e77c5',
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
        sx={{
          marginTop: '18px',
          backgroundColor: 'gray-100',
          borderRadius: 2,
          borderWidth: '1px',
          boxShadow: 5,
          borderStyle: 'solid',
          borderColor: '#d5d5d5',
          ':hover': {
            boxShadow: 8,
            borderWidth: '1px',
            position: 'relative',
            borderColor: '#1e77c5',
          },
        }}
      >
        <CardActionArea>
          <CardContent
            onClick={() => navigate(link)}
            sx={{
              height: '30vh',
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
            }}
          >
            <Typography
              gutterBottom
              className={'MuiTypography-h1'}
              sx={{
                fontSize: '1.3rem',
                position: 'relative',
                fontWeight: '700',
                padding: '60px',
                textTransform: 'uppercase',
                color: '#242424',

                ':hover': {
                  // fontSize: '1.4rem',
                  // fontWeight: '1000',
                },
              }}
            >
              {title}
            </Typography>
          </CardContent>
        </CardActionArea>
      </Card>
    </Grid>
  );
}
