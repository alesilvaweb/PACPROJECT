import React, { useState, useEffect, ChangeEvent, FormEvent } from 'react';
import { Calendar, momentLocalizer, SlotInfo, View } from 'react-big-calendar';
import moment from 'moment';
import axios from 'axios';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import styled, { createGlobalStyle } from 'styled-components';
import EventModal from './EventModal';
import './callendarStyle.css';

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
    background-color: #ff5733;
  }

  .rbc-event-anniversary {
    background-color: #9b59b6;
  }

  .rbc-timeslot-group {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px;
  }

  .rbc-timeslot-group.free {
    background-color: #e0f7fa;
  }

  .rbc-timeslot-group.booked {
    background-color: #ffebee;
  }
`;

const CalendarContainer = styled.div`
  height: 100vh;
`;

const App: React.FC = () => {
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
  const [view, setView] = useState<View>('week');

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
    setFormData({ name: '', startDate: start.toISOString(), endDate: end.toISOString(), recurrence: 'none', recurrenceEndDate: '', category: 'work' });
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

  // eslint-disable-next-line @typescript-eslint/require-await
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
        setEvents([...events, { ...newEvent }]);
      }
    } catch (error) {
      console.error('Error saving event:', error);
    }

    setIsModalOpen(false);
  };

  // eslint-disable-next-line @typescript-eslint/require-await
  const handleDelete = async () => {
    if (selectedEvent) {
      try {
        // await axios.delete(`/api/events/${selectedEvent.id}`);
        setEvents(events.filter(event => !event.id.startsWith(selectedEvent.id.split('-')[0])));
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
      : [{ ...event, className: `rbc-event-${event.category}` }];

    return acc.concat(recurringEvents);
  }, []) : [];

  const renderTimeslots = (date: Date) => {
    const timeslots = [];
    for (let hour = 0; hour < 24; hour++) {
      const start = new Date(date);
      start.setHours(hour, 0, 0, 0);
      const end = new Date(start);
      end.setHours(hour + 1, 0, 0, 0);

      const isBooked = events.some(event =>
        (start >= new Date(event.start) && start < new Date(event.end)) ||
        (end > new Date(event.start) && end <= new Date(event.end)) ||
        (start < new Date(event.start) && end > new Date(event.end))
      );

      timeslots.push(
        <div key={hour} className={`rbc-timeslot-group ${isBooked ? 'booked' : 'free'}`}>
          {isBooked ? 'Agendado' : 'Livre'}
        </div>
      );
    }
    return timeslots;
  };


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
        views={['month', 'week', 'day']}
        step={60}
        timeslots={1}
        view={view}
        onView={setView}
        components={{
          day: {
            event: ({ event }) => <span>{event.title} Ativo </span>,


          },
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

export default App;
