import * as React from 'react';
import '../home.scss';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { Link, useNavigate } from 'react-router-dom';

import { Card, CardActionArea, Grid } from '@mui/material';
import { CreditCard } from '@mui/icons-material';

export default function Cards({ link, title, icon, subtitle }) {
  const navigate = useNavigate();

  const cardStyle = {
    background: '#e63946', // Cor de fundo vermelha
    color: 'white', // Cor do texto branca
    fontWeight: 800,
    position: 'relative', // Para permitir posicionamento absoluto dentro do relativo
    minHeight: '20vh',
  };
  const linkStyle = {
    textDecoration: 'none',
    boxShadow: '10',
    border: '5px',
  };

  // @ts-ignore
  return (
    <Grid item xs={12} sm={6} md={4}>
      <Link to={link} style={linkStyle}>
        <Card sx={{ ...cardStyle, boxShadow: '8' }}>
          {icon}
          <CardContent sx={cardStyle}>
            <Typography variant="h5" sx={{ textTransform: 'uppercase' }} className={'titleCard'}>
              {title}
            </Typography>
            <Typography className={'subtitleCard'}>{subtitle}</Typography>
          </CardContent>
        </Card>
      </Link>
    </Grid>
    // <Grid item xs={4} sm={4} md={4} sx={{marginTop: '5%'}}>
    //   {/* Icone do card*/}
    //   <Card
    //     sx={{
    //       backgroundColor: '#1e77c5',
    //       display: 'flex',
    //       justifyContent: 'center',
    //       padding: 0.5,
    //       marginTop: '-18px',
    //       marginLeft: '11.px',
    //       position: 'absolute',
    //       borderRadius: 2,
    //       // boxShadow: 5,
    //       zIndex: 10,
    //       width: '13vh',
    //     }}
    //   >
    //     {icon}
    //   </Card>
    //
    //   <Card
    //     sx={{
    //       backgroundColor: 'gray-100',
    //       borderRadius: 2,
    //       borderWidth: '1px',
    //       boxShadow: 2,
    //       // borderStyle: 'solid',
    //       // borderColor: '#d5d5d5',
    //       ':hover': {
    //         boxShadow: 3,
    //         borderWidth: '1px',
    //         position: 'relative',
    //         // borderColor: '#1e77c5',
    //       },
    //     }}
    //   >
    //     <CardActionArea >
    //       <CardContent onClick={() => navigate(link)} sx={{
    //           height: '20vh',
    //           display: 'flex',
    //           justifyContent: 'center',
    //           flexWrap:"wrap",
    //           alignItems: 'center'}}>
    //         <Typography gutterBottom variant={"h5"} >
    //           {title}
    //         </Typography>
    //         <hr/>
    //
    //       </CardContent>
    //       <CardActionArea >
    //         <Typography gutterBottom variant={"subtitle2"}>
    //           {subtitle}
    //         </Typography>
    //       </CardActionArea>
    //     </CardActionArea>
    //   </Card>
    // </Grid>
  );
}
