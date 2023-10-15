import './home.scss';

import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, CardTitle, Container, Row } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
// import CardLocais from 'app/modules/home/card-locais';
import { Grid, Paper, styled } from '@mui/material';
import { getEntities } from 'app/entities/local/local.reducer';
import CallendarAapm from 'app/modules/home/callendar';
import CardLocais from 'app/modules/home/card-locais';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  colyamahanoor: theme.palette.text.secondary,
}));

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const locaisList = useAppSelector(state => state.local.entities);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  //Carregamento das sedes
  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: 1,
        size: 1,
      })
    );
  };

  // Executa função para carregamento das sedes se o usuário estiver logado
  if (account?.login) {
    useEffect(() => {
      getAllEntities();
    }, []);
  } else {
    navigate('/login');
  }

  return (
    <Row>
      {account?.login ? (
        <Container>
          {/*<CardTitle></CardTitle>*/}
          {/*<Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 2, sm: 8, md: 12 }}>*/}
          {/*  {locaisList.map((locais, i) => (*/}
          {/*    <Grid key={locais.id} item xs={2} sm={4} md={4} justifyContent="center" display={'flex'}>*/}
          {/*      /!* Chama a view de seleção de locais*!/*/}
          {/*      <CardLocais*/}
          {/*        image={`data:${locais.imagenContentType};base64,${locais.imagen}`}*/}
          {/*        text={locais.descricao}*/}
          {/*        title={locais.nome}*/}
          {/*        id={locais.id}*/}
          {/*        valor={locais.valor}*/}
          {/*        capacidade={locais.capacidade}*/}
          {/*      />*/}
          {/*    </Grid>*/}
          {/*  ))}*/}
          {/*</Grid>*/}
        </Container>
      ) : (
        <div>
          <Alert color="warning">
            <Link to="/login" className="alert-link">
              Entrar
            </Link>
          </Alert>
        </div>
      )}
    </Row>
  );
};

export default Home;
