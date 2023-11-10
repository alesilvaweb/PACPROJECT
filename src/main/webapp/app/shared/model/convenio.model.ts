import dayjs from 'dayjs';
import { IImagensConvenio } from 'app/shared/model/imagens-convenio.model';
import { IRedesSociaisConvenio } from 'app/shared/model/redes-sociais-convenio.model';
import { IDescontoConvenio } from 'app/shared/model/desconto-convenio.model';
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
  imagemContentType?: string;
  imagem?: string;
  logoContentType?: string | null;
  logo?: string | null;
  bannerContentType?: string | null;
  banner?: string | null;
  localizacao?: string | null;
  status?: Status | null;
  created?: string | null;
  modified?: string | null;
  imagens?: IImagensConvenio[] | null;
  redesSociais?: IRedesSociaisConvenio[] | null;
  descontos?: IDescontoConvenio[] | null;
  categoria?: ICategoria | null;
}

export const defaultValue: Readonly<IConvenio> = {};
