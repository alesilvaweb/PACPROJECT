import dayjs from 'dayjs';
import { IImagensConvenio } from 'app/shared/model/imagens-convenio.model';
import { IRedesSociaisConvenio } from 'app/shared/model/redes-sociais-convenio.model';
import { ICategoria } from 'app/shared/model/categoria.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IConvenio {
  id?: number;
  nome?: string;
  titulo?: string | null;
  descricao?: string | null;
  endereco?: string | null;
  telefone?: string | null;
  email?: string | null;
  localizacao?: string | null;
  status?: Status | null;
  created?: string | null;
  modified?: string | null;
  imagens?: IImagensConvenio[] | null;
  redesSociais?: IRedesSociaisConvenio[] | null;
  categoria?: ICategoria | null;
}

export const defaultValue: Readonly<IConvenio> = {};
