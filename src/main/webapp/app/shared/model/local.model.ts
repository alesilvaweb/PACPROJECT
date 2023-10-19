import dayjs from 'dayjs';
import { IReserva } from 'app/shared/model/reserva.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface ILocal {
  id?: number;
  nome?: string;
  descricao?: string;
  capacidade?: number;
  imagenContentType?: string;
  imagen?: string;
  observacoes?: string | null;
  localizacao?: string | null;
  status?: Status | null;
  valor?: number;
  cor?: string | null;
  created?: string | null;
  modified?: string | null;
  reservas?: IReserva[] | null;
}

export const defaultValue: Readonly<ILocal> = {};
