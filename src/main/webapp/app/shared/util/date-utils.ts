import dayjs from 'dayjs';

import { APP_LOCAL_DATETIME_FORMAT } from 'app/config/constants';
import { format, parseISO } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { now } from 'lodash';

export const convertDateTimeFromServer = date => (date ? dayjs(date).format(APP_LOCAL_DATETIME_FORMAT) : null);

export const convertDateTimeToServer = date => (date ? dayjs(date).toDate() : null);

export const displayDefaultDateTime = () => dayjs().startOf('day').format(APP_LOCAL_DATETIME_FORMAT);

export const formatData = (date: string) => {
  return format(parseISO(date), 'dd/MM/yyyy', {
    locale: ptBR,
  });
};
export const dataAtual = () => {
  return format(new Date(now()), 'yyyy-MM-dd', {
    locale: ptBR,
  });
};

export const difDate = (date1: string, date2: string) => {
  // @ts-ignore
  const diffInMs = new Date(date1) - new Date(date2);
  const diffInDays = diffInMs / (1000 * 60 * 60 * 24);
  return diffInDays;
};
