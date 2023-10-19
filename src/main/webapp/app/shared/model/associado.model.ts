import dayjs from 'dayjs';
import { IReserva } from 'app/shared/model/reserva.model';
import { IContato } from 'app/shared/model/contato.model';
import { IDependente } from 'app/shared/model/dependente.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IAssociado {
  id?: number;
  nome?: string;
  matricula?: string;
  status?: Status | null;
  telefone?: string | null;
  email?: string;
  dataNascimento?: string | null;
  created?: string | null;
  modified?: string | null;
  reservas?: IReserva[] | null;
  contatos?: IContato[] | null;
  dependentes?: IDependente[] | null;
}

export const defaultValue: Readonly<IAssociado> = {};
