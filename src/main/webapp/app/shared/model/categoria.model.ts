import dayjs from 'dayjs';
import { IConvenio } from 'app/shared/model/convenio.model';

export interface ICategoria {
  id?: number;
  categoria?: string;
  descricao?: string | null;
  created?: string | null;
  modified?: string | null;
  convenios?: IConvenio[] | null;
}

export const defaultValue: Readonly<ICategoria> = {};
