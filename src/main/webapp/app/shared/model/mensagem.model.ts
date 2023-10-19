import dayjs from 'dayjs';
import { ITipo } from 'app/shared/model/tipo.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IMensagem {
  id?: number;
  titulo?: string;
  descricao?: string;
  conteudo?: string | null;
  imagenContentType?: string | null;
  imagen?: string | null;
  link?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  status?: Status | null;
  created?: string | null;
  modified?: string | null;
  tipo?: ITipo | null;
}

export const defaultValue: Readonly<IMensagem> = {};
