import './locais.scss';

import React, { useEffect, useRef, useState } from 'react';
import { getEntity } from './local.reducer';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { dataAtual, difDate, formatData } from 'app/shared/util/date-utils';
import FullCalendar from '@fullcalendar/react';
import { IReserva } from 'app/shared/model/reserva.model';
import { Chip } from '@mui/material';
import { ArrowBackIos, ArrowForwardIos } from '@mui/icons-material';
import axios from 'axios';
import Swal from 'sweetalert2';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import Spinner from 'app/components/spinner';

const LocaisAgenda = args => {
  const dispatch = useAppDispatch();
  const [reservasList, setReservasList] = useState([]);
  const locaisEntity = useAppSelector(state => state.local.entity);
  const account = useAppSelector(state => state.authentication.account);
  const loadingLocal = useAppSelector(state => state.local.loading);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const [currentEvents, setCurrentEvents] = useState([]);
  const [weekendsVisible, setWeekendsVisible] = useState(true);
  const navigate = useNavigate();
  const [modal, setModal] = useState(false);
  const [statusText, setStatusText] = useState('');
  const [parametro, setParametro] = useState([]);
  const [numReservas, setNumReservas] = useState(0);
  const [count, setCount] = useState(0);
  const [diasStart, setDiasStart] = useState(0);
  const [diasEnd, setDiasEnd] = useState('');

  const calendarRef = useRef<FullCalendar>(null!);

  const { id } = useParams<'id'>();

  const DataValida = addDays(diasStart);

  let arrData = diasEnd.split('/');
  const DataFinal = (arrData[2] + '-' + arrData[1] + '-' + arrData[0]).toString();

  function addDays(days) {
    const data = new Date();
    data.setDate(data.getDate() + days);
    return data;
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
      const response = await axios.get(`api/parametros?chave.in=limite-start&chave.in=limite-end&page=0&size=20`);
      setParametro(response.data);
    } catch (error) {
      console.error('Erro ao buscar Parametros:', error);
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

  /* Define a quantidade de dias permitida de acordo com os parametros "limite-start inicio e limite-end final" */
  useEffect(() => {
    parametro.map(a => {
      if (a.chave.toString() === 'limite-start') {
        setDiasStart(parseInt(a.valor));
      }
      if (a.chave.toString() === 'limite-end') {
        setDiasEnd(a.valor);
      }
    });
  }, [parametro]);

  /* Busca os dados das reservas a partir da data atual e do local selecionado */
  async function atualizaAgenda() {
    const apiUrl = `api/reservas?size=999&data.greaterThan=${dataAtual()}&localId.equals=${id}`;
    try {
      const requestUrl = `${apiUrl}`;
      const response = await axios
        .get<IReserva[]>(requestUrl)
        .then(e => {
          setReservasList(e.data);
          const evento = [];
          e.data.map(event => {
            if (event.local.id.toString() === id) {
              if (event.status === 'Bloqueado') {
                evento.push({
                  id: event.id,
                  title: '-RESERVADO AAPM',
                  start: event.data,
                  end: event.descricao,
                  groupId: '000',
                  color: 'black',
                });
              } else {
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
    return <Spinner text={'reservas'} />;
  } else {
    return (
      <div>
        <div>
          {loadingLocal ? (
            <Spinner text={'locais'} />
          ) : (
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
                {/*<BotaoVoltar link={'/Locais'} label={locaisEntity.nome} />*/}

                <nav aria-label="breadcrumb">
                  <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                      <Link to="/">Início</Link>
                    </li>
                    <li className="breadcrumb-item">
                      <Link to="/Locais">Locais</Link>
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
                  //end: DataFinal,
                  //start: isAdmin ? '' : DataValida.toISOString().substring(0, 10),
                  end: isAdmin ? '' : DataFinal,
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
        <br />
        <br />
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
    } else if (difDate(dataAtual(), selectInfo.startStr) == 0 || difDate(dataAtual(), selectInfo.startStr) > -diasStart) {
      Swal.fire({
        icon: 'info',
        title: 'Data Limite!',
        text: `Você não pode fazer uma reserva com menos de ${diasStart} dias de antecedência!`,
      });
    } else if (selectInfo.startStr > DataFinal && !isAdmin) {
      Swal.fire({
        icon: 'info',
        title: 'Data Limite!',
        text: `São permitidas reservas até ${formatData(DataFinal)}.`,
      });
    } else {
      navigate('/reserva/new/' + locaisEntity.id + '/' + selectInfo.startStr);
    }
  }

  /* FUNÇÃO DE EDIÇÃO DE RESERVAS */
  function handleEventClick(clickInfo) {
    if (isAdmin) {
      navigate('/reserva/' + clickInfo.event.id + '/' + locaisEntity.id + '/edit');
    } else {
      if (clickInfo.event.groupId != account.id) {
        if (clickInfo.event.groupId === '000') {
          Swal.fire({
            icon: 'info',
            title: 'Data bloqueada!',
            text: `Esta data foi bloqueada pela AAPM.`,
          });
        } else {
          Swal.fire({
            icon: 'info',
            title: 'Data reservada!',
            text: `Esta data já está reservada para ${locaisEntity.nome}`,
          });
        }
      } else if (difDate(dataAtual(), clickInfo.startStr) >= 1) {
        Swal.fire({
          icon: 'info',
          title: 'Data Limite!',
          text: 'Você não pode editar uma reserva no passado!',
        });
      } else if (difDate(dataAtual(), clickInfo.event.startStr) == 0 || difDate(dataAtual(), clickInfo.event.startStr) > -diasStart) {
        Swal.fire({
          icon: 'info',
          title: 'Data Limite!',
          text: `Você não pode editar reservas com menos de ${diasStart} dias de antecedência!`,
        });
      } else {
        navigate('/reserva/' + clickInfo.event.id + '/' + locaisEntity.id + '/edit');
      }
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
