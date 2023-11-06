import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssociado } from 'app/shared/model/associado.model';
import { getEntities } from './associado.reducer';
import { BottomNavigationAction } from '@mui/material';
import Breadcrunbs from 'app/components/breadcrunbs';

export const Associado = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const associadoList = useAppSelector(state => state.associado.entities);
  const loading = useAppSelector(state => state.associado.loading);
  const totalItems = useAppSelector(state => state.associado.totalItems);

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
      <Breadcrunbs />
      <h2 id="associado-heading" data-cy="AssociadoHeading">
        <Translate contentKey="aapmApp.associado.home.title">Associados</Translate>
        <div className="d-flex justify-content-end">
          <Link to="/arquivo" className="btn btn-primary jh-create-entity">
            Importar
          </Link>
          &nbsp;
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            {/*<Translate contentKey="aapmApp.associado.home.refreshListLabel">Refresh List</Translate>*/}
          </Button>
          {/*<Link to="/associado/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">*/}
          {/*  <FontAwesomeIcon icon="plus" />*/}
          {/*  &nbsp;*/}
          {/*  <Translate contentKey="aapmApp.associado.home.createLabel">Create new Associado</Translate>*/}
          {/*</Link>*/}
        </div>
      </h2>
      <div className="table-responsive">
        {associadoList && associadoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/*<th className="hand" onClick={sort('id')}>*/}
                {/*  <Translate contentKey="aapmApp.associado.id">ID</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="aapmApp.associado.nome">Nome</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('matricula')}>
                  <Translate contentKey="aapmApp.associado.matricula">Matricula</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="aapmApp.associado.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('telefone')}>
                  <Translate contentKey="aapmApp.associado.telefone">Telefone</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="aapmApp.associado.email">Email</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dataNascimento')}>
                  <Translate contentKey="aapmApp.associado.dataNascimento">Data Nascimento</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('created')}>*/}
                {/*  <Translate contentKey="aapmApp.associado.created">Created</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('modified')}>*/}
                {/*  <Translate contentKey="aapmApp.associado.modified">Modified</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th />
              </tr>
            </thead>
            <tbody>
              {associadoList.map((associado, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/*<td>*/}
                  {/*  <Button tag={Link} to={`/associado/${associado.id}`} color="link" size="sm">*/}
                  {/*    {associado.id}*/}
                  {/*  </Button>*/}
                  {/*</td>*/}
                  <td>{associado.nome}</td>
                  <td>{associado.matricula}</td>
                  <td>
                    <Translate contentKey={`aapmApp.Status.${associado.status}`} />
                  </td>
                  <td>{associado.telefone}</td>
                  <td>{associado.email}</td>
                  <td>
                    {associado.dataNascimento ? (
                      <TextFormat type="date" value={associado.dataNascimento} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  {/*<td>{associado.created ? <TextFormat type="date" value={associado.created} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  {/*<td>{associado.modified ? <TextFormat type="date" value={associado.modified} format={APP_DATE_FORMAT} /> : null}</td>*/}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/associado/${associado.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.view">View</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/associado/${associado.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.edit">Edit</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/associado/${associado.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="aapmApp.associado.home.notFound">No Associados found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={associadoList && associadoList.length > 0 ? '' : 'd-none'}>
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

export default Associado;
