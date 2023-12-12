import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Breadcrumb, BreadcrumbItem, Button, Table } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, openFile, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './arquivo.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import isAdm from 'app/components/is-adm';

export const Arquivo = () => {
  isAdm();
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const arquivoList = useAppSelector(state => state.arquivo.entities);
  const loading = useAppSelector(state => state.arquivo.loading);
  const totalItems = useAppSelector(state => state.arquivo.totalItems);

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
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem onClick={() => navigate('/associado')}>
          <a>Associados</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>{'Importação'}</BreadcrumbItem>
      </Breadcrumb>

      <h4 id="arquivo-heading" data-cy="ArquivoHeading">
        Importação de associados
        <div className="d-flex justify-content-end">
          {/*<Link to="/associado" className="btn btn-primary jh-create-entity">*/}
          {/*  Associados*/}
          {/*</Link>*/}
          {/*&nbsp;*/}
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            {/*<Translate contentKey="aapmApp.arquivo.home.refreshListLabel">Refresh List</Translate>*/}
          </Button>
          <Link to="/arquivo/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            {/*<Translate contentKey="aapmApp.arquivo.home.createLabel">Create new Arquivo</Translate>*/}
          </Link>
        </div>
      </h4>
      <div className="table-responsive">
        {arquivoList && arquivoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/*<th className="hand" onClick={sort('id')}>*/}
                {/*  <Translate contentKey="aapmApp.arquivo.id">ID</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="aapmApp.arquivo.nome">Nome</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('descricao')}>
                  <Translate contentKey="aapmApp.arquivo.descricao">Descricao</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('arquivo')}>
                  <Translate contentKey="aapmApp.arquivo.arquivo">Arquivo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="aapmApp.arquivo.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('created')}>*/}
                {/*  <Translate contentKey="aapmApp.arquivo.created">Created</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('modified')}>*/}
                {/*  <Translate contentKey="aapmApp.arquivo.modified">Modified</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th />
              </tr>
            </thead>
            <tbody>
              {arquivoList.map((arquivo, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/*<td>*/}
                  {/*  <Button tag={Link} to={`/arquivo/${arquivo.id}`} color="link" size="sm">*/}
                  {/*    {arquivo.id}*/}
                  {/*  </Button>*/}
                  {/*</td>*/}
                  <td>{arquivo.nome}</td>
                  <td>{arquivo.descricao}</td>
                  <td>
                    {arquivo.arquivo ? (
                      <div>
                        {arquivo.arquivoContentType ? (
                          <a onClick={openFile(arquivo.arquivoContentType, arquivo.arquivo)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        {/*<span>*/}
                        {/*  {arquivo.arquivoContentType}, {byteSize(arquivo.arquivo)}*/}
                        {/*</span>*/}
                      </div>
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`aapmApp.StatusArquivo.${arquivo.status}`} />
                  </td>
                  {/*<td>{arquivo.created ? <TextFormat type="date" value={arquivo.created} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  {/*<td>{arquivo.modified ? <TextFormat type="date" value={arquivo.modified} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {/*<Button tag={Link} to={`/arquivo/${arquivo.id}`} color="info" size="sm" data-cy="entityDetailsButton">*/}
                      {/*  <FontAwesomeIcon icon="eye" />{' '}*/}
                      {/*  <span className="d-none d-md-inline">/!*<Translate contentKey="entity.action.view">View</Translate>*!/</span>*/}
                      {/*</Button>*/}
                      {/*<Button*/}
                      {/*  tag={Link}*/}
                      {/*  to={`/arquivo/${arquivo.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}*/}
                      {/*  color="primary"*/}
                      {/*  size="sm"*/}
                      {/*  data-cy="entityEditButton"*/}
                      {/*>*/}
                      {/*  <FontAwesomeIcon icon="pencil-alt" />{' '}*/}
                      {/*  <span className="d-none d-md-inline">/!*<Translate contentKey="entity.action.edit">Edit</Translate>*!/</span>*/}
                      {/*</Button>*/}
                      <Button
                        tag={Link}
                        to={`/arquivo/${arquivo.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="aapmApp.arquivo.home.notFound">No Arquivos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={arquivoList && arquivoList.length > 0 ? '' : 'd-none'}>
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

export default Arquivo;
