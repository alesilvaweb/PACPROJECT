import dayjs from 'dayjs';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IParametro {
  id?: number;
  parametro?: string;
  descricao?: string | null;
  chave?: string | null;
  valor?: string;
  status?: Status | null;
  created?: string | null;
  modified?: string | null;
}

export const defaultValue: Readonly<IParametro> = {};
