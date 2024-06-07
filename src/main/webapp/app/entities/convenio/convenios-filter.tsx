import React, { useEffect, useState } from 'react';
import { Button, Card, CardContent, CardMedia, Grid, Typography } from '@mui/material';
import { JhiItemCount, JhiPagination, ValidatedField } from 'react-jhipster';
import Breadcrunbs from 'app/components/breadcrunbs';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { useNavigate } from 'react-router-dom';
import { getEntities } from 'app/entities/convenio/convenio.reducer';
import Spinner from 'app/components/spinner';

const ConveniosFilter = () => {
  const [convenios, setConvenios] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [categoriaFiltro, setCategoriaFiltro] = useState('');
  const [buscaNome, setBuscaNome] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [conveniosPerPage] = useState(12); // Número de convênios por página
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const navigate = useNavigate();
  const [modal, setModal] = useState(false);
  const convenioList = useAppSelector(state => state.convenio.entities);
  const loadingConvenio = useAppSelector(state => state.convenio.loading);
  const dispatch = useAppDispatch();
  const [filter, setFilter] = useState('');

  useEffect(() => {
    dispatch(
      getEntities({
        page: 0,
        size: 999,
      })
    );
  }, []);

  useEffect(() => {
    const categoriasFromApi = convenioList.map(convenio => convenio.categoria);
    const uniqueCategorias = categoriasFromApi.filter((categoria, index, array) => array.findIndex(c => c.id === categoria.id) === index);
    setCategorias(uniqueCategorias);
  }, [convenioList]);

  const filterConvenios = () => {
    let filteredConvenios = [...convenioList];

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
      {!loadingConvenio ? (
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
            <Button
              variant={categoriaFiltro === '' ? 'contained' : 'text'}
              onClick={() => handleCategoriaClick('')}
              style={{ margin: '5px' }}
            >
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
              <>
                <Grid item key={convenio.id} xs={6} sm={4} md={3} xl={2}>
                  <Card
                    onClick={() => {
                      navigate(`/convenio/${convenio.id}`);
                    }}
                    className={'hand'}
                    sx={{
                      padding: '5px',
                      backgroundColor: 'gray-100',
                      borderRadius: 2,
                      borderWidth: '2px',
                      borderStyle: 'solid',
                      borderColor: '#a1a1a1',
                      ':hover': {
                        boxShadow: 7,
                        borderColor: '#1975d1',
                      },
                    }}
                  >
                    <CardMedia
                      component="img"
                      height="100"
                      image={`data:${convenio.imagemContentType};base64,${convenio.logo}`}
                      alt={convenio.nome}
                      sx={{ borderRadius: 2, paddingLeft: '20px', paddingRight: '20px' }}
                    />
                    <hr />
                    <CardContent>
                      <Typography variant="subtitle1">{convenio.nome}</Typography>
                      <Typography variant="subtitle2">{convenio.categoria.categoria}</Typography>
                    </CardContent>
                  </Card>
                </Grid>
              </>
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
      ) : (
        <Spinner text={'convênios'} />
      )}
      <br />
      <br />
    </div>
  );
};

export default ConveniosFilter;
