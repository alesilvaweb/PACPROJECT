import React, { useState, ChangeEvent, FormEvent } from 'react';
import { Calendar, momentLocalizer, SlotInfo } from 'react-big-calendar';
import moment from 'moment';
import 'moment/locale/pt-br';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import './callendarStyle.css';
import Modal from 'react-modal';
import styled, { ThemeProvider, DefaultTheme, StyledComponent } from 'styled-components';
import axios from 'axios';
import { lightTheme, darkTheme } from './themes';
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';

// Tipos das propriedades do tema
declare module 'styled-components' {
  export interface DefaultTheme {
    background: string;
    color: string;
    buttonBackground: string;
    buttonColor: string;
    buttonHoverBackground: string;
    calendarBackground: string,
    calendarHeaderBackground: string,
    calendarEventBackground: string,
    calendarEventColor: string,
  }
}
// Configurando o localizador para usar o idioma português
moment.locale('pt-br');
const localizer = momentLocalizer(moment);

interface Event {
  id: string;
  title: string;
  start: Date;
  end: Date;
}

interface FormData {
  id:string;
  name: string;
  startDate: string;
  endDate: string;
}

const CalendarContainer = styled.div`
  margin: 20px;

  @media (max-width: 600px) {
    margin: 10px;
  }
`;

const StyledModal = styled(Modal)<{ theme: DefaultTheme }>`
  background: ${({ theme }) => theme.background};
  color: ${({ theme }) => theme.color};
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  margin-left: 40%;
  margin-top: 10%;


  @media (max-width: 600px) {
    padding: 10px;
    width: 90%;
  }
`;

const Form:StyledComponent<any, any> = styled.form`
  display: flex;
  flex-direction: column;
`;

const Label:StyledComponent<any, any> = styled.label`
  margin-bottom: 10px;
`;

const Input:StyledComponent<any, any> = styled.input<{ theme: DefaultTheme }>`
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background: ${({ theme }) => theme.background};
  color: ${({ theme }) => theme.color};

  @media (max-width: 600px) {
    padding: 8px;
  }
`;

const Button:StyledComponent<any, any> = styled.button<{ theme: DefaultTheme }>`
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: ${({ theme }) => theme.buttonBackground};
  color: ${({ theme }) => theme.buttonColor};
  cursor: pointer;

  &:hover {
    background-color: ${({ theme }) => theme.buttonHoverBackground};
  }

  @media (max-width: 600px) {
    padding: 8px;
  }
`;

const ToggleButton:StyledComponent<any, any> = styled(Button)`
  position: absolute;
  top: 50px;
  right: 10px;
`;

const AppOld: React.FC = () => {
  const [events, setEvents] = useState<Event[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState<FormData>({ id:'', name: '', startDate: '', endDate: '' });
  const [theme, setTheme] = useState<DefaultTheme>(lightTheme);
  const [selectedEvent, setSelectedEvent] = useState<Event | null>(null);
  const handleSelectSlot = ({ start, end }: SlotInfo) => {
    setFormData({id:'', name: '', startDate: start.toISOString(), endDate: end.toISOString() });
    setSelectedEvent(null);
    setIsModalOpen(true);
  };

  const handleSelectEvent = (event: Event) => {
    // eslint-disable-next-line no-console
    console.log(event)
    setFormData({
      id:event.id,
      name: event.title,
      startDate: event.start.toISOString(),
      endDate: event.end.toISOString(),
    });
    setSelectedEvent(event);
    setIsModalOpen(true);
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const newEvent: Event = {
      id: selectedEvent ? selectedEvent.id : `${events.length + 1}`,
      title: formData.name,
      start: new Date(formData.startDate),
      end: new Date(formData.endDate),
    };
    try {
      if (selectedEvent) {
        // await axios.put(`https://yourapi.com/endpoint/${selectedEvent.id}`, newEvent);
        setEvents(events.map(event => (event.id === selectedEvent.id ? newEvent : event)));
      } else {
        // await axios.post('https://yourapi.com/endpoint', newEvent);
        setEvents([...events, newEvent]);
      }
      setIsModalOpen(false);
    } catch (error) {
      console.error('Error submitting data:', error);
    }
  };

  const handleDelete = async () => {
    if (selectedEvent) {
      try {
        // await axios.delete(`https://yourapi.com/endpoint/${selectedEvent.id}`);
        setEvents(events.filter(event => event.id !== selectedEvent.id));
        setIsModalOpen(false);
      } catch (error) {
        console.error('Error deleting event:', error);
      }
    }
  };
  const toggleTheme = () => {
    setTheme(theme === lightTheme ? darkTheme : lightTheme);
  };

  return (
    <ThemeProvider theme={theme}>
      <CalendarContainer >
        <ToggleButton onClick={toggleTheme}>
          {theme === lightTheme ? 'Modo Escuro' : 'Modo Claro'}
        </ToggleButton>
        <Calendar
          localizer={localizer}
          events={events}
          startAccessor="start"
          endAccessor="end"
          selectable
          onSelectSlot={handleSelectSlot}
          onSelectEvent={handleSelectEvent}
          style={{ height: 700 }}
          messages={{
            today: 'Hoje',
            previous: 'Voltar',
            next: 'Próximo',
            month: 'Mês',
            week: 'Semana',
            day: 'Dia',
            agenda: 'Agenda',
            date: 'Data',
            time: 'Hora',
            event: 'Evento',
            noEventsInRange: 'Não há eventos neste intervalo.',
            showMore: total => `+ Ver mais (${total})`
          }}
        />
        <StyledModal
          isOpen={isModalOpen}
          onRequestClose={() => setIsModalOpen(false)}
          contentLabel="Appointment Form"

        >
          <h2>{selectedEvent ? 'Edit Event' : 'Add Event'}</h2>
          <Form onSubmit={handleSubmit}>
            <Input
              type="text"
              name="id"
              value={formData.id}
              onChange={handleChange}
              required
            />
            <Label>
              Nome:
              <Input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                required
              />
            </Label>
            <Label>
              Data Inicial:
              <Input
                type="datetime-local"
                name="startDate"
                value={convertDateTimeFromServer(formData.startDate)}
                onChange={handleChange}
                required
              />
            </Label>
            <Label>
              Data Final:
              <Input
                type="datetime-local"
                name="endDate"
                value={convertDateTimeFromServer(formData.endDate)}
                onChange={handleChange}
                required
              />
            </Label>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <Button onClick={() => setIsModalOpen(false)}
                      style={{ width: '150px', marginLeft: '5px' }}>Fechar</Button>
              <Button type="submit">{selectedEvent ? 'Save' : 'Add'}</Button>
              {selectedEvent && <Button onClick={handleDelete}>Delete</Button>}
            </div>
          </Form>
        </StyledModal>
      </CalendarContainer>
    </ThemeProvider>
  );
};

export default AppOld;
