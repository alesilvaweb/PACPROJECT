import React, { useEffect, useState } from 'react';

import { Form, Input, Table } from 'reactstrap';
import Button from '@mui/material/Button';
import readXlsxFile from 'read-excel-file';
import { isUndefined } from 'lodash';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntities, reset } from 'app/entities/associado/associado.reducer';
import { createDependente, getDependentes } from 'app/entities/dependente/dependente.reducer';
import { Status } from 'app/shared/model/enumerations/status.model';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { isEmpty } from 'react-jhipster';
import axios from 'axios';
import { IAssociado } from 'app/shared/model/associado.model';

export const Xlsx = () => {
  const [state, setState] = useState({
    headers: null,
    body: null,
  });
  const dispatch = useAppDispatch();
  const [listAssociados, setAssociado] = useState([]);
  const dependenteList = useAppSelector(state => state.dependente.entities);

  const atualizarAssociados = async () => {
    const apiUrl = `api/associados`;
    try {
      const requestUrl = `${apiUrl}`;
      const [response] = await Promise.all([
        axios.get<IAssociado[]>(requestUrl).then(e => {
          console.log(e.data);
          setAssociado(e.data);
        }),
      ]);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    atualizarAssociados();
  }, []);

  const handleSubmit = async e => {
    e.preventDefault();

    const file = e.target.file.files[0];

    if (!file) return;

    const rows = await readXlsxFile(file);

    // console.log('Objeto completo: ', rows);

    const headers = rows[0];

    let array = [];

    // console.log('Objetos formatados: ');

    for (let i = 1; i < rows.length; i++) {
      const row = rows[i];
      let obj = {};

      for (let j = 0; j < headers.length; j++) {
        if (headers[j] === 'Nascimento' || headers[j] === 'NascimentoDep') {
          obj = {
            ...obj,
            // @ts-ignore
            [headers[j]]: format(new Date(row[j]), 'yyyy-MM-dd', { locale: ptBR }),
          };
        } else {
          obj = {
            ...obj,
            // @ts-ignore
            [headers[j]]: row[j],
          };
        }
      }

      // console.log(obj);
      array.push(obj);
    }

    setState({
      ...state,
      headers,
      body: array,
    });
  };
  const user: any = [];
  const associados: any = [];
  const dependente: any = [];
  let qtAssociados = associados.length;

  const saveAssociados = () => {
    if (!isEmpty(associados)) {
      atualizarAssociados();
      dispatch(createEntity(associados));
    } else {
      alert('Nenhum Registro para atualizar');
    }
    atualizarAssociados();
  };

  const saveDependente = () => {
    atualizarAssociados();
    dependente.map((obj, i) => {
      const assoc = listAssociados.find(associadoList => associadoList.id === parseInt(obj.id));

      if (obj.status != 'Inativo') {
        dispatch(
          createDependente({
            nome: obj.dependente,
            dataNascimento: obj.dtNasDep,
            parentesco: obj.parentesco,
            status: obj.status,
            associado: assoc,
          })
        );
      }
    });
  };

  const renderHeader = () => {
    // @ts-ignore
    const headers = state.headers || [];
    return (
      <tr>
        {headers.map((header, i) => (
          <th key={i}>{header}</th>
        ))}
      </tr>
    );
  };

  const renderBody = () => {
    const data = state.body || [];
    let lastId = 'o';

    return data.map((obj, i) => {
      if (
        !associados.find(associados => associados.id === parseInt(obj['Matricula'])) &&
        !listAssociados.find(associadoList => parseInt(associadoList.matricula) === parseInt(obj['Matricula']))
      ) {
        user.push({
          id: obj['Matricula'],
          login: obj['Email'],
          firstName: obj['Nome'].split(' ')[0],
          // @ts-ignore
          lastName:
            obj['Nome'].split(' ')[1] +
            ' ' +
            (isUndefined(obj['Nome'].split(' ')[2]) ? '' : obj['Nome'].split(' ')[2]) +
            ' ' +
            (isUndefined(obj['Nome'].split(' ')[3]) ? '' : obj['Nome'].split(' ')[3]),
          email: obj['Email'],
          // @ts-ignore
          activated: true,
          // @ts-ignore
          langKey: 'pt-br',
          dtNasAssoc: obj['Nascimento'],
          authorities: [
            // @ts-ignore
            'ROLE_USER',
          ],
        });

        associados.push({
          id: parseInt(obj['Matricula']),
          nome: obj['Nome'],
          matricula: obj['Matricula'],
          status: 'Ativo',
          email: obj['Email'],
          dataNascimento: obj['Nascimento'],
        });

        // if (!isEmpty(associados)&&associados.length === 100){
        //   saveAssociados()
        // }
      }

      if (
        obj['Dependente'] ||
        (lastId === obj['Matricula'] && !dependenteList.find(dependenteList => dependenteList.nome === obj['Dependente']))
      ) {
        dependente.push({
          id: obj['Matricula'],
          dependente: obj['Dependente'],
          parentesco: obj['Parentesco'],
          dtNasDep: obj['NascimentoDep'],
          status: obj['Status'].trim(),
        });
      }

      lastId = obj['Matricula'];

      return (
        <tr key={i}>
          {Object.keys(obj).map((column, j) => {
            return <td key={j}>{obj[column]}</td>;
          })}
        </tr>
      );
    });
  };

  console.log({ listAssociados });
  console.log({ associados });
  console.log({ dependente });
  return (
    <div>
      <h4>Upload de arquivos excel</h4>
      <br />
      {state.body ? (
        <div>
          <Button type={'button'} onClick={() => saveAssociados()}>
            Salvar Associados ({qtAssociados})
          </Button>
          <Button type={'button'} onClick={() => saveDependente()}>
            Salvar dependentes
          </Button>
        </div>
      ) : null}
      <Form onSubmit={handleSubmit}>
        <Input type="file" name="file" id="file" />
        <Button type="submit" color={'primary'}>
          enviar
        </Button>
      </Form>
      {
        // @ts-ignore
        state.body ? (
          <React.Fragment>
            <Table>
              <thead>{renderHeader()}</thead>
              <tbody>{renderBody()}</tbody>
            </Table>
          </React.Fragment>
        ) : null
      }
    </div>
  );
};

export default Xlsx;
