import { IUser } from 'app/shared/model/user.model';
import { IRoutine } from 'app/shared/model/routine.model';

export interface IApplicationUser {
  id?: string;
  ranking?: number | null;
  internalUser?: IUser | null;
  routines?: IRoutine[] | null;
}

export const defaultValue: Readonly<IApplicationUser> = {};
