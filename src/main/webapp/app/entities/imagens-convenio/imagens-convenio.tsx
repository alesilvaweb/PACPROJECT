import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, getSortState, JhiItemCount, JhiPagination, openFile, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './imagens-convenio.reducer';
import isAdm from 'app/components/is-adm';

export const ImagensConvenio = () => {
  const dispatch = useAppDispatch();
  isAdm();
  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const imagensConvenioList = useAppSelector(state => state.imagensConvenio.entities);
  const loading = useAppSelector(state => state.imagensConvenio.loading);
  const totalItems = useAppSelector(state => state.imagensConvenio.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="imagens-convenio-heading" data-cy="ImagensConvenioHeading">
        <Translate contentKey="aapmApp.imagensConvenio.home.title">Imagens Convenios</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="aapmApp.imagensConvenio.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/imagens-convenio/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="aapmApp.imagensConvenio.home.createLabel">Create new Imagens Convenio</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {imagensConvenioList && imagensConvenioList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="aapmApp.imagensConvenio.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('titulo')}>
                  <Translate contentKey="aapmApp.imagensConvenio.titulo">Titulo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('descricao')}>
                  <Translate contentKey="aapmApp.imagensConvenio.descricao">Descricao</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('imagem')}>
                  <Translate contentKey="aapmApp.imagensConvenio.imagem">Imagem</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('created')}>
                  <Translate contentKey="aapmApp.imagensConvenio.created">Created</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('modified')}>
                  <Translate contentKey="aapmApp.imagensConvenio.modified">Modified</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="aapmApp.imagensConvenio.convenio">Convenio</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {imagensConvenioList.map((imagensConvenio, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/imagens-convenio/${imagensConvenio.id}`} color="link" size="sm">
                      {imagensConvenio.id}
                    </Button>
                  </td>
                  <td>{imagensConvenio.titulo}</td>
                  <td>{imagensConvenio.descricao}</td>
                  <td>
                    {imagensConvenio.imagem ? (
                      <div>
                        {imagensConvenio.imagemContentType ? (
                          <a onClick={openFile(imagensConvenio.imagemContentType, imagensConvenio.imagem)}>
                            <img
                              src={`data:${imagensConvenio.imagemContentType};base64,${imagensConvenio.imagem}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {imagensConvenio.imagemContentType}, {byteSize(imagensConvenio.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {imagensConvenio.created ? <TextFormat type="date" value={imagensConvenio.created} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {imagensConvenio.modified ? <TextFormat type="date" value={imagensConvenio.modified} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {imagensConvenio.convenio ? (
                      <Link to={`/convenio/${imagensConvenio.convenio.id}`}>{imagensConvenio.convenio.nome}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/imagens-convenio/${imagensConvenio.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/imagens-convenio/${imagensConvenio.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/imagens-convenio/${imagensConvenio.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="aapmApp.imagensConvenio.home.notFound">No Imagens Convenios found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={imagensConvenioList && imagensConvenioList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default ImagensConvenio;
