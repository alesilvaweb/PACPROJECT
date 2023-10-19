import dayjs from 'dayjs';
import { IIconsRedesSociais } from 'app/shared/model/icons-redes-sociais.model';
import { IConvenio } from 'app/shared/model/convenio.model';

export interface IRedesSociaisConvenio {
  id?: number;
  nome?: string;
  descricao?: string | null;
  endereco?: string;
  created?: string | null;
  modified?: string | null;
  icon?: IIconsRedesSociais | null;
  convenio?: IConvenio | null;
}

export const defaultValue: Readonly<IRedesSociaisConvenio> = {};
