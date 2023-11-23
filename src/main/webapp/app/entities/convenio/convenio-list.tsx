import React, { useEffect, useState } from 'react';
import { Button, Card, CardContent, CardMedia, TextField, Grid, Pagination, Typography } from '@mui/material';
import axios from 'axios';

import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import CardActions from '@mui/material/CardActions';

function ConveniosList() {
  const [convenios, setConvenios] = useState([]);
  const [conveniosTemp, setConveniosTemp] = useState([]);
  const [categoriaFiltro, setCategoriaFiltro] = useState(0);
  const [categoriaFiltradas, setCategoriaFiltradas] = useState([]);
  const [conveniosFiltrados, setConveniosFiltrados] = useState([]);

  const [page, setPage] = useState(1);
  const itemsPerPage = 8;
  const [categorias, setCategorias] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  async function handleSearch() {
    try {
      if (searchTerm === '') {
        const response = await axios.get(`api/convenios`);
        setConvenios(response.data);
        filterCategoria();
      } else {
        const response = await axios.get(`api/convenios?nome.contains=${searchTerm}`);
        setConvenios(response.data);
        filterCategoria();
      }
    } catch (error) {
      console.error('Erro ao buscar convênios:', error);
    }
  }

  async function fetchConvenios() {
    try {
      // const response = await axios.get(`api/convenios?categoriaId.equals=${categoria}`).then(res => {
      const response = await axios.get(`api/convenios`).then(res => {
        setConvenios(res.data);
      });
    } catch (error) {
      console.error('Erro ao buscar convênios:', error);
    }
  }

  async function fetchCategorias() {
    try {
      const response = await axios.get('api/categorias');
      setCategorias(response.data);
    } catch (error) {
      console.error('Erro ao buscar categorias:', error);
    }
  }

  useEffect(() => {
    filterConvenios(categoriaFiltro);
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    setConvenios(convenios.slice(startIndex, endIndex));
  }, [page]);

  useEffect(() => {
    fetchCategorias();
    filterCategoria();
    filterConvenios(categoriaFiltro);
  }, []);

  function handlePageChange(event, value) {
    setPage(value);
  }

  function filterConvenios(cate) {
    setCategoriaFiltro(cate);
    if (cate == 0) {
    } else {
      fetchConvenios().then(r => {
        setConveniosFiltrados(convenios.filter(value => value.categoria.id === cate));
      });
    }

    filterCategoria();
    setPage(1);
  }

  function filterCategoria() {
    if (categorias.length > 0) {
      const cate = categorias.filter(ca => convenios.some(co => ca.id === co.categoria.id));

      setCategoriaFiltradas(cate);
    }
  }

  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const loadingConvenio = useAppSelector(state => state.convenio.loading);
  const navigate = useNavigate();

  return (
    <div>
      {!loadingConvenio ? (
        <div>
          <Breadcrumb>
            <BreadcrumbItem onClick={() => navigate('/')}>
              <a>Início</a>
            </BreadcrumbItem>
            <BreadcrumbItem active>Convênios</BreadcrumbItem>
          </Breadcrumb>
          <div>
            <Grid container spacing={1} sx={{ dispay: 'flex', justifyContent: 'flex-end' }}>
              <Grid item>
                <TextField
                  label="Busca"
                  name={'busca'}
                  value={searchTerm}
                  variant="outlined"
                  size="small"
                  fullWidth
                  onChange={e => {
                    setSearchTerm(e.target.value);
                    handleSearch();
                  }}
                  type="text"
                  InputLabelProps={{
                    shrink: true,
                  }}
                />
              </Grid>
              <Grid item>
                <Button
                  type={'button'}
                  variant={'contained'}
                  color={'primary'}
                  // style={{ float: 'left', marginBottom: '10px', marginLeft: '10px' }}
                  onClick={handleSearch}
                >
                  Buscar
                </Button>
              </Grid>
              {isAdmin ? (
                <Grid item>
                  <Button
                    type={'button'}
                    variant={'contained'}
                    color={'primary'}
                    // style={{ float: 'right', marginBottom: '10px', marginRight: '10px' }}
                    onClick={() => navigate(`/convenio/new`)}
                  >
                    Novo
                  </Button>
                </Grid>
              ) : null}
            </Grid>
            <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
              <Grid item>
                <Button onClick={() => filterConvenios('')}>Todas</Button>
                {categorias.map(categoria => (
                  <Button key={categoria.id} onClick={() => filterConvenios(categoria.id)}>
                    {categoria.categoria}
                  </Button>
                ))}
              </Grid>
            </Grid>
            &nbsp;
          </div>
          <Grid container spacing={2}>
            {conveniosFiltrados.map(convenio => (
              <Grid item xs={12} sm={6} md={4} key={convenio.id}>
                <a>
                  <Card
                    onClick={() => {
                      navigate(`/convenio/${convenio.id}`);
                    }}
                  >
                    <CardMedia component="img" height="200" image={`data:${convenio.imagemContentType};base64,${convenio.imagem}`} />
                    <CardContent>
                      <Typography variant="h6" component="div">
                        {convenio.nome}
                      </Typography>
                      <Typography color="textSecondary">Categoria: {convenio.categoria.categoria}</Typography>
                    </CardContent>
                  </Card>
                </a>
              </Grid>
            ))}
          </Grid>
          <br />
          {/*<Pagination count={Math.ceil(convenios.length / itemsPerPage)} page={page} onChange={handlePageChange} color="primary" />*/}
        </div>
      ) : (
        <div>Carregando...</div>
      )}
    </div>
  );
}

export default ConveniosList;
