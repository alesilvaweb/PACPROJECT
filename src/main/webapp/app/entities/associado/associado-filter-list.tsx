import React, { useEffect, useState } from 'react';
import { JhiItemCount, JhiPagination, TextFormat, ValidatedField, ValidatedForm } from 'react-jhipster';
import associadoService from './associado-service';
import { Button, Col, Input, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useNavigate } from 'react-router-dom';
import Breadcrunbs from 'app/components/breadcrunbs';
import { toast } from 'react-toastify';
import { TextField } from '@mui/material';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

const AssociadoFilter = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [associados, setAssociados] = useState([]);
  const [totalItems, setTotalItems] = useState(0);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [currentPage, setCurrentPage] = useState(1);
  const [sort, setSort] = useState(''); // Adicione o estado para a ordenação
  const navigate = useNavigate();
  useEffect(() => {
    handleSearch(searchQuery, currentPage, sort);
  }, [currentPage, sort]);

  const handleSearch = async (value, page = 1, sortBy = '') => {
    setSearchQuery(value);
    try {
      const result = await associadoService.searchUsers(value, page - 1, itemsPerPage, sortBy);
      setAssociados(result.data);
      setTotalItems(parseInt(result.headers['x-total-count'], 10));
      setCurrentPage(page);
    } catch (error) {
      console.error('Error searching users:', error);
    }
  };

  const handlePagination = page => {
    handleSearch(searchQuery, page, sort);
  };

  const handleSort = field => {
    const newSort = sort === `${field},asc` ? `${field},desc` : `${field},asc`;
    setSort(newSort);
  };
  return (
    <div>
      <Breadcrunbs atual={'Associados'} />
      <h4>Associados</h4>
      <ValidatedForm onSubmit={null}>
        <div className="d-flex justify-content-between   align-items-center">
          <Col sm={4} className="d-flex align-content-center">
            <Input
              type="text"
              name="searchQuery"
              placeholder="Busca"
              onChange={e => handleSearch(e.target.value)}
              // style={{ padding: '10px', marginTop:"10px" }}
            />
          </Col>

          <Col sm={2} className="d-flex justify-content-end">
            <Button type={'button'} color={'primary'} onClick={() => navigate('/arquivo')}>
              Importar
            </Button>
          </Col>
        </div>
        <Table responsive hover>
          <thead>
            <tr>
              <th className="hand" onClick={() => handleSort('nome')}>
                Nome &nbsp;
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={() => handleSort('id')}>
                Matrícula &nbsp; <FontAwesomeIcon icon="sort" />
              </th>

              <th className="hand" onClick={() => handleSort('status')}>
                Status &nbsp;
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={() => handleSort('dataNascimento')}>
                Data Nascimento &nbsp;
                <FontAwesomeIcon icon="sort" />
              </th>
              <th> </th>
            </tr>
          </thead>
          <tbody>
            {associados.map(associado => (
              <tr key={associado.id} onClick={() => navigate(`/associado/${associado.id}`)} className="hand">
                <td> {associado.nome}</td>
                <td> {associado.id}</td>
                <td> {associado.status}</td>
                <td>
                  <TextFormat type="date" value={associado.dataNascimento} format={APP_LOCAL_DATE_FORMAT} />
                </td>

                <td className="text-end">
                  <div className="btn-group flex-btn-group-container">
                    <Button tag={Link} to={`/associado/${associado.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                      <FontAwesomeIcon icon="pencil-alt" />{' '}
                      <span className="d-none d-md-inline">{/*<Translate contentKey="entity.action.edit">Edit</Translate>*/}</span>
                    </Button>{' '}
                    &nbsp;
                    <Button tag={Link} to={`/associado/${associado.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
          <>
            <div className="justify-content-center d-flex">
              <JhiItemCount page={currentPage} total={totalItems} itemsPerPage={itemsPerPage} i18nEnabled />
            </div>
            <div className="justify-content-center d-flex">
              <JhiPagination
                totalItems={totalItems}
                activePage={currentPage}
                itemsPerPage={itemsPerPage}
                onSelect={page => handlePagination(page)}
                maxButtons={5}
              />
            </div>
          </>
        ) : null}
      </ValidatedForm>
    </div>
  );
};

export default AssociadoFilter;
