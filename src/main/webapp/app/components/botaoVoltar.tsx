import React from 'react';
import { ArrowBackIos } from '@mui/icons-material';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';

const BotaoVoltar = ({ link, top }) => {
  const navigate = useNavigate();
  return (
    <>
      <Button
        variant="contained"
        color={'primary'}
        onClick={() => navigate(link)}
        startIcon={<ArrowBackIos />}
        sx={{
          position: 'fixed',
          marginTop: top,
          zIndex: '1',
        }}
      >
        Voltar
      </Button>
    </>
  );
};

export default BotaoVoltar;
