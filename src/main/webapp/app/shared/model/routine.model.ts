import dayjs from 'dayjs';
import { IExcercise } from 'app/shared/model/excercise.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';

export interface IRoutine {
  id?: string;
  name?: string | null;
  dateStarted?: string | null;
  dateEnded?: string | null;
  goalDate?: string | null;
  startingBodyWeight?: number | null;
  endingBodyWeight?: number | null;
  excercises?: IExcercise[] | null;
  users?: IApplicationUser[] | null;
}

export const defaultValue: Readonly<IRoutine> = {};
