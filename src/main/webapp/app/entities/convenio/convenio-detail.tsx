import React, { useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import CardMedia from '@mui/material/CardMedia';
import Grid from '@mui/material/Grid';
import IconButton from '@mui/material/IconButton';
import FacebookIcon from '@mui/icons-material/Facebook';
import axios from 'axios';
import { Image, Instagram, WhatsApp } from '@mui/icons-material';
// Importe outros ícones das redes sociais, se necessário
import { getEntity } from './convenio.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate, useParams } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem, CardFooter } from 'reactstrap';
import Stack from '@mui/material/Stack';
import { Button, Paper } from '@mui/material';
import Divider from '@mui/material/Divider';
import { styled } from '@mui/material/styles';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#e43922',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',

  color: '#ffffff',
}));

function ConvenioDetalhe() {
  const dispatch = useAppDispatch();

  const [desconto, setDesconto] = useState([]);
  const [redeSocial, setRedeSocial] = useState([]);
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  async function fetchDescontos(id) {
    try {
      const response = await axios.get(`api/desconto-convenios?convenioId.equals=${id}`);
      setDesconto(response.data);
    } catch (error) {
      console.error('Erro ao buscar Descontos:', error);
    }
  }

  async function fetchRedesSociais(id) {
    try {
      const response = await axios.get(`api/redes-sociais-convenios?convenioId.equals=${id}`);
      setRedeSocial(response.data);
    } catch (error) {
      console.error('Erro ao buscar redes Sociais:', error);
    }
  }

  useEffect(() => {
    fetchDescontos(id);
    fetchRedesSociais(id);
  }, [id]);

  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  const convenioEntity = useAppSelector(state => state.convenio.entity);
  const navigate = useNavigate();
  return (
    <div>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/convenio/list')}>
          <a>Convênios</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{convenioEntity.nome}</BreadcrumbItem>
      </Breadcrumb>
      <Card raised sx={{ margin: '0 auto', padding: '0.1em' }}>
        <CardMedia
          component="img"
          alt={convenioEntity.nome}
          height="200"
          image={`data:${convenioEntity.imagemContentType};base64,${convenioEntity.imagem}`}
          title={convenioEntity.nome}
          sx={{
            objectFit: 'contain',
            maxWidth: '400px',
            width: '100%',
            height: '100%',
            float: 'right',
            borderRadius: '4px',
            boxShadow: 5,
            marginRight: '10px',
          }}
        />
        <CardContent>
          {convenioEntity.logo ? (
            <Typography variant="h4" component="div">
              <img
                src={`data:${convenioEntity.logoContentType};base64,${convenioEntity.logo}`}
                style={{ maxHeight: '50px', borderRadius: '4px', marginTop: '10px', marginBottom: '5px' }}
              />{' '}
              {convenioEntity.nome}
            </Typography>
          ) : (
            <Typography variant="h4" component="div">
              {convenioEntity.nome}
            </Typography>
          )}
          <Typography variant="subtitle2" component="div">
            {convenioEntity.titulo}
          </Typography>

          <hr />
          <Typography variant="subtitle2" component="div">
            {convenioEntity.endereco}
          </Typography>
          <Typography variant="subtitle2" component="div">
            Fone: {convenioEntity.telefone}
          </Typography>
          <Typography variant="subtitle2" component="div">
            {convenioEntity.email}
          </Typography>
          <hr />
          <Typography variant="button" color="textSecondary" sx={{ display: 'flex', alignItems: 'center' }}>
            <Typography variant="subtitle2" component="div">
              Desconto:
            </Typography>

            {desconto.map(desc => (
              <>
                &nbsp;
                <Stack
                  direction="row"
                  divider={<Divider orientation="vertical" flexItem />}
                  spacing={{ xs: 1, sm: 2, md: 4 }}
                  key={desc.id}
                >
                  <Item>
                    &nbsp;{desc.desconto}% {desc.descricao} &nbsp;
                  </Item>
                </Stack>
                &nbsp;
              </>
            ))}
          </Typography>
          <hr />
          <Grid container spacing={2}>
            {redeSocial.map(rede => (
              <Grid item key={rede.id}>
                <IconButton
                  color="primary"
                  aria-label={rede.nome}
                  href={rede.endereco}
                  target="_blank"
                  style={{ fontSize: '15px', float: 'right' }}
                >
                  {rede.icon?.nome === 'Facebook' ? (
                    <>
                      <FacebookIcon /> {rede.endereco} &nbsp;
                    </>
                  ) : rede.icon?.nome === 'WhatsApp' ? (
                    <>
                      <WhatsApp /> {rede.endereco} &nbsp;
                    </>
                  ) : rede.icon?.nome === 'Instagram' ? (
                    <>
                      <Instagram /> {rede.endereco} &nbsp;
                    </>
                  ) : null}
                </IconButton>
              </Grid>
            ))}
          </Grid>
        </CardContent>
        <hr />
        {isAdmin ? (
          <CardFooter>
            <Button
              type={'button'}
              color={'primary'}
              style={{ float: 'right', marginBottom: '10px', marginRight: '10px' }}
              onClick={() => navigate(`/convenio/${convenioEntity.id}/edit`)}
            >
              Editar
            </Button>
            <Button
              type={'button'}
              color={'error'}
              style={{ float: 'right', marginBottom: '10px', marginRight: '10px' }}
              onClick={() => navigate(`/convenio/${convenioEntity.id}/delete?page=1&sort=id,asc`)}
            >
              Excluir
            </Button>
          </CardFooter>
        ) : null}
        <Button
          type={'button'}
          color={'primary'}
          style={{ float: 'left', marginBottom: '10px', marginLeft: '10px' }}
          onClick={() => navigate(`/convenio/list`)}
        >
          voltar
        </Button>
      </Card>
    </div>
  );
}

export default ConvenioDetalhe;
