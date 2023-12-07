import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, Table } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './icons-redes-sociais.reducer';
import Breadcrunbs from 'app/components/breadcrunbs';

export const IconsRedesSociais = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const [filter, setFilter] = useState('');

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const iconsRedesSociaisList = useAppSelector(state => state.iconsRedesSociais.entities);
  const loading = useAppSelector(state => state.iconsRedesSociais.loading);
  const totalItems = useAppSelector(state => state.iconsRedesSociais.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        filter: `nome.contains=${filter}`,
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
  }, [paginationState.activePage, paginationState.order, paginationState.sort, filter]);

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
      <Breadcrunbs atual={'Redes Sociais'} />
      <h3 id="icons-redes-sociais-heading" data-cy="IconsRedesSociaisHeading">
        <Translate contentKey="aapmApp.iconsRedesSociais.home.title">Icons Redes Sociais</Translate>
      </h3>
      <div className="d-flex justify-content-between">
        <div className="col-md-4 col-sm-8 col-xl-4">
          <Input
            type={'text'}
            name={'busca'}
            onChange={e => {
              setFilter(e.target.value);
            }}
            placeholder={'Buscar'}
          />
        </div>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            {/*<Translate contentKey="aapmApp.iconsRedesSociais.home.refreshListLabel">Refresh List</Translate>*/}
          </Button>
          <Link
            to="/icons-redes-sociais/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            {/*<Translate contentKey="aapmApp.iconsRedesSociais.home.createLabel">Create new Icons Redes Sociais</Translate>*/}
          </Link>
        </div>
      </div>
      <div className="table-responsive">
        {iconsRedesSociaisList && iconsRedesSociaisList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/*<th className="hand" onClick={sort('id')}>*/}
                {/*  <Translate contentKey="aapmApp.iconsRedesSociais.id">ID</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="aapmApp.iconsRedesSociais.nome">Nome</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*<th className="hand" onClick={sort('descricao')}>*/}
                {/*  <Translate contentKey="aapmApp.iconsRedesSociais.descricao">Descricao</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('icon')}>*/}
                {/*  <Translate contentKey="aapmApp.iconsRedesSociais.icon">Icon</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                {/*<th className="hand" onClick={sort('image')}>*/}
                {/*  <Translate contentKey="aapmApp.iconsRedesSociais.imagem">Image</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>*/}
                <th />
              </tr>
            </thead>
            <tbody>
              {iconsRedesSociaisList.map((iconsRedesSociais, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/*<td>*/}
                  {/*  <Button tag={Link} to={`/icons-redes-sociais/${iconsRedesSociais.id}`} color="link" size="sm">*/}
                  {/*    {iconsRedesSociais.id}*/}
                  {/*  </Button>*/}
                  {/*</td>*/}
                  <td>{iconsRedesSociais.nome}</td>
                  {/*<td>{iconsRedesSociais.descricao}</td>*/}
                  {/*<td>{iconsRedesSociais.icon}</td>*/}
                  {/*<td>*/}
                  {/*  {iconsRedesSociais.image ? (*/}
                  {/*    <div>*/}
                  {/*      {iconsRedesSociais.imageContentType ? (*/}
                  {/*        <a onClick={openFile(iconsRedesSociais.imageContentType, iconsRedesSociais.image)}>*/}
                  {/*          <img*/}
                  {/*            src={`data:${iconsRedesSociais.imageContentType};base64,${iconsRedesSociais.image}`}*/}
                  {/*            style={{ maxHeight: '30px' }}*/}
                  {/*          />*/}
                  {/*          &nbsp;*/}
                  {/*        </a>*/}
                  {/*      ) : null}*/}
                  {/*      <span>*/}
                  {/*        {iconsRedesSociais.imageContentType}, {byteSize(iconsRedesSociais.image)}*/}
                  {/*      </span>*/}
                  {/*    </div>*/}
                  {/*  ) : null}*/}
                  {/*</td>*/}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/icons-redes-sociais/${iconsRedesSociais.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.view">View</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/icons-redes-sociais/${iconsRedesSociais.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.edit">Edit</Translate>*/}</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/icons-redes-sociais/${iconsRedesSociais.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="aapmApp.iconsRedesSociais.home.notFound">No Icons Redes Sociais found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={iconsRedesSociaisList && iconsRedesSociaisList.length > 0 ? '' : 'd-none'}>
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

export default IconsRedesSociais;
