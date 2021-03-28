import { ICycle } from 'app/shared/model/cycle.model';
import { IRoutine } from 'app/shared/model/routine.model';
import { ExcerciseType } from 'app/shared/model/enumerations/excercise-type.model';

export interface IExcercise {
  id?: string;
  type?: ExcerciseType | null;
  currentVolume?: number | null;
  startingVolume?: number | null;
  goalVolume?: number | null;
  cycles?: ICycle[] | null;
  routine?: IRoutine | null;
}

export const defaultValue: Readonly<IExcercise> = {};
