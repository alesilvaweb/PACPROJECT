import * as React from 'react';
import './home.scss';
import SendIcon from '@mui/icons-material/Send';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { Link, useNavigate } from 'react-router-dom';

import { Card, CardActionArea, CardHeader, Grid, Paper } from '@mui/material';
import Button from '@mui/material/Button';

export default function CardLocais({ link, title, icon }) {
  const navigate = useNavigate();
  // @ts-ignore
  return (
    <Grid item xs={4} sm={4} md={4} sx={{ marginTop: '3%' }}>
      <Card
        sx={{
          backgroundColor: '#1975d1',
          display: 'flex',
          padding: 0.5,
          marginTop: '-15px',
          marginLeft: '15px',
          position: 'absolute',
          justifyContent: 'center',
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
          backgroundColor: '#fafafa',
          borderRadius: 2,
          borderWidth: '1px',
          borderStyle: 'solid',
          borderColor: '#a1a1a1',

          ':hover': {
            boxShadow: 10,
            position: 'relative',
            borderColor: '#1975d1',
          },
        }}
      >
        <CardActionArea>
          <CardContent onClick={() => navigate(link)} sx={{ height: '30vh', textAlign: 'center' }}>
            <Typography
              gutterBottom
              className={'MuiTypography-h1'}
              sx={{
                fontSize: '1.3rem',
                position: 'relative',
                fontWeight: '1000',
                // marginTop:"2vh" ,
                padding: '60px',
                // backgroundColor:"red",
                textTransform: 'uppercase',
                color: '#212121',
                ':hover': {
                  fontSize: '1.4rem',
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
