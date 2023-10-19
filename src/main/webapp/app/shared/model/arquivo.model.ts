import dayjs from 'dayjs';
import { StatusArquivo } from 'app/shared/model/enumerations/status-arquivo.model';

export interface IArquivo {
  id?: number;
  nome?: string;
  descricao?: string | null;
  arquivoContentType?: string;
  arquivo?: string;
  status?: StatusArquivo | null;
  created?: string | null;
  modified?: string | null;
}

export const defaultValue: Readonly<IArquivo> = {};
