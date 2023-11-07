import { Card, CardContent, Typography } from '@mui/material';
import React from 'react';

const AssociadoCard = ({ name, memberSince, imageUrl }) => {
  const cardStyle = {
    backgroundImage: `url(${imageUrl})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    width: '150vh',
    height: '100%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: 'black',
    textAlign: 'center',
  };

  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: 'black',
        textAlign: 'center',
      }}
    >
      <Card sx={cardStyle}>
        <CardContent>
          <Typography variant="h5" component="div" sx={{ marginTop: '300px' }}>
            {name}
          </Typography>
          <Typography variant="subtitle1" color="inherit" sx={{ marginTop: '100px' }}>
            {/*Associado desde {memberSince}*/}
          </Typography>
        </CardContent>
      </Card>
    </div>
  );
};

export default AssociadoCard;
