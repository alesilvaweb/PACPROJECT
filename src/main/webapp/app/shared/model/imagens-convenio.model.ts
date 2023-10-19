import dayjs from 'dayjs';
import { IConvenio } from 'app/shared/model/convenio.model';

export interface IImagensConvenio {
  id?: number;
  titulo?: string;
  descricao?: string | null;
  imagenContentType?: string;
  imagen?: string;
  created?: string | null;
  modified?: string | null;
  convenio?: IConvenio | null;
}

export const defaultValue: Readonly<IImagensConvenio> = {};
