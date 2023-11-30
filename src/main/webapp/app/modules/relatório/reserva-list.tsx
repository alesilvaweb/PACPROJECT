import React, { useEffect, useMemo, useState } from 'react';
import { MaterialReactTable, type MRT_ColumnDef, MRT_Row, useMaterialReactTable } from 'material-react-table';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { Button } from '@mui/material';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import Box from '@mui/material/Box';
import axios from 'axios';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { useNavigate } from 'react-router-dom';

const ReservaList = () => {
  const [reservas, setReservas] = useState([]);
  const [data, setData] = useState([]);

  async function fetchReservas() {
    try {
      const response = await axios.get('api/reservas?status.equals=Agendado&data.greaterThanOrEqual=2023-11-28&page=0&size=9999');
      setReservas(response.data);
    } catch (error) {
      console.error('Erro ao buscar Reservas:', error);
    }
  }

  useEffect(() => {
    fetchReservas();
  }, []);

  useEffect(() => {
    dataReserva();
  }, [reservas]);

  type Reserva = {
    matricula: string;
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
        accessorKey: 'matricula',
        header: 'Matricula',
        filterVariant: 'text',
      },
      {
        accessorKey: 'associado',
        header: 'Associado',
        filterVariant: 'text',
      },
      {
        accessorKey: 'telefone',
        header: 'Telefone',
        filterVariant: 'text',
      },
      {
        accessorKey: 'motivoReserva',
        header: 'Motivo Reserva',
        filterVariant: 'text', // default
        size: 200,
      },
    ],
    []
  );

  const handleExportRows = (rows: MRT_Row<Reserva>[]) => {
    const doc = new jsPDF('l', 'pt', 'letter');

    const tableData = rows.map(row => Object.values(row.original));
    const tableHeaders = columns.map(c => c.header);

    autoTable(doc, {
      head: [tableHeaders],
      body: tableData,
      headStyles: { lineWidth: 1, fillColor: [202, 37, 30], textColor: [255, 255, 255] },
    });

    var y = 10;
    doc.setLineWidth(2);
    doc.text('Relatório de Reservas AAPM', (y = y + 30), 25);
    doc.setLanguage('pt-BR');
    doc.save('Reservas.pdf');
  };

  const dataReserva = () => {
    const re = [];
    reservas.map(res => {
      re.push({
        local: res.local?.nome,
        data: res.data,
        numPessoas: res.numPessoas,
        matricula: res.associado?.id,
        associado: res.associado?.nome,
        // status: res.status,
        telefone: res.associado?.telefone,
        motivoReserva: res.motivoReserva,
        // departamento: res.departamento?.nome,
      });
    });
    setData(re);
  };

  // @ts-ignore
  const navigate = useNavigate();

  const table = useMaterialReactTable({
    columns,
    data,
    initialState: {
      showColumnFilters: false,
      density: 'compact',
    },
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
          Baixar PDF
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
