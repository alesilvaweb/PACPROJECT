import dayjs from 'dayjs';
import { IReserva } from 'app/shared/model/reserva.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IDepartamento {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  status?: Status | null;
  created?: string | null;
  modified?: string | null;
  reservas?: IReserva[] | null;
}

export const defaultValue: Readonly<IDepartamento> = {};
