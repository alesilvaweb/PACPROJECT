import React, { useEffect, useState } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { useDispatch, useSelector } from 'react-redux';
// import { getReservations, createReservation } from 'app/entities/reservation/reservation-reducer';
import './quadras.scss';

const localizer = momentLocalizer(moment);

const ScheduleCourt = () => {
  const dispatch = useDispatch();
  // const reservations = useSelector(state => state.reservation.entities);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [availableTimes, setAvailableTimes] = useState([]);
  const [selectedTime, setSelectedTime] = useState('');
  const [court, setCourt] = useState('Quadra 1'); // Exemplo de identificação da quadra

  useEffect(() => {
    // dispatch(getReservations());
  }, []);

  useEffect(() => {
    if (selectedDate) {
      // Gerar todos os horários possíveis das 8h às 20h em intervalos de 1 hora
      const allTimes = [];
      for (let i = 8; i <= 20; i++) {
        allTimes.push(`${i}:00`);
      }

      // Filtrar os horários já reservados para a data selecionada
      // const reservedTimes = reservations
      //   .filter(reservation => moment(reservation.date).isSame(selectedDate, 'day'))
      //   .map(reservation => reservation.time);

      // Obter os horários disponíveis
      // const availableTimes = allTimes.filter(time => !reservedTimes.includes(time));
      setAvailableTimes(availableTimes);
    }
  }, [selectedDate ]);

  const handleSelectSlot = slotInfo => {
    setSelectedDate(slotInfo.start);
  };

  const handleSubmit = () => {
    const reservation = {
      date: moment(selectedDate).format('YYYY-MM-DD'),
      time: selectedTime,
      court,
    };
    // dispatch(createReservation(reservation));
  };

  // const events = reservations.map(reservation => ({
  //   start: new Date(reservation.date + 'T' + reservation.time),
  //   end: new Date(reservation.date + 'T' + reservation.time),
  //   title: reservation.court,
  // }));

  return (
    <div className="schedule-court">
      <h2>Agendamento de Quadra Esportiva</h2>
      <div className="calendar-container">
        <Calendar
          localizer={localizer}
          // events={events}
          startAccessor="start"
          endAccessor="end"
          selectable
          onSelectSlot={handleSelectSlot}
          style={{ height: 500 }}
        />
      </div>
      <div className="date-picker-container">
        <DatePicker selected={selectedDate} onChange={date => setSelectedDate(date)} />
      </div>
      <div className="available-times-container">
        <h3>Horários Disponíveis</h3>
        <ul className="available-times-list">
          {availableTimes.map((time, index) => (
            <li key={index} className="available-time-item" onClick={() => setSelectedTime(time)}>
              {time}
            </li>
          ))}
        </ul>
      </div>
      {selectedDate && selectedTime && (
        <div className="reservation-form">
          <h3>Selecionar Quadra</h3>
          <input
            type="text"
            value={court}
            onChange={e => setCourt(e.target.value)}
          />
          <button onClick={handleSubmit}>
            Agendar Horário
          </button>
        </div>
      )}
    </div>
  );
};

export default ScheduleCourt;
