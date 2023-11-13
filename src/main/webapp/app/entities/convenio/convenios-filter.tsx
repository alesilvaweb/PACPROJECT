import React, { useEffect, useState } from 'react';
import { Button, Card, CardContent, CardMedia, Grid, Typography } from '@mui/material';
import axios from 'axios';
import { JhiItemCount, JhiPagination, ValidatedField } from 'react-jhipster';
import Breadcrunbs from 'app/components/breadcrunbs';
import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { useNavigate } from 'react-router-dom';

const ConveniosFilter = () => {
  const [convenios, setConvenios] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [categoriaFiltro, setCategoriaFiltro] = useState('');
  const [buscaNome, setBuscaNome] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [conveniosPerPage] = useState(6); // Número de convênios por página
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const navigate = useNavigate();
  useEffect(() => {
    // Carrega os dados da API ao montar o componente
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      // Substitua 'sua-api-aqui' pela URL real da sua API
      const response = await axios.get(`api/convenios`);

      // Extrai os dados da resposta
      const data = response.data;

      // Obtém as categorias únicas
      const categoriasFromApi = data.map(convenio => convenio.categoria);

      // Remover duplicatas com base no id da categoria
      const uniqueCategorias = categoriasFromApi.filter((categoria, index, array) => array.findIndex(c => c.id === categoria.id) === index);

      setCategorias(uniqueCategorias);

      // Define os convênios
      setConvenios(data);
    } catch (error) {
      console.error('Erro ao buscar dados da API', error);
    }
  };

  const filterConvenios = () => {
    let filteredConvenios = [...convenios];

    // Filtra por categoria
    if (categoriaFiltro !== '') {
      filteredConvenios = filteredConvenios.filter(convenio => convenio.categoria.id === categoriaFiltro);
    }

    // Filtra por nome
    if (buscaNome !== '') {
      filteredConvenios = filteredConvenios.filter(convenio => convenio.nome.toLowerCase().includes(buscaNome.toLowerCase()));
    }
    return filteredConvenios;
  };

  // Lógica para calcular os índices dos convênios a serem exibidos
  const indexOfLastConvenio = currentPage * conveniosPerPage;
  const indexOfFirstConvenio = indexOfLastConvenio - conveniosPerPage;
  const currentConvenios = filterConvenios().slice(indexOfFirstConvenio, indexOfLastConvenio);

  const handleCategoriaClick = categoria => {
    setCategoriaFiltro(categoria === categoriaFiltro ? '' : categoria);
  };

  const handleBuscaNomeChange = event => {
    if (categoriaFiltro != '') {
      handleCategoriaClick('');
    }
    setBuscaNome(event.target.value);
  };

  // Lógica para mudar de página
  const paginate = pageNumber => setCurrentPage(pageNumber);

  return (
    <div>
      <Breadcrunbs atual={'Convênios'} />

      <Grid container spacing={2}>
        <Grid item md={4} xs={9} sm={6}>
          <ValidatedField
            value={buscaNome}
            placeholder={'Busca'}
            onChange={handleBuscaNomeChange}
            style={{ padding: '5px' }}
            name={'busca'}
          />
        </Grid>
        <Grid item md={6} xs={1} sm={4}></Grid>
        {isAdmin ? (
          <Grid item md={2} xs={2} sm={2}>
            <Button
              type={'button'}
              variant={'contained'}
              color={'primary'}
              style={{ float: 'right' }}
              onClick={() => navigate(`/convenio/new`)}
            >
              Novo
            </Button>
          </Grid>
        ) : null}
      </Grid>
      <div style={{ display: 'flex', flexWrap: 'wrap' }}>
        <Button variant={categoriaFiltro === '' ? 'contained' : 'text'} onClick={() => handleCategoriaClick('')} style={{ margin: '5px' }}>
          Todas
        </Button>
        {categorias.map(categoria => (
          <Button
            key={categoria.id}
            size={'small'}
            variant={categoria.id === categoriaFiltro ? 'contained' : 'text'}
            onClick={() => handleCategoriaClick(categoria.id)}
            style={{ margin: '3px' }}
          >
            {categoria.categoria}
          </Button>
        ))}
      </div>
      <hr />

      <Grid container spacing={2}>
        {currentConvenios.map(convenio => (
          <Grid item key={convenio.id} xs={6} sm={4} md={3}>
            <Card
              onClick={() => {
                navigate(`/convenio/${convenio.id}`);
              }}
              className={'hand'}
            >
              <CardMedia
                component="img"
                height="140"
                image={`data:${convenio.imagemContentType};base64,${convenio.imagem}`}
                alt={convenio.nome}
              />
              <CardContent>
                <Typography variant="h6">{convenio.nome}</Typography>
                <Typography>{convenio.categoria.categoria}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      <br />
      {filterConvenios().length > 0 ? (
        <>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={currentPage} total={filterConvenios().length} itemsPerPage={conveniosPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              totalItems={filterConvenios().length}
              activePage={currentPage}
              itemsPerPage={conveniosPerPage}
              onSelect={page => paginate(page)}
              maxButtons={5}
            />
          </div>
        </>
      ) : null}
    </div>
  );
};

export default ConveniosFilter;