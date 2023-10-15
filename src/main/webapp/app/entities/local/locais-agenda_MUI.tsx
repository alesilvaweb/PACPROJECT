import './locais.scss';
import { Scheduler } from '@aldabil/react-scheduler';
import { EventActions, ProcessedEvent, ViewEvent } from '@aldabil/react-scheduler/types';
import { EVENTS } from '../../entities/local/events';
import React, { useEffect, useState } from 'react';
import getStore, { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate, useParams } from 'react-router-dom';
import { getEntities as getReservas } from 'app/entities/reserva/reserva.reducer';
import { getEntities, getEntities as getLocais, getEntity } from 'app/entities/local/local.reducer';
import { toast } from 'react-toastify';
import promise = toast.promise;
import { isEmpty } from 'react-jhipster';
import { ptBR } from 'date-fns/locale';

export default function CallendarAapm() {
  const dispatch = useAppDispatch();
  const reservasList = useAppSelector(state => state.reservas.entities);
  const locaisEntity = useAppSelector(state => state.locais.entity);
  const parametrosList = useAppSelector(state => state.parametros.entities);
  const [currentEvents, setCurrentEvents] = useState([]);
  const [weekendsVisible, setWeekendsVisible] = useState(true);
  const navigate = useNavigate();
  const [modal, setModal] = useState(false);
  const locaisList = useAppSelector(state => state.locais.entities);
  const account = useAppSelector(state => state.authentication.account);
  const toggle = () => setModal(!modal);
  const { id } = useParams<'id'>();
  useEffect(() => {
    dispatch(getEntity(id));
    dispatch(
      getReservas({
        page: 0,
        size: 9999999,
        sort: 'id',
      })
    );
    dispatch(
      getLocais({
        page: 1,
        size: 1,
      })
    );
    dispatch(
      getEntities({
        page: 1,
        size: 1,
      })
    );
  }, []);

  const evento = [];

  // useEffect(() => {
  //   reservasList.map(event => {
  //
  //     if (event.local.id.toString() === id) {
  //       if (event.associado.id === account.id) {
  //         currentEvents.push({
  //           event_id: event.id,
  //           title: event.local.nome,
  //           start: new Date(event.data),
  //           end: new Date(event.data),
  //           admin_id: 2,
  //           color: '#50b500',
  //         });
  //       } else {
  //         currentEvents.push({
  //           event_id: event.id,
  //           title: event.local.nome,
  //           start: new Date(event.data),
  //           end: new Date(event.data),
  //           admin_id: 2,
  //           color: '#1337a1',
  //         });
  //       }
  //     }
  //   });
  // }, [])

  // useEffect(() => {
  //   reservasList.map(event => {
  //     if (event.local.id.toString() === id) {
  //       // console.log('Events = ' + event.associado.nome);
  //
  //       if (event.associado.id === account.id) {
  //         currentEvents.push( {
  //             event_id: event.id,
  //             title: event.local.nome,
  //             start: new Date(event.data),
  //             end: new Date(event.data),
  //             admin_id: 2,
  //             color: "#50b500"
  //           }
  //         )
  //
  //       } else {
  //         currentEvents.push( {
  //             event_id: event.id,
  //             title: event.local.nome,
  //             start: new Date(event.data),
  //             end: new Date(event.data),
  //             admin_id: 2,
  //             color: "#1337a1"
  //           }
  //        )
  //       }
  //     }
  //   });
  //
  //
  // }, [reservasList]);

  const fetchRemote = async (query: ViewEvent): Promise<ProcessedEvent[]> => {
    return new Promise(res => {
      dispatch(
        getReservas({
          page: 0,
          size: 9999999,
          sort: 'id',
        })
      ).then(
        reservasList.map(event => {
          if (event.local.id.toString() === id) {
            if (event.associado.id === account.id) {
              currentEvents.push({
                event_id: event.id,
                title: event.local.nome,
                start: new Date(event.data),
                end: new Date(event.data),
                admin_id: 2,
                color: '#50b500',
              });
            } else {
              currentEvents.push({
                event_id: event.id,
                title: event.local.nome,
                start: new Date(event.data),
                end: new Date(event.data),
                admin_id: 2,
                color: '#1337a1',
              });
            }
          }
        })
      );
      setCurrentEvents(currentEvents);
      res(currentEvents);
    });
  };
  console.log;
  const handleConfirm = async (event: ProcessedEvent, action: EventActions): Promise<ProcessedEvent> => {
    console.log('handleConfirm =', action, event);

    /**
     * Make sure to return 4 mandatory fields:
     * event_id: string|number
     * title: string
     * start: Date|string
     * end: Date|string
     * ....extra other fields depend on your custom fields/editor properties
     */
    // Simulate http request: return added/edited event
    return new Promise((res, rej) => {
      if (action === 'edit') {
        /** PUT event to remote DB */
      } else if (action === 'create') {
        /**POST event to remote DB */
      }

      const isFail = Math.random() > 0.6;
      // Make it slow just for testing
      setTimeout(() => {
        if (isFail) {
          rej('Ops... Faild');
        } else {
          res({
            ...event,
            event_id: event.event_id || Math.random(),
          });
        }
      }, 3000);
    });
  };

  const handleDelete = async (deletedId: string): Promise<string> => {
    // Simulate http request: return the deleted id
    return new Promise((res, rej) => {
      setTimeout(() => {
        res(deletedId);
      }, 3000);
    });
  };

  return renderCallendar();

  function renderCallendar() {
    return (
      <Scheduler
        view="month"
        getRemoteEvents={fetchRemote}
        onConfirm={handleConfirm}
        onDelete={handleDelete}
        fields={[
          {
            name: 'numPessoas',
            type: 'input',
            config: { label: 'Número de pessoas', required: true, variant: 'outlined', decimal: true },
          },
        ]}
        translations={{
          navigation: {
            month: 'Mês',
            week: 'Semana',
            day: 'dia',
            today: 'Hoje',
          },
          form: {
            addTitle: 'Reserva',
            editTitle: 'Editar',
            confirm: 'Confirm',
            delete: 'Deletar',
            cancel: 'Cancelar',
          },
          event: {
            title: 'Motivo da reserva',
            start: 'Start',
            end: 'End',
            allDay: 'All Day',
          },
          // validation: {
          //   required: "Requrido",
          //   invalidEmail: " Email Inválido",
          //   onlyNumbers: "Only Numbers Allowed",
          //   min: "Minimo {{min}} letras",
          //   max: "Maximo {{max}} letras"
          // },
          moreEvents: 'Mais...',
          loading: 'Carregando...',
        }}
        locale={ptBR}
      />
    );
  }
}
