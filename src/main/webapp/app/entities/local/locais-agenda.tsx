import './locais.scss';

import React, { useEffect, useRef, useState } from 'react';
import { getEntity } from './local.reducer';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { dataAtual, difDate } from 'app/shared/util/date-utils';
import FullCalendar from '@fullcalendar/react';
import { IReserva } from 'app/shared/model/reserva.model';
import { Chip } from '@mui/material';
import { ArrowBackIos, ArrowForwardIos } from '@mui/icons-material';
import axios from 'axios';
import Swal from 'sweetalert2';

const LocaisAgenda = args => {
  const dispatch = useAppDispatch();
  const [reservasList, setReservasList] = useState([]);
  const locaisEntity = useAppSelector(state => state.local.entity);
  const account = useAppSelector(state => state.authentication.account);
  const loadingLocal = useAppSelector(state => state.local.loading);

  const [currentEvents, setCurrentEvents] = useState([]);
  const [weekendsVisible, setWeekendsVisible] = useState(true);
  const navigate = useNavigate();
  const [modal, setModal] = useState(false);
  const [statusText, setStatusText] = useState('');
  const [parametro, setParametro] = useState([]);
  const [numReservas, setNumReservas] = useState(0);
  const [count, setCount] = useState(0);
  const [dias, setDias] = useState(0);

  const calendarRef = useRef<FullCalendar>(null!);

  const { id } = useParams<'id'>();
  const data = new Date();
  const DataValida = addDays(data, dias);
  const location = useLocation();
  const pathnames = location.pathname.split('/').filter(x => x);

  function addDays(date, days) {
    date.setDate(date.getDate() + days);
    return date;
  }

  async function countReservas() {
    try {
      const response = await axios.get(`api/reservas/count`);
      setNumReservas(response.data);
    } catch (error) {
      console.error('Erro ao verificar quantidade de reservas:', error);
    }
  }

  async function fetchParametros() {
    try {
      const response = await axios.get(`api/parametros?chave.equals=limite-age`);
      setParametro(response.data);
    } catch (error) {
      console.error('Erro ao buscar convênios:', error);
    }
  }

  /* consulta a quantidade de reservas de 5 em 5 segundos */
  useEffect(() => {
    setInterval(() => {
      countReservas();
    }, 5000);
  }, []);

  /* Executa quando a quantidade de reservas é alterada */
  useEffect(() => {
    atualizaAgenda().then(() => {
      if (calendarRef.current) {
        calendarRef.current.getApi().refetchEvents();
      }
    });
  }, [numReservas]);

  /* Carrega as entidades do banco de dados */
  useEffect(() => {
    dispatch(getEntity(id));
    fetchParametros();
    atualizaAgenda();
  }, []);

  /* Define a quantidade de dias permitida de acordo com o parametro "limite-age" */
  useEffect(() => {
    parametro.map(values => {
      setDias(parseInt(values.valor));
    });
  }, [parametro]);

  /* Busca os dados das reservas a partir da data atual e do local selecionado */
  async function atualizaAgenda() {
    const apiUrl = `api/reservas?status.equals=Agendado&data.greaterThan=${dataAtual()}&localId.equals=${id}`;
    try {
      const requestUrl = `${apiUrl}`;
      const response = await axios
        .get<IReserva[]>(requestUrl)
        .then(e => {
          setReservasList(e.data);
          const evento = [];
          e.data.map(event => {
            if (event.local.id.toString() === id) {
              console.log('Events = ' + event.associado.nome);
              if (event.associado.id === account.id) {
                evento.push({
                  id: event.id,
                  title: event.local.nome,
                  start: event.data,
                  groupId: event.associado.id,
                  resourceEditable: false,
                });
              } else {
                evento.push({
                  id: event.id,
                  title: 'RESERVADO',
                  start: event.data,
                  groupId: event.associado.id,
                  color: 'red',
                });
              }
            }
          });
          setCurrentEvents(evento);
        })
        .then(e => {
          setStatusText('OK');
        });
    } catch (error) {
      console.log(error);
    }
  }

  const [callendarButton, setCallendarButton] = useState({
    fontSize: '0.9rem',
    padding: '1vh',
    fontWeight: 500,
    marginBottom: '2px',
    borderRadius: '5px',
  });

  if (statusText != 'OK') {
    return <div>Carregando ...</div>;
  } else {
    return (
      <div>
        <div>
          {loadingLocal ? (
            <p>Carregando ...</p>
          ) : (
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
                {/*<BotaoVoltar link={'/cabanas'} label={locaisEntity.nome} />*/}

                <nav aria-label="breadcrumb">
                  <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                      <Link to="/">Início</Link>
                    </li>
                    <li className="breadcrumb-item">
                      <Link to="/cabanas">Cabanas</Link>
                    </li>
                    <li className="breadcrumb-item active align-middle mt7 " aria-current="page">
                      <Link to={'#'}>
                        {locaisEntity.nome}({currentEvents.length}){' '}
                      </Link>
                    </li>
                  </ol>
                </nav>

                <div>
                  <Chip
                    sx={callendarButton}
                    onClick={() => calendarRef.current.getApi().prev()}
                    label={<ArrowBackIos />}
                    color={'primary'}
                  />
                  {/*&nbsp;*/}
                  {/*<Chip*/}
                  {/*  sx={callendarButton}*/}
                  {/*  style={{ textTransform: 'uppercase' }}*/}
                  {/*  onClick={() => calendarRef.current.getApi().today()}*/}
                  {/*  label={calendarRef.current ? calendarRef.current.getApi().view.title : null}*/}
                  {/*  color={'primary'}*/}
                  {/*/>*/}
                  &nbsp;
                  <Chip
                    sx={callendarButton}
                    onClick={() => calendarRef.current.getApi().next()}
                    color={'primary'}
                    label={<ArrowForwardIos />}
                  />
                </div>
              </div>

              <FullCalendar
                ref={calendarRef}
                bootstrapFontAwesome={{
                  close: 'fa-times',
                  prev: 'fa-chevron-left',
                  next: 'fa-chevron-right',
                  prevYear: 'fa-angle-double-left',
                  nextYear: 'fa-angle-double-right',
                }}
                themeSystem={'bootstrap5'}
                plugins={[dayGridPlugin, interactionPlugin]}
                headerToolbar={{
                  center: 'title',
                  left: '',
                  right: '',
                }}
                views={{
                  dayGridMonth: {
                    titleFormat: { year: 'numeric', month: 'short' },
                  },
                }}
                eventDisplay={'block'}
                selectLongPressDelay={5}
                locale={'pt-br'}
                // height='parent'
                contentHeight={'80vh'}
                selectOverlap={false}
                initialView="dayGridMonth"
                validRange={{
                  start: DataValida.toISOString().substring(0, 10),
                }}
                editable={false}
                selectable={true}
                dragScroll={false}
                weekends={weekendsVisible}
                events={currentEvents}
                select={handleDateSelect}
                eventContent={renderEventContent}
                eventClick={handleEventClick}
                eventsSet={handleEvents}
              />
            </div>
          )}
        </div>
      </div>
    );
  }

  // /* Função que retorna o Calendario */
  // function renderCallendar() {
  //
  //   return (
  //
  //   );
  // }

  function handleWeekendsToggle() {
    setWeekendsVisible(!weekendsVisible);
  }

  /* FUNÇÃO DE CRIAÇÃO DE RESERVAS */
  function handleDateSelect(selectInfo) {
    reservasList.map(event => {
      if (event.data === selectInfo.startStr && event.local.id === locaisEntity.id) {
        this.count = 1;
      }
    });

    if (this.count === 1) {
      Swal.fire({
        icon: 'info',
        title: 'Data reservada',
        text: 'Escolha uma outra data ',
      });
      this.count = 0;
    } else if (difDate(dataAtual(), selectInfo.startStr) >= 1) {
      Swal.fire({
        icon: 'info',
        title: 'Data Limite!',
        text: 'Você não pode fazer uma reserva no passado!',
      });
    } else if (difDate(dataAtual(), selectInfo.startStr) == 0 || difDate(dataAtual(), selectInfo.startStr) > -7) {
      Swal.fire({
        icon: 'info',
        title: 'Data Limite!',
        text: 'Você não pode fazer uma reserva com menos de 7 dias de antecedência!',
      });
    } else {
      navigate('/reserva/new/' + locaisEntity.id + '/' + selectInfo.startStr);
    }
  }

  /* FUNÇÃO DE EDIÇÃO DE RESERVAS */
  function handleEventClick(clickInfo) {
    if (clickInfo.event.groupId != account.id) {
      Swal.fire({
        icon: 'warning',
        title: 'Ops...',
        text: 'Data reservada!',
      });
    } else if (difDate(dataAtual(), clickInfo.startStr) >= 1) {
      Swal.fire({
        icon: 'info',
        title: 'Data Limite!',
        text: 'Você não pode editar uma reserva no passado!',
      });
    } else if (difDate(dataAtual(), clickInfo.event.startStr) == 0 || difDate(dataAtual(), clickInfo.event.startStr) > -7) {
      Swal.fire({
        icon: 'info',
        title: 'Data Limite!',
        text: 'Você não pode editar reservas com menos de 7 dias de antecedência!',
      });
    } else {
      navigate('/reserva/' + clickInfo.event.id + '/' + locaisEntity.id + '/edit');
    }
  }

  function handleEvents(events) {}
};

function renderEventContent(eventInfo) {
  return (
    <>
      <b>{eventInfo.timeText}</b>
      <i>{eventInfo.event.title}</i>
    </>
  );
}

function renderSidebarEvent(event) {
  return (
    <li key={event.id}>
      <b>{event.scheduleDate}</b>
      <i>{event.name}</i>
    </li>
  );
}

export default LocaisAgenda;
