import { IExcercise } from 'app/shared/model/excercise.model';

export interface ICycle {
  id?: string;
  reps?: number | null;
  volume?: number | null;
  excercise?: IExcercise | null;
}

export const defaultValue: Readonly<ICycle> = {};
