import React, { useState, useEffect } from 'react';
import { ValidatedForm, ValidatedField, JhiPagination } from 'react-jhipster';
import userService from './user-service';
import { Table } from 'reactstrap';

const UserList = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [users, setUsers] = useState([]);
  const [totalItems, setTotalItems] = useState(0);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    handleSearch(searchQuery, currentPage);
  }, []);

  const handleSearch = async (value, page = 1) => {
    setSearchQuery(value);
    try {
      const result = await userService.searchUsers(value, 1, itemsPerPage);
      console.log(result.data);
      setUsers(result.data);
      setTotalItems(result.data.totalElements);
      setCurrentPage(page);
    } catch (error) {
      console.error('Error searching users:', error);
    }
  };

  const handlePagination = page => {
    handleSearch(searchQuery, page);
  };

  return (
    <ValidatedForm onSubmit={null}>
      <ValidatedField
        type="text"
        name="searchQuery"
        placeholder="Digite para buscar usuários"
        onChange={e => handleSearch(e.target.value)}
      />
      <Table>
        <thead>
          <th>id</th>
          <th>Nome</th>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.firstName}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      {/*<JhiPagination*/}
      {/*  totalItems={totalItems}*/}
      {/*  activePage={currentPage}*/}
      {/*  onSelect={page => handlePagination(page)}*/}
      {/*  maxButtons={5} // Ajuste conforme necessário*/}
      {/*  itemsPerPage={itemsPerPage}*/}
      {/*/>*/}
    </ValidatedForm>
  );
};

export default UserList;
