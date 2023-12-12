import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, Table } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, openFile, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './local.reducer';
import Breadcrunbs from 'app/components/breadcrunbs';
import isAdm from 'app/components/is-adm';

export const Local = () => {
  isAdm();
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const localList = useAppSelector(state => state.local.entities);
  const loading = useAppSelector(state => state.local.loading);
  const totalItems = useAppSelector(state => state.local.totalItems);
  const [filterNome, setFilterNome] = useState('');

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        filter: `nome.contains=${filterNome}`,
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
  }, [paginationState.activePage, paginationState.order, paginationState.sort, filterNome]);

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
      <Breadcrunbs atual={'Locais'} />
      <h3 id="local-heading" data-cy="LocalHeading">
        <Translate contentKey="aapmApp.local.home.title">Locals</Translate>
      </h3>

      <div className="d-flex justify-content-between">
        <div className="col-md-4 col-sm-8 col-xl-4">
          <Input
            type={'text'}
            name={'busca'}
            onChange={e => {
              setFilterNome(e.target.value);
            }}
            placeholder={'Buscar'}
          />
        </div>

        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />
            {/*<Translate contentKey="aapmApp.local.home.refreshListLabel">Refresh List</Translate>*/}
          </Button>
          <Link to="/local/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            {/*<Translate contentKey="aapmApp.local.home.createLabel">Create new Local</Translate>*/}
          </Link>
        </div>
      </div>

      <div className="table-responsive">
        {localList && localList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/*<th className="hand" onClick={sort('id')}>*/}
                {/*  <Translate contentKey="aapmApp.local.id">ID</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="aapmApp.local.nome">Nome</Translate>&nbsp; <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('descricao')}>
                  <Translate contentKey="aapmApp.local.descricao">Descricao</Translate>&nbsp; <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('capacidade')}>
                  <Translate contentKey="aapmApp.local.capacidade">Capacidade</Translate>&nbsp; <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('imagen')}>
                  <Translate contentKey="aapmApp.local.imagen">Imagen</Translate>&nbsp; <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('observacoes')}>*/}
                {/*  <Translate contentKey="aapmApp.local.observacoes">Observacoes</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('localizacao')}>*/}
                {/*  <Translate contentKey="aapmApp.local.localizacao">Localizacao</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('valor')}>
                  <Translate contentKey="aapmApp.local.valor">Valor</Translate>&nbsp; <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="aapmApp.local.status">Status</Translate>&nbsp; <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('cor')}>*/}
                {/*  <Translate contentKey="aapmApp.local.cor">Cor</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('created')}>*/}
                {/*  <Translate contentKey="aapmApp.local.created">Created</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('modified')}>*/}
                {/*  <Translate contentKey="aapmApp.local.modified">Modified</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th />
              </tr>
            </thead>
            <tbody>
              {localList.map((local, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/*<td>*/}
                  {/*  <Button tag={Link} to={`/local/${local.id}`} color="link" size="sm">*/}
                  {/*    {local.id}*/}
                  {/*  </Button>*/}
                  {/*</td>*/}
                  <td>{local.nome}</td>
                  <td>{local.descricao}</td>
                  <td>{local.capacidade}</td>
                  <td>
                    {local.imagen ? (
                      <div>
                        {local.imagenContentType ? (
                          <a onClick={openFile(local.imagenContentType, local.imagen)}>
                            <img src={`data:${local.imagenContentType};base64,${local.imagen}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        {/*<span>*/}
                        {/*  {local.imagenContentType}, {byteSize(local.imagen)}*/}
                        {/*</span>*/}
                      </div>
                    ) : null}
                  </td>
                  {/*<td>{local.observacoes}</td>*/}
                  {/*<td>{local.localizacao}</td>*/}
                  <td>{local.valor}</td>
                  <td>{local.status}</td>
                  {/*<td>{local.cor}</td>*/}
                  {/*<td>{local.created ? <TextFormat type="date" value={local.created} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  {/*<td>{local.modified ? <TextFormat type="date" value={local.modified} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {/*<Button tag={Link} to={`/local/detail/${local.id}`} color="info" size="sm" data-cy="entityDetailsButton">*/}
                      {/*  <FontAwesomeIcon icon="eye" />{' '}*/}
                      {/*  <span className="d-none d-md-inline">/!*<Translate contentKey="entity.action.view">View</Translate>*!/</span>*/}
                      {/*</Button>*/}
                      <Button
                        tag={Link}
                        to={`/local/${local.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.edit">Edit</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/local/${local.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="aapmApp.local.home.notFound">No Locals found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={localList && localList.length > 0 ? '' : 'd-none'}>
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

export default Local;
