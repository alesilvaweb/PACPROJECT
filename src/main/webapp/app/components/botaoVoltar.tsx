import React from 'react';
import { ArrowBackIos } from '@mui/icons-material';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';
import { Chip } from '@mui/material';

const BotaoVoltar = ({ link, label }) => {
  const navigate = useNavigate();
  return (
    <>
      <Chip
        avatar={
          <>
            <ArrowBackIos sx={{ paddingLeft: '4px' }} />
            {/*<Avatar sx={{ backgroundColor: `${locaisEntity.cor}` }}>{locaisEntity.nome[0]}</Avatar>*/}
          </>
        }
        sx={{
          fontSize: '0.9rem',
          padding: '1vh',
          fontWeight: 500,
          marginBottom: '2px',
          borderRadius: '5px',
        }}
        onClick={() => {
          navigate(link);
        }}
        label={label}
        color={'info'}
      />
    </>
  );
};

export default BotaoVoltar;
