import dayjs from 'dayjs';
import { IAssociado } from 'app/shared/model/associado.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IDependente {
  id?: number;
  nome?: string;
  dataNascimento?: string;
  parentesco?: string;
  status?: Status | null;
  created?: string | null;
  modified?: string | null;
  associado?: number | null;
}

export const defaultValue: Readonly<IDependente> = {};
