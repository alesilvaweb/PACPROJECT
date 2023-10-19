import dayjs from 'dayjs';
import { ILocal } from 'app/shared/model/local.model';
import { IAssociado } from 'app/shared/model/associado.model';
import { IDepartamento } from 'app/shared/model/departamento.model';
import { StatusReserva } from 'app/shared/model/enumerations/status-reserva.model';

export interface IReserva {
  id?: number;
  motivoReserva?: string;
  descricao?: string | null;
  numPessoas?: number;
  status?: StatusReserva | null;
  data?: string;
  somenteFuncionarios?: boolean | null;
  created?: string | null;
  modified?: string | null;
  local?: ILocal | null;
  associado?: IAssociado | null;
  departamento?: IDepartamento | null;
}

export const defaultValue: Readonly<IReserva> = {
  somenteFuncionarios: false,
};
