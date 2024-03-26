import { IConvenio } from 'app/shared/model/convenio.model';

export interface IDescontoConvenio {
  id?: number;
  desconto?: string | null;
  tipo?: string | null;
  descricao?: string | null;
  convenio?: IConvenio | null;
}

export const defaultValue: Readonly<IDescontoConvenio> = {};
