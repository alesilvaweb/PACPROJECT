import * as React from 'react';
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';

export default function Spinner({ action = 'Carregando', text = '' }) {
  return (
    <div>
      {action} {text} ...
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', marginTop: '10%' }}>
        <div className="lds-spinner">
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
        </div>
        {/*<div className="spinner">*/}
        {/*  <img width="200px" src="./content/images/logo-jhipster.png" alt="AAPM" />*/}
        {/*  <br/>*/}
        {/*  Carregando ...*/}
        {/*</div>*/}
      </Box>
    </div>
  );
}
