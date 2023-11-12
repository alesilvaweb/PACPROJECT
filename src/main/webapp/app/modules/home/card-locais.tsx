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
      {/* Icone do card*/}
      <Card
        sx={{
          backgroundColor: '#1e77c5',
          display: 'flex',
          justifyContent: 'center',
          padding: 0.5,
          marginTop: '-18px',
          marginLeft: '11.px',
          position: 'absolute',
          borderRadius: 2,
          // boxShadow: 5,
          zIndex: 10,
          width: '13vh',
        }}
      >
        {icon}
      </Card>

      <Card
        sx={{
          backgroundColor: 'gray-100',
          borderRadius: 2,
          borderWidth: '1px',
          boxShadow: 2,
          // borderStyle: 'solid',
          // borderColor: '#d5d5d5',
          ':hover': {
            boxShadow: 3,
            borderWidth: '1px',
            position: 'relative',
            // borderColor: '#1e77c5',
          },
        }}
      >
        <CardActionArea>
          <CardContent
            onClick={() => navigate(link)}
            sx={{
              height: '20vh',
              display: 'flex',
              justifyContent: 'center',
              flexWrap: 'wrap',
              alignItems: 'center',
            }}
          >
            <Typography
              gutterBottom
              variant={'subtitle1'}
              sx={{
                fontWeight: 600,
                fontSize: '2rem',
              }}
            >
              {title}
            </Typography>
            <CardActionArea>
              <Typography variant={'overline'}>{subtitle}</Typography>
            </CardActionArea>
          </CardContent>
        </CardActionArea>
      </Card>
    </Grid>
  );
}
