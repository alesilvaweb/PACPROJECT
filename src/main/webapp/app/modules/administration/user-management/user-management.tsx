import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Badge, Button, Input, Table } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getUsersAsAdmin, updateUser } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import Breadcrunbs from 'app/components/breadcrunbs';

export const UserManagement = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const [filterName, setFilterName] = useState('');
  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const getUsersFromProps = () => {
    dispatch(
      getUsersAsAdmin({
        page: pagination.activePage - 1,
        filter: filterName,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
      })
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}&query=${filterName}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort, filterName]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sortParam = params.get(SORT);
    if (page && sortParam) {
      const sortSplit = sortParam.split(',');
      setPagination({
        ...pagination,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () =>
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    getUsersFromProps();
  };

  const toggleActive = user => () => {
    dispatch(
      updateUser({
        ...user,
        activated: !user.activated,
      })
    ).then(() => {
      getUsersFromProps();
    });
  };

  const account = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const totalItems = useAppSelector(state => state.userManagement.totalItems);
  const loading = useAppSelector(state => state.userManagement.loading);

  return (
    <div>
      <Breadcrunbs atual={'Usuário'} />
      <h2 id="user-management-page-heading" data-cy="userManagementPageHeading">
        <Translate contentKey="userManagement.home.title">Users</Translate>
        <div className="d-flex justify-content-between">
          <div className="col-md-4 col-sm-8 col-xl-4">
            <Input
              type={'text'}
              name={'busca'}
              onChange={e => {
                setFilterName(e.target.value);
              }}
              placeholder={'Buscar'}
            />
          </div>

          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} />{' '}
              {/*<Translate contentKey="userManagement.home.refreshListLabel">Refresh List</Translate>*/}
            </Button>
            <Link to="new" className="btn btn-primary jh-create-entity">
              <FontAwesomeIcon icon="plus" />
              {/*<Translate contentKey="userManagement.home.createLabel">Create a new user</Translate>*/}
            </Link>
          </div>
        </div>
      </h2>
      <Table responsive striped>
        <thead>
          <tr>
            <th className="hand" onClick={sort('id')}>
              <Translate contentKey="global.field.id">ID</Translate>
              &nbsp;
              <FontAwesomeIcon icon="sort" />
            </th>
            <th className="hand" onClick={sort('login')}>
              <Translate contentKey="userManagement.login">Login</Translate>
              &nbsp;
              <FontAwesomeIcon icon="sort" />
            </th>
            <th className="hand" onClick={sort('activated')}>
              Status &nbsp;
              <FontAwesomeIcon icon="sort" />
            </th>
            {/*<th className="hand" onClick={sort('email')}>*/}
            {/*  <Translate contentKey="userManagement.email">Email</Translate>*/}
            {/*  <FontAwesomeIcon icon="sort" />*/}
            {/*</th>*/}

            {/*<th className="hand" onClick={sort('langKey')}>*/}
            {/*  <Translate contentKey="userManagement.langKey">Lang Key</Translate>*/}
            {/*  <FontAwesomeIcon icon="sort" />*/}
            {/*</th>*/}
            <th>
              <Translate contentKey="userManagement.profiles">Profiles</Translate>
            </th>
            <th></th>
            {/*<th className="hand" onClick={sort('createdDate')}>*/}
            {/*  <Translate contentKey="userManagement.createdDate">Created Date</Translate>*/}
            {/*  <FontAwesomeIcon icon="sort" />*/}
            {/*</th>*/}
            {/*<th className="hand" onClick={sort('lastModifiedBy')}>*/}
            {/*  <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>*/}
            {/*  <FontAwesomeIcon icon="sort" />*/}
            {/*</th>*/}
            {/*<th id="modified-date-sort" className="hand" onClick={sort('lastModifiedDate')}>*/}
            {/*  <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>*/}
            {/*  <FontAwesomeIcon icon="sort" />*/}
            {/*</th>*/}
            {/*<th />*/}
          </tr>
        </thead>
        <tbody>
          {users?.map((user, i) => (
            <tr id={user.login} key={`user-${i}`}>
              <td>
                <Button tag={Link} to={user.login} color="link" size="sm">
                  {user.id}
                </Button>
              </td>
              <td>{user.login}</td>
              {/*<td>{user.email}</td>*/}
              <td>
                {user.activated ? (
                  <Button color="success" onClick={toggleActive(user)} sx={{ padding: '0' }}>
                    <Translate contentKey="userManagement.activated">Activated</Translate>
                  </Button>
                ) : (
                  <Button color="danger" onClick={toggleActive(user)}>
                    Inativo
                  </Button>
                )}
              </td>
              {/*<td>{user.langKey}</td>*/}
              <td>
                {user.authorities
                  ? user.authorities.map((authority, j) => (
                      <div key={`user-auth-${i}-${j}`}>
                        <Badge color="info">{authority}</Badge>
                      </div>
                    ))
                  : null}
              </td>
              {/*<td>*/}
              {/*  {user.createdDate ? <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}*/}
              {/*</td>*/}
              {/*<td>{user.lastModifiedBy}</td>*/}
              {/*<td>*/}
              {/*  {user.lastModifiedDate ? (*/}
              {/*    <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />*/}
              {/*  ) : null}*/}
              {/*</td>*/}
              <td className="text-end">
                <div className="btn-group flex-btn-group-container">
                  <Button tag={Link} to={user.login} color="info" size="sm">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.view">View</Translate>*/}</span>
                  </Button>
                  <Button tag={Link} to={`${user.login}/edit`} color="primary" size="sm">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.edit">Edit</Translate>*/}</span>
                  </Button>
                  <Button tag={Link} to={`${user.login}/delete`} color="danger" size="sm" disabled={account.login === user.login}>
                    <FontAwesomeIcon icon="trash" />{' '}
                    <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.delete">Delete</Translate>*/}</span>
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {totalItems ? (
        <div className={users?.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={pagination.activePage} total={totalItems} itemsPerPage={pagination.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={pagination.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={pagination.itemsPerPage}
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

export default UserManagement;
