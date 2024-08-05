import React from 'react';
import Modal from 'react-modal';
import styled, { StyledComponent } from 'styled-components';
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';

Modal.setAppElement('#root'); // Para acessibilidade

const ModalContent = styled.div`
  background: ${({ theme }) => theme.background};
  color: ${({ theme }) => theme.color};


  @media (max-width: 600px) {
    padding: 10px;
    width: 90%;
  }
`;

const FormGroup:StyledComponent<any, any> = styled.div`
  margin-bottom: 15px;
`;

const Label:StyledComponent<any, any> = styled.label`
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
`;

const Input:StyledComponent<any, any> = styled.input`
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const Select:StyledComponent<any, any> = styled.select`
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const Button:StyledComponent<any, any> = styled.button`
  background: #007bff;
  color: #fff;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background: #0056b3;
  }
`;

interface EventFormData {
  name: string;
  startDate: string;
  endDate: string;
  recurrence: string;
  recurrenceEndDate:string;
  category: string;
}

interface EventModalProps {
  isOpen: boolean;
  onRequestClose: () => void;
  formData: EventFormData;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void;
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
  onDelete: () => void;
  isEditing: boolean;
}

const EventModal: React.FC<EventModalProps> = ({ isOpen, onRequestClose, formData, onChange, onSubmit, onDelete, isEditing }) => (
  <Modal
    isOpen={isOpen}
    onRequestClose={onRequestClose}
    contentLabel="Event Modal"
    shouldCloseOnOverlayClick={true}
    style={{
      overlay: {
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: 'rgba(0,0,0,0.76)'
      },
      content: {
        position: 'absolute',
        top: '40px',
        left: '40px',
        right: '40px',
        bottom: '40px',
        border: '1px solid #ccc',
        background: '#fff',
        overflow: 'auto',
        WebkitOverflowScrolling: 'touch',
        borderRadius: '4px',
        outline: 'none',
        padding: '20px',
        maxWidth:'600px',
        marginLeft:'40%',
        marginTop:'4%',
        height:'70%'
      }
    }}

  >
    <ModalContent>
      <h2>{isEditing ? 'Editar Reserva' : 'Nova Reserva'}</h2>
      <form onSubmit={onSubmit}>
        <FormGroup>
          <Label htmlFor="name">Motivo Reserva</Label>
          <Input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={onChange}
            required
          />
        </FormGroup>
        <FormGroup>
          <Label htmlFor="startDate">Data Inicial</Label>
          <Input
            type="datetime-local"
            id="startDate"
            name="startDate"
            value={convertDateTimeFromServer(formData.startDate)}
            onChange={onChange}
            required
          />
        </FormGroup>
        <FormGroup>
          <Label htmlFor="endDate">Data Final</Label>
          <Input
            type="datetime-local"
            id="endDate"
            name="endDate"
            value={convertDateTimeFromServer(formData.endDate)}
            onChange={onChange}
            required
          />
        </FormGroup>
        <FormGroup>
          <Label htmlFor="recurrence">Recorrência</Label>
          <Select
            id="recurrence"
            name="recurrence"
            value={formData.recurrence}
            onChange={onChange}
          >
            <option value="none">Nenhuma</option>
            <option value="daily">Diário</option>
            <option value="weekly">Semanal</option>
            <option value="monthly">Mensal</option>
          </Select>

        </FormGroup>
        {formData.recurrence !== 'none' && (
          <FormGroup>
            <Label htmlFor="recurrenceEndDate">Final da Recorrência</Label>
            <Input
            type="datetime-local"
            id="recurrenceEndDate"
            name="recurrenceEndDate"
            value={convertDateTimeFromServer(formData.recurrenceEndDate)}
          onChange={onChange}
          required
        />
      </FormGroup>
        )}
        <FormGroup>
          <Label htmlFor="category">Categoria</Label>
          <Select
            id="category"
            name="category"
            value={formData.category}
            onChange={onChange}
          >
            <option value="work">Work</option>
            <option value="personal">Personal</option>
            <option value="holiday">Holiday</option>
          </Select>
        </FormGroup>
        <Button type="submit">{isEditing ? 'Atualizar' : 'Salvar'} </Button>
        {isEditing && (
          <Button type="button" onClick={onDelete} style={{ background: '#dc3545', marginLeft: '10px' }}>
            Excluir
          </Button>
        )}
      </form>
    </ModalContent>
  </Modal>
);

export default EventModal;
