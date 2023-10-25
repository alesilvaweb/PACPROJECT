import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IConvenio } from 'app/shared/model/convenio.model';
import { getEntities } from './convenio.reducer';

export const Convenio = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const convenioList = useAppSelector(state => state.convenio.entities);
  const loading = useAppSelector(state => state.convenio.loading);
  const totalItems = useAppSelector(state => state.convenio.totalItems);

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
      <h2 id="convenio-heading" data-cy="ConvenioHeading">
        <Translate contentKey="aapmApp.convenio.home.title">Convenios</Translate>
        <div className="d-flex justify-content-end">
          <Link to="/categoria" className="btn btn-primary jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Categorias
          </Link>
          &nbsp;
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            {/*<Translate contentKey="aapmApp.convenio.home.refreshListLabel">Refresh List</Translate>*/}
          </Button>
          <Link to="/convenio/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />

            {/*<Translate contentKey="aapmApp.convenio.home.createLabel">Create new Convenio</Translate>*/}
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {convenioList && convenioList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/*<th className="hand" onClick={sort('id')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.id">ID</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="aapmApp.convenio.nome">Nome</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('titulo')}>
                  <Translate contentKey="aapmApp.convenio.titulo">Titulo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('descricao')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.descricao">Descricao</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('endereco')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.endereco">Endereco</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('telefone')}>
                  <Translate contentKey="aapmApp.convenio.telefone">Telefone</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('email')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.email">Email</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('imagem')}>
                  <Translate contentKey="aapmApp.convenio.imagem">Imagem</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('logo')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.logo">Logo</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('banner')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.banner">Banner</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('localizacao')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.localizacao">Localizacao</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="aapmApp.convenio.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('created')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.created">Created</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('modified')}>*/}
                {/*  <Translate contentKey="aapmApp.convenio.modified">Modified</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th>
                  <Translate contentKey="aapmApp.convenio.categoria">Categoria</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {convenioList.map((convenio, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/*<td>*/}
                  {/*  <Button tag={Link} to={`/convenio/${convenio.id}`} color="link" size="sm">*/}
                  {/*    {convenio.id}*/}
                  {/*  </Button>*/}
                  {/*</td>*/}
                  <td>{convenio.nome}</td>
                  <td>{convenio.titulo}</td>
                  {/*<td>{convenio.descricao}</td>*/}
                  {/*<td>{convenio.endereco}</td>*/}
                  <td>{convenio.telefone}</td>
                  {/*<td>{convenio.email}</td>*/}
                  <td>
                    {convenio.imagem ? (
                      <div>
                        {convenio.imagemContentType ? (
                          <a onClick={openFile(convenio.imagemContentType, convenio.imagem)}>
                            <img src={`data:${convenio.imagemContentType};base64,${convenio.imagem}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {convenio.imagemContentType}, {byteSize(convenio.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  {/*<td>*/}
                  {/*  {convenio.logo ? (*/}
                  {/*    <div>*/}
                  {/*      {convenio.logoContentType ? (*/}
                  {/*        <a onClick={openFile(convenio.logoContentType, convenio.logo)}>*/}
                  {/*          <img src={`data:${convenio.logoContentType};base64,${convenio.logo}`} style={{ maxHeight: '30px' }} />*/}
                  {/*          &nbsp;*/}
                  {/*        </a>*/}
                  {/*      ) : null}*/}
                  {/*      <span>*/}
                  {/*        {convenio.logoContentType}, {byteSize(convenio.logo)}*/}
                  {/*      </span>*/}
                  {/*    </div>*/}
                  {/*  ) : null}*/}
                  {/*</td>*/}
                  {/*<td>*/}
                  {/*  {convenio.banner ? (*/}
                  {/*    <div>*/}
                  {/*      {convenio.bannerContentType ? (*/}
                  {/*        <a onClick={openFile(convenio.bannerContentType, convenio.banner)}>*/}
                  {/*          <img src={`data:${convenio.bannerContentType};base64,${convenio.banner}`} style={{ maxHeight: '30px' }} />*/}
                  {/*          &nbsp;*/}
                  {/*        </a>*/}
                  {/*      ) : null}*/}
                  {/*      <span>*/}
                  {/*        {convenio.bannerContentType}, {byteSize(convenio.banner)}*/}
                  {/*      </span>*/}
                  {/*    </div>*/}
                  {/*  ) : null}*/}
                  {/*</td>*/}
                  {/*<td>{convenio.localizacao}</td>*/}
                  {/*<td>*/}
                  {/*  <Translate contentKey={`aapmApp.Status.${convenio.status}`} />*/}
                  {/*</td>*/}
                  {/*<td>{convenio.created ? <TextFormat type="date" value={convenio.created} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  {/*<td>{convenio.modified ? <TextFormat type="date" value={convenio.modified} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  <td>
                    {convenio.categoria ? <Link to={`/categoria/${convenio.categoria.id}`}>{convenio.categoria.categoria}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/convenio/${convenio.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.view">View</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/convenio/${convenio.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.edit">Edit</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/convenio/${convenio.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.delete">Delete</Translate>*/}</span>
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
              <Translate contentKey="aapmApp.convenio.home.notFound">No Convenios found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={convenioList && convenioList.length > 0 ? '' : 'd-none'}>
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

export default Convenio;
