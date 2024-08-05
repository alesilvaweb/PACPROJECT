import React, { useState, useEffect, ChangeEvent, FormEvent } from 'react';
import { Calendar, momentLocalizer, SlotInfo } from 'react-big-calendar';
import moment from 'moment';
import axios from 'axios';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import styled, { createGlobalStyle } from 'styled-components';
import EventModal from './EventModal';
import './callendarStyle.css'

moment.locale('pt-br');
const localizer = momentLocalizer(moment);

interface Event {
  id: string;
  title: string;
  start: Date;
  end: Date;
  recurrence?: string;
  recurrenceEndDate?: Date;
  category: string;
  className?:string;
}

interface EventFormData {
  name: string;
  startDate: string;
  endDate: string;
  recurrence: string;
  recurrenceEndDate: string;
  category: string;
}

const GlobalStyle = createGlobalStyle`
  .rbc-calendar {
    background-color: #fff;
    color: #000;
  }

  .rbc-toolbar {
    background-color: #f0f0f0;
  }

  .rbc-event {
    color: #fff; /* Text color */
  }

  .rbc-event-work {
    background-color: #007bff;
  }

  .rbc-event-personal {
    background-color: #28a745;
  }

  .rbc-event-holiday {
    background-color: #ffc107;
  }

  .rbc-event-birthday {
    background-color: #ff5733; /* Example additional category */
  }

  .rbc-event-anniversary {
    background-color: #9b59b6; /* Example additional category */
  }
`;

const CalendarContainer = styled.div`
  height: 100vh;
`;

const App2: React.FC = () => {
  const [events, setEvents] = useState<Event[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState<EventFormData>({
    name: '',
    startDate: '',
    endDate: '',
    recurrence: 'none',
    recurrenceEndDate: '',
    category: 'work',
  });
  const [selectedEvent, setSelectedEvent] = useState<Event | null>(null);

  useEffect(() => {
    axios.get('/api/events')
      .then(response => {
        // eslint-disable-next-line no-console
        console.log('Fetched events:', response.data);
        if (Array.isArray(response.data)) {
          setEvents(response.data);
        } else {
          console.error('Fetched data is not an array:', response.data);
        }
      })
      .catch(error => {
        console.error('Error fetching events:', error);
      });
  }, []);

  const handleSelectSlot = ({ start, end }: SlotInfo) => {
    setFormData({ name: '', startDate: start.toISOString(), endDate: end.toISOString(), recurrence: 'none',   recurrenceEndDate: '', category: 'work' });
    setSelectedEvent(null);
    setIsModalOpen(true);
  };

  const handleSelectEvent = (event: Event) => {
    setFormData({
      name: event.title,
      startDate: event.start.toISOString(),
      endDate: event.end.toISOString(),
      recurrence: event.recurrence || 'none',
      recurrenceEndDate: event.recurrenceEndDate ? event.recurrenceEndDate.toISOString() : '',
      category: event.category || 'work',
    });
    setSelectedEvent(event);
    setIsModalOpen(true);
  };

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
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
      recurrence: formData.recurrence,
      recurrenceEndDate: formData.recurrenceEndDate ? new Date(formData.recurrenceEndDate) : undefined,
      category: formData.category,
    };

    try {
      if (selectedEvent) {
        if (selectedEvent.recurrence && selectedEvent.recurrence !== 'none') {
          // Update all recurring events
          const updatedEvents = events.map(event => {
            if (event.id.startsWith(selectedEvent.id.split('-')[0])) {
              return { ...event, ...newEvent, className: `rbc-event-${newEvent.category}` };
            }
            return event;
          });
          // await axios.put(`/api/events/${selectedEvent.id.split('-')[0]}`, newEvent);
          setEvents(updatedEvents);
        } else {
          // await axios.put(`/api/events/${selectedEvent.id}`, newEvent);
          setEvents(events.map(event => (event.id === selectedEvent.id ? { ...newEvent, className: `rbc-event-${newEvent.category}` } : event)));
        }
      } else {
        // await axios.post('/api/events', newEvent);
        setEvents([...events, { ...newEvent, className: `rbc-event-${newEvent.category}` }]);
      }
      setIsModalOpen(false);
    } catch (error) {
      console.error('Error submitting data:', error);
    }
  };

  // eslint-disable-next-line @typescript-eslint/require-await
  const handleDelete = async () => {
    if (selectedEvent) {
      try {
        const deleteWholeSeries = window.confirm('Do you want to delete the entire series of recurring events?');
        if (deleteWholeSeries && selectedEvent.recurrence && selectedEvent.recurrence !== 'none') {
          // Delete all recurring events
          const seriesId = selectedEvent.id.split('-')[0];
          // await axios.delete(`/api/events/${seriesId}`);
          setEvents(events.filter(event => !event.id.startsWith(seriesId)));
        } else {
          // await axios.delete(`/api/events/${selectedEvent.id}`);
          setEvents(events.filter(event => event.id !== selectedEvent.id));
        }
        setIsModalOpen(false);
      } catch (error) {
        console.error('Error deleting event:', error);
      }
    }
  };

  const generateRecurringEvents = (event: Event) => {
    const recurringEvents: Event[] = [];
    const currentStart = moment(event.start);
    const currentEnd = moment(event.end);
    const recurrenceEnd = moment(event.recurrenceEndDate);

    while (currentStart.isBefore(recurrenceEnd)) {
      recurringEvents.push({
        ...event,
        id: `${event.id}-${currentStart.format('YYYYMMDD')}`,
        start: currentStart.toDate(),
        end: currentEnd.toDate(),
        className: `rbc-event-${event.category}`,
      });

      switch (event.recurrence) {
        case 'daily':
          currentStart.add(1, 'day');
          currentEnd.add(1, 'day');
          break;
        case 'weekly':
          currentStart.add(1, 'week');
          currentEnd.add(1, 'week');
          break;
        case 'monthly':
          currentStart.add(1, 'month');
          currentEnd.add(1, 'month');
          break;
        default:
          return recurringEvents;
      }
    }

    return recurringEvents;
  };

  const extendedEvents = Array.isArray(events) ? events.reduce((acc: Event[], event: Event) => {
    const recurringEvents = event.recurrence && event.recurrence !== 'none'
      ? generateRecurringEvents(event)
      : [{ ...event, color: `red` }];

    return acc.concat(recurringEvents);
  }, []) : [];

  // eslint-disable-next-line no-console
  console.log(extendedEvents);
  return (
    <CalendarContainer>
      <GlobalStyle />
      <Calendar
        localizer={localizer}
        events={extendedEvents}
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
      <EventModal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        formData={formData}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onDelete={handleDelete}
        isEditing={!!selectedEvent}
      />
    </CalendarContainer>
  );
};

export default App2;
