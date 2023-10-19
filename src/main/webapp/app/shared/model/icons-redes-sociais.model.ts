import { IRedesSociaisConvenio } from 'app/shared/model/redes-sociais-convenio.model';

export interface IIconsRedesSociais {
  id?: number;
  nome?: string;
  descricao?: string | null;
  icon?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  redeSocials?: IRedesSociaisConvenio[] | null;
}

export const defaultValue: Readonly<IIconsRedesSociais> = {};
