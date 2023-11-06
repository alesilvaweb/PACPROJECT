import React, { Component, useState } from 'react';

import { Form, Input, Table } from 'reactstrap';
import Button from '@mui/material/Button';
import { formatDate } from '@fullcalendar/core';
import readXlsxFile from 'read-excel-file';
import { Mail } from '@mui/icons-material';
import { o } from '@fullcalendar/core/internal-common';
import { lowerCase } from 'lodash';

export const Xlsx = () => {
  const [state, setState] = useState({
    headers: null,
    body: null,
  });

  const [associado, setAssociado] = useState<object>([
    {
      Matricula: String,
      Nome: String,
      Dependente: String,
      Parentesco: String,
      Nascimento: String,
      NascimentoDep: String,
      Status: String,
      Email: String,
    },
  ]);
  const handleSubmit = async e => {
    e.preventDefault();

    const file = e.target.file.files[0];

    if (!file) return;

    const rows = await readXlsxFile(file);

    console.log('Objeto completo: ', rows);

    const headers = rows[0];

    let array = [];

    console.log('Objetos formatados: ');

    for (let i = 1; i < rows.length; i++) {
      const row = rows[i];
      let obj = {};

      for (let j = 0; j < headers.length; j++) {
        if (headers[j] === 'Nascimento' || headers[j] === 'NascimentoDep') {
          obj = {
            ...obj,
            // @ts-ignore
            [headers[j]]: formatDate(row[j], { timeZone: 'UTC', locale: 'br' }),
          };
        } else {
          obj = {
            ...obj,
            // @ts-ignore
            [headers[j]]: row[j],
          };
        }
      }

      console.log(obj);
      array.push(obj);
    }

    setState({
      ...state,
      headers,
      body: array,
    });
  };

  const associados = [
    {
      id: String,
      login: String,
      firstName: String,
      lastName: String,
      email: String,
      activated: Boolean,
      langKey: String,
      dependente: String,
      parentesco: String,
      dtNasAssoc: String,
      dtNasDep: String,
      status: String,
      authorities: [String],
    },
  ];
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

    return data.map((obj, i) => {
      associados.push({
        id: obj['Matricula'],
        login: obj['Email'],
        firstName: obj['Nome'].split(' ')[0],
        // @ts-ignore
        lastName: obj['Nome'].split(' ')[1] + ' ' + obj['Nome'].split(' ')[2] + ' ' + obj['Nome'].split(' ')[3],
        email: obj['Email'],
        // @ts-ignore
        activated: true,
        // @ts-ignore
        langKey: 'pt-br',
        dependente: obj['Dependente'],
        parentesco: obj['Parentesco'],
        dtNasAssoc: obj['Nascimento'],
        dtNasDep: obj['NascimentoDep'],
        status: obj['Status'],
        authorities: [
          // @ts-ignore
          'ROLE_USER',
        ],
      });

      return (
        <tr key={i}>
          {Object.keys(obj).map((column, j) => {
            return <td key={j}>{obj[column]}</td>;
          })}
        </tr>
      );
    });
  };

  console.log({ associados });
  return (
    <div>
      <h4>Upload de arquivos excel</h4>
      <br />

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
