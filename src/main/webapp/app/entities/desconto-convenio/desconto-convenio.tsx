import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDescontoConvenio } from 'app/shared/model/desconto-convenio.model';
import { getEntities } from './desconto-convenio.reducer';

export const DescontoConvenio = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const descontoConvenioList = useAppSelector(state => state.descontoConvenio.entities);
  const loading = useAppSelector(state => state.descontoConvenio.loading);
  const totalItems = useAppSelector(state => state.descontoConvenio.totalItems);

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
      <h2 id="desconto-convenio-heading" data-cy="DescontoConvenioHeading">
        <Translate contentKey="aapmApp.descontoConvenio.home.title">Desconto Convenios</Translate>
        <div className="d-flex justify-content-end">
          <Link to="/convenio" className="btn btn-primary jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Convênios
          </Link>
          &nbsp;
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            {/*<Translate contentKey="aapmApp.descontoConvenio.home.refreshListLabel">Refresh List</Translate>*/}
          </Button>
          <Link to="/desconto-convenio/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            {/*<Translate contentKey="aapmApp.descontoConvenio.home.createLabel">Create new Desconto Convenio</Translate>*/}
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {descontoConvenioList && descontoConvenioList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="aapmApp.descontoConvenio.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('desconto')}>
                  <Translate contentKey="aapmApp.descontoConvenio.desconto">Desconto</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('descricao')}>
                  <Translate contentKey="aapmApp.descontoConvenio.descricao">Descricao</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="aapmApp.descontoConvenio.convenio">Convenio</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {descontoConvenioList.map((descontoConvenio, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/desconto-convenio/${descontoConvenio.id}`} color="link" size="sm">
                      {descontoConvenio.id}
                    </Button>
                  </td>
                  <td>{descontoConvenio.desconto}</td>
                  <td>{descontoConvenio.descricao}</td>
                  <td>
                    {descontoConvenio.convenio ? (
                      <Link to={`/convenio/${descontoConvenio.convenio.id}`}>{descontoConvenio.convenio.nome}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/desconto-convenio/${descontoConvenio.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.view">View</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/desconto-convenio/${descontoConvenio.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.edit">Edit</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/desconto-convenio/${descontoConvenio.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="aapmApp.descontoConvenio.home.notFound">No Desconto Convenios found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={descontoConvenioList && descontoConvenioList.length > 0 ? '' : 'd-none'}>
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

export default DescontoConvenio;
