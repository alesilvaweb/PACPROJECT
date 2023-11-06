import React, { useEffect, useMemo, useState } from 'react';

import { MaterialReactTable, type MRT_ColumnDef, MRT_Row, useMaterialReactTable } from 'material-react-table';
// import {citiesList, data, type Person, usStateList} from './makeData';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { Button } from '@mui/material';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import Box from '@mui/material/Box';
import axios from 'axios';
import { IReserva } from 'app/shared/model/reserva.model';
import { StatusReserva } from 'app/shared/model/enumerations/status-reserva.model';
import { ILocal } from 'app/shared/model/local.model';
import { IAssociado } from 'app/shared/model/associado.model';
import { IDepartamento } from 'app/shared/model/departamento.model';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { useNavigate } from 'react-router-dom';

const ReservaList = () => {
  const [reservas, setReservas] = useState([]);
  const [data, setData] = useState([]);

  async function fetchReservas() {
    try {
      const response = await axios.get('api/reservas');
      setReservas(response.data);
    } catch (error) {
      console.error('Erro ao buscar Reservas:', error);
    }
  }

  useEffect(() => {
    fetchReservas();
  }, []);

  type Reserva = {
    status: boolean;
    motivoReserva: string;
    data: string;
    age: number;
    numPessoas: number;
    local: string;
    associado: string;
    departamento: string;
  };

  const columns = useMemo<MRT_ColumnDef<Reserva>[]>(
    () => [
      // {
      //   header: 'Status',
      //   accessorFn: (originalRow) => (originalRow.status ? 'Ativo' : 'Inativo'), //must be strings
      //   id: 'status',
      //   filterVariant: 'checkbox',
      //   Cell: ({cell}) =>
      //     cell.getValue() === 'Ativo' ? 'Ativo' : 'Inativo',
      //   size: 170,
      // },
      {
        accessorKey: 'local',
        header: 'Local',
        filterVariant: 'text',
      },
      {
        accessorFn: originalRow => new Date(originalRow.data), //convert to date for sorting and filtering
        id: 'data',
        header: 'Data',
        filterVariant: 'date-range',
        size: 100,
        Cell: ({ cell }) => cell.getValue<Date>().toLocaleDateString(), // convert back to string for display
      },
      {
        accessorKey: 'numPessoas',
        header: 'N° Pessoas',
        filterVariant: 'text',
        size: 100,
      },

      {
        accessorKey: 'associado',
        header: 'Associado',
        filterVariant: 'text',
      },
      {
        accessorKey: 'motivoReserva',
        header: 'Motivo Reserva',
        filterVariant: 'text', // default
        size: 200,
      },
      // {
      //   accessorKey: 'numPessoas',
      //   header: 'Número Pessoas',
      //   Cell: ({cell}) =>
      //     cell.getValue<number>().toLocaleString('pt-br', {
      //
      //
      //     }),
      //   filterVariant: 'range-slider',
      //   filterFn: 'betweenInclusive', // default (or between)
      //   muiFilterSliderProps: {
      //     marks: true,
      //     max: 300 , //custom max (as opposed to faceted max)
      //     min: 0, //custom min (as opposed to faceted min)
      //     step: 1 ,
      //
      //   },
      // },

      {
        accessorKey: 'departamento',
        header: 'Departamento',
        filterVariant: 'text',
      },
    ],
    []
  );
  const handleExportRows = (rows: MRT_Row<Reserva>[]) => {
    const doc = new jsPDF();
    const tableData = rows.map(row => Object.values(row.original));
    const tableHeaders = columns.map(c => c.header);

    autoTable(doc, {
      head: [tableHeaders],
      body: tableData,
    });

    doc.save('Reservas.pdf');
  };

  const dataReserva = () => {
    const re = [];
    reservas.map(res => {
      re.push({
        motivoReserva: res.motivoReserva,
        numPessoas: res.numPessoas,
        status: res.status,
        data: res.data,
        local: res.local?.nome,
        associado: res.associado?.nome,
        departamento: res.departamento?.nome,
      });
    });
    setData(re);
  };

  useEffect(() => {
    dataReserva();
  }, [reservas]);
  // @ts-ignore
  const navigate = useNavigate();
  const table = useMaterialReactTable({
    columns,
    data,
    initialState: { showColumnFilters: false },
    renderTopToolbarCustomActions: ({ table }) => (
      <Box
        sx={{
          display: 'flex',
          gap: '16px',
          padding: '8px',
          flexWrap: 'wrap',
        }}
      >
        <Button
          disabled={table.getPrePaginationRowModel().rows.length === 0}
          //export all rows, including from the next page, (still respects filtering and sorting)
          onClick={() => handleExportRows(table.getPrePaginationRowModel().rows)}
          startIcon={<FileDownloadIcon />}
        >
          Salvar PDF
        </Button>
        {/*<Button*/}
        {/*  disabled={table.getRowModel().rows.length === 0}*/}
        {/*  //export all rows as seen on the screen (respects pagination, sorting, filtering, etc.)*/}
        {/*  onClick={() => handleExportRows(table.getRowModel().rows)}*/}
        {/*  startIcon={<FileDownloadIcon/>}*/}
        {/*>*/}
        {/*  Export Page Rows*/}
        {/*</Button>*/}
        {/*<Button*/}
        {/*  disabled={*/}
        {/*    !table.getIsSomeRowsSelected() && !table.getIsAllRowsSelected()*/}
        {/*  }*/}
        {/*  //only export selected rows*/}
        {/*  onClick={() => handleExportRows(table.getSelectedRowModel().rows)}*/}
        {/*  startIcon={<FileDownloadIcon/>}*/}
        {/*>*/}
        {/*  Export Selected Rows*/}
        {/*</Button>*/}
      </Box>
    ),
  });
  return (
    <div>
      <Breadcrumb>
        <BreadcrumbItem onClick={() => navigate('/')}>
          <a>Início</a>
        </BreadcrumbItem>
        <BreadcrumbItem active>Relatório de Reservas</BreadcrumbItem>
      </Breadcrumb>
      <MaterialReactTable table={table} />
    </div>
  );
};

const ReservaListWithLocalizationProvider = () => (
  <LocalizationProvider dateAdapter={AdapterDayjs}>
    <ReservaList />
  </LocalizationProvider>
);

export default ReservaListWithLocalizationProvider;
