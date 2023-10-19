import dayjs from 'dayjs';
import { IAssociado } from 'app/shared/model/associado.model';
import { TipoContato } from 'app/shared/model/enumerations/tipo-contato.model';

export interface IContato {
  id?: number;
  tipo?: TipoContato;
  contato?: string;
  created?: string | null;
  modified?: string | null;
  associado?: IAssociado | null;
}

export const defaultValue: Readonly<IContato> = {};
