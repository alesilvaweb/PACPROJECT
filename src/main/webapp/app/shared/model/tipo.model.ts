import { IMensagem } from 'app/shared/model/mensagem.model';

export interface ITipo {
  id?: number;
  tipo?: string;
  mensagems?: IMensagem[] | null;
}

export const defaultValue: Readonly<ITipo> = {};
