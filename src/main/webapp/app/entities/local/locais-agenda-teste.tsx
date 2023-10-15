import { useState } from 'react';

import { Scheduler } from '@aldabil/react-scheduler';
import type { ProcessedEvent, SchedulerHelpers } from '@aldabil/react-scheduler/types';
import TextField from '@mui/material/TextField';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';
import React from 'react';
import { ptBR } from 'date-fns/locale';
import { useNavigate } from 'react-router-dom';
import { Box } from '@mui/material';

interface CustomEditorProps {
  scheduler: SchedulerHelpers;
}
const CustomEditor = ({ scheduler }: CustomEditorProps) => {
  const navigate = useNavigate();

  const event = scheduler.edited;

  // Make your own form/state
  const [state, setState] = useState({
    title: event?.title || '',
    numPessoas: event?.numPessoas || '',
  });
  const [error, setError] = useState('');
  const [errorNumPessoas, setErrorNumPessoas] = useState('');

  const handleChange = (value: string, name: string) => {
    setState(prev => {
      return {
        ...prev,
        [name]: value,
      };
    });
  };
  const handleSubmit = async () => {
    // Your own validation
    if (state.title.length < 3) {
      return setError('Min 3 letters');
    }
    if (state.numPessoas > 50) {
      return setErrorNumPessoas('Max 50 Pessoas');
    }

    try {
      scheduler.loading(true);

      /**Simulate remote data saving */
      const added_updated_event = (await new Promise(res => {
        /**
         * Make sure the event have 4 mandatory fields
         * event_id: string|number
         * title: string
         * start: Date|string
         * end: Date|string
         */
        setTimeout(() => {
          res({
            event_id: event?.event_id || Math.random(),
            title: state.title,
            start: scheduler.state.start.value,
            end: scheduler.state.end.value,
            numPessoas: state.numPessoas,
          });
        }, 1000);
      })) as ProcessedEvent;

      scheduler.onConfirm(added_updated_event, event ? 'edit' : 'create');
      scheduler.close();
    } finally {
      scheduler.loading(false);
    }
  };
  return (
    <Box component="form" noValidate autoComplete="off">
      <div style={{ padding: '5rem' }}>
        <h3>Nova Reserva </h3>
        <br />
        <TextField
          label="Motivo da Reserva"
          style={{ marginBottom: '5rem' }}
          focused
          value={state.title}
          onChange={e => handleChange(e.target.value, 'title')}
          error={!!error}
          helperText={error}
          required={true}
          fullWidth
        />

        <TextField
          label="Quantidade de pessoas"
          focused
          type={'number'}
          error={!!errorNumPessoas}
          helperText={errorNumPessoas}
          value={state.numPessoas}
          onChange={e => handleChange(e.target.value, 'numPessoas')}
          required={true}
          fullWidth
        />
      </div>

      <DialogActions>
        <Button onClick={scheduler.close}>Cancelar</Button>
        <Button onClick={handleSubmit}>Salvar</Button>
      </DialogActions>
    </Box>
  );
};

function LocaisAgendaTeste({ currentEvents, id, locaisEntity }) {
  const navigate = useNavigate();
  console.log(currentEvents.length);
  if (currentEvents.length == 0) {
    alert('teste');
    // @ts-ignore
    useNavigate('/locais/' + id);
  }
  return (
    <Scheduler
      view="month"
      day={null}
      week={null}
      events={currentEvents}
      customEditor={scheduler => <CustomEditor scheduler={scheduler} />}
      viewerExtraComponent={(fields, event) => {
        return (
          <div>
            <h3>Reserva </h3>
            <p>Número de pessoas: {locaisEntity.numPessoas || 'Nothing...'}</p>
          </div>
        );
      }}
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
      eventRenderer={({ event }) => {
        if (event.title === 'teste') {
          return (
            <div
              style={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                height: '100%',
                background: '#c70f0f',
              }}
            >
              <div style={{ height: 20, background: 'rgba(47,19,19,0.71)', color: 'black' }}>
                {event.start.toLocaleTimeString('en-US', {
                  timeStyle: 'short',
                })}
              </div>
              <div>{event.title}</div>
              <div style={{ height: 20, background: 'rgba(113,47,47,0.71)', color: 'green' }}>
                {event.end.toLocaleTimeString('pt-BR', { timeStyle: 'short' })}
              </div>
            </div>
          );
        }
        return null;
      }}
    />
  );
}

export default LocaisAgendaTeste;
