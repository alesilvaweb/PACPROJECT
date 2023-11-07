import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './departamento.reducer';
import { Breadcrumbs } from '@mui/material';
import Breadcrunbs from 'app/components/breadcrunbs';

export const Departamento = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const departamentoList = useAppSelector(state => state.departamento.entities);
  const loading = useAppSelector(state => state.departamento.loading);
  const totalItems = useAppSelector(state => state.departamento.totalItems);

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
      <Breadcrunbs atual={'Departamentos'} />
      <h2 id="departamento-heading" data-cy="DepartamentoHeading">
        <Translate contentKey="aapmApp.departamento.home.title">Departamentos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            {/*<Translate contentKey="aapmApp.departamento.home.refreshListLabel">Refresh List</Translate>*/}
          </Button>
          <Link to="/departamento/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            {/*<Translate contentKey="aapmApp.departamento.home.createLabel">Create new Departamento</Translate>*/}
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {departamentoList && departamentoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/*<th className="hand" onClick={sort('id')}>*/}
                {/*  <Translate contentKey="aapmApp.departamento.id">ID</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="aapmApp.departamento.nome">Nome</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('descricao')}>
                  <Translate contentKey="aapmApp.departamento.descricao">Descricao</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="aapmApp.departamento.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('created')}>*/}
                {/*  <Translate contentKey="aapmApp.departamento.created">Created</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('modified')}>*/}
                {/*  <Translate contentKey="aapmApp.departamento.modified">Modified</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th />
              </tr>
            </thead>
            <tbody>
              {departamentoList.map((departamento, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/*<td>*/}
                  {/*  <Button tag={Link} to={`/departamento/${departamento.id}`} color="link" size="sm">*/}
                  {/*    {departamento.id}*/}
                  {/*  </Button>*/}
                  {/*</td>*/}
                  <td>{departamento.nome}</td>
                  <td>{departamento.descricao}</td>
                  <td>
                    <Translate contentKey={`aapmApp.Status.${departamento.status}`} />
                  </td>
                  {/*<td>{departamento.created ? <TextFormat type="date" value={departamento.created} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  {/*<td>*/}
                  {/*  {departamento.modified ? <TextFormat type="date" value={departamento.modified} format={APP_DATE_FORMAT} /> : null}*/}
                  {/*</td>*/}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/departamento/${departamento.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.view">View</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/departamento/${departamento.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.edit">Edit</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/departamento/${departamento.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="aapmApp.departamento.home.notFound">No Departamentos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={departamentoList && departamentoList.length > 0 ? '' : 'd-none'}>
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

export default Departamento;
