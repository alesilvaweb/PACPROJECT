import './locais.scss';

import React, { useEffect, useRef, useState } from 'react';
import { getEntity } from './local.reducer';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate, useParams } from 'react-router-dom';
import { dataAtual, difDate } from 'app/shared/util/date-utils';
import FullCalendar from '@fullcalendar/react';
import { IReserva } from 'app/shared/model/reserva.model';
import { Chip, useMediaQuery } from '@mui/material';
import { ArrowBackIos, ArrowForwardIos } from '@mui/icons-material';
import axios from 'axios';
import Swal from 'sweetalert2';
import BotaoVoltar from 'app/components/botaoVoltar';

const LocaisAgenda = args => {
  const dispatch = useAppDispatch();
  const [reservasList, setReservasList] = useState([]);
  const locaisEntity = useAppSelector(state => state.local.entity);
  const parametrosList = useAppSelector(state => state.parametro.entities);
  const [currentEvents, setCurrentEvents] = useState([]);
  const [weekendsVisible, setWeekendsVisible] = useState(true);
  const navigate = useNavigate();
  const loadingLocal = useAppSelector(state => state.local.loading);
  const [modal, setModal] = useState(false);
  const [statusText, setStatusText] = useState('');
  const account = useAppSelector(state => state.authentication.account);
  const toggle = () => setModal(!modal);
  const { id } = useParams<'id'>();

  const data = new Date();
  const dias = 7;

  function addDays(date, days) {
    date.setDate(date.getDate() + days);
    return date;
  }

  const DataValida = addDays(data, dias);

  useEffect(() => {
    dispatch(getEntity(id));
    atualizaAgenda();
  }, []);

  useEffect(() => {
    const evento = [];

    reservasList.map(event => {
      if (event.local.id.toString() === id) {
        console.log('Events = ' + event.associado.id);

        if (event.associado.id === account.id) {
          // @ts-ignore
          evento.push({
            id: event.id,
            title: event.local.nome,
            start: event.data,
            groupId: event.associado.id,
            resourceEditable: false,
          });
        } else {
          // @ts-ignore
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
    handleEvents(evento);
  }, [reservasList]);

  async function atualizaAgenda() {
    const apiUrl = 'api/reservas';
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
                // @ts-ignore
                evento.push({
                  id: event.id,
                  title: event.local.nome,
                  start: event.data,
                  groupId: event.associado.id,
                  resourceEditable: false,
                });
              } else {
                // @ts-ignore
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
          handleEvents(evento);
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

  const matches = useMediaQuery('(min-width:600px)');

  // if (matches===true){
  // setCallendarButton({
  //
  //     fontSize: '0.5rem',
  //     padding: '5px',
  //     fontWeight: 400,
  //     marginBottom: '2px',
  //     borderRadius: '5px',
  //
  //   })
  //
  // }else{
  //   setCallendarButton({
  //
  //     fontSize: '0.9rem',
  //     padding: '10px',
  //     fontWeight: 400,
  //     marginBottom: '2px',
  //     borderRadius: '5px',
  //
  //
  // })}

  const calendarRef = useRef<FullCalendar>(null!);

  if (statusText != 'OK') {
    return <div>Carregando ...</div>;
  } else {
    return (
      <div className="app">
        <div className="demo-app-sidebar">
          {loadingLocal ? (
            <p>Carregando ...</p>
          ) : (
            <div className="app-main">
              <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
                <BotaoVoltar link={'/agenda'} label={locaisEntity.nome} />
                &nbsp;
                <div>
                  <Chip
                    sx={callendarButton}
                    onClick={() => calendarRef.current.getApi().prev()}
                    label={<ArrowBackIos />}
                    color={'primary'}
                  />
                  &nbsp;
                  <Chip
                    sx={callendarButton}
                    style={{ textTransform: 'uppercase' }}
                    onClick={() => calendarRef.current.getApi().today()}
                    label={calendarRef.current ? calendarRef.current.getApi().view.title : null}
                    color={'primary'}
                  />
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
                  center: '',
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
                initialEvents={currentEvents}
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

  function handleEvents(events) {
    setCurrentEvents(events);
  }
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
