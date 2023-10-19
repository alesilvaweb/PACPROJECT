import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRedesSociaisConvenio } from 'app/shared/model/redes-sociais-convenio.model';
import { getEntities } from './redes-sociais-convenio.reducer';

export const RedesSociaisConvenio = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const redesSociaisConvenioList = useAppSelector(state => state.redesSociaisConvenio.entities);
  const loading = useAppSelector(state => state.redesSociaisConvenio.loading);
  const totalItems = useAppSelector(state => state.redesSociaisConvenio.totalItems);

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
      <h2 id="redes-sociais-convenio-heading" data-cy="RedesSociaisConvenioHeading">
        <Translate contentKey="aapmApp.redesSociaisConvenio.home.title">Redes Sociais Convenios</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="aapmApp.redesSociaisConvenio.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/redes-sociais-convenio/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="aapmApp.redesSociaisConvenio.home.createLabel">Create new Redes Sociais Convenio</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {redesSociaisConvenioList && redesSociaisConvenioList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="aapmApp.redesSociaisConvenio.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="aapmApp.redesSociaisConvenio.nome">Nome</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('descricao')}>
                  <Translate contentKey="aapmApp.redesSociaisConvenio.descricao">Descricao</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('endereco')}>
                  <Translate contentKey="aapmApp.redesSociaisConvenio.endereco">Endereco</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('created')}>
                  <Translate contentKey="aapmApp.redesSociaisConvenio.created">Created</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('modified')}>
                  <Translate contentKey="aapmApp.redesSociaisConvenio.modified">Modified</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="aapmApp.redesSociaisConvenio.icon">Icon</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="aapmApp.redesSociaisConvenio.convenio">Convenio</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {redesSociaisConvenioList.map((redesSociaisConvenio, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/redes-sociais-convenio/${redesSociaisConvenio.id}`} color="link" size="sm">
                      {redesSociaisConvenio.id}
                    </Button>
                  </td>
                  <td>{redesSociaisConvenio.nome}</td>
                  <td>{redesSociaisConvenio.descricao}</td>
                  <td>{redesSociaisConvenio.endereco}</td>
                  <td>
                    {redesSociaisConvenio.created ? (
                      <TextFormat type="date" value={redesSociaisConvenio.created} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {redesSociaisConvenio.modified ? (
                      <TextFormat type="date" value={redesSociaisConvenio.modified} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {redesSociaisConvenio.icon ? (
                      <Link to={`/icons-redes-sociais/${redesSociaisConvenio.icon.id}`}>{redesSociaisConvenio.icon.nome}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {redesSociaisConvenio.convenio ? (
                      <Link to={`/convenio/${redesSociaisConvenio.convenio.id}`}>{redesSociaisConvenio.convenio.nome}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/redes-sociais-convenio/${redesSociaisConvenio.id}`}
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
                        to={`/redes-sociais-convenio/${redesSociaisConvenio.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`/redes-sociais-convenio/${redesSociaisConvenio.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="aapmApp.redesSociaisConvenio.home.notFound">No Redes Sociais Convenios found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={redesSociaisConvenioList && redesSociaisConvenioList.length > 0 ? '' : 'd-none'}>
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

export default RedesSociaisConvenio;
