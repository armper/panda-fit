import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRoutine, defaultValue } from 'app/shared/model/routine.model';

export const ACTION_TYPES = {
  SEARCH_ROUTINES: 'routine/SEARCH_ROUTINES',
  FETCH_ROUTINE_LIST: 'routine/FETCH_ROUTINE_LIST',
  FETCH_ROUTINE: 'routine/FETCH_ROUTINE',
  CREATE_ROUTINE: 'routine/CREATE_ROUTINE',
  UPDATE_ROUTINE: 'routine/UPDATE_ROUTINE',
  PARTIAL_UPDATE_ROUTINE: 'routine/PARTIAL_UPDATE_ROUTINE',
  DELETE_ROUTINE: 'routine/DELETE_ROUTINE',
  RESET: 'routine/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRoutine>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type RoutineState = Readonly<typeof initialState>;

// Reducer

export default (state: RoutineState = initialState, action): RoutineState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ROUTINES):
    case REQUEST(ACTION_TYPES.FETCH_ROUTINE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ROUTINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ROUTINE):
    case REQUEST(ACTION_TYPES.UPDATE_ROUTINE):
    case REQUEST(ACTION_TYPES.DELETE_ROUTINE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ROUTINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ROUTINES):
    case FAILURE(ACTION_TYPES.FETCH_ROUTINE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ROUTINE):
    case FAILURE(ACTION_TYPES.CREATE_ROUTINE):
    case FAILURE(ACTION_TYPES.UPDATE_ROUTINE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ROUTINE):
    case FAILURE(ACTION_TYPES.DELETE_ROUTINE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ROUTINES):
    case SUCCESS(ACTION_TYPES.FETCH_ROUTINE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROUTINE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ROUTINE):
    case SUCCESS(ACTION_TYPES.UPDATE_ROUTINE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ROUTINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ROUTINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/routines';
const apiSearchUrl = 'api/_search/routines';

// Actions

export const getSearchEntities: ICrudSearchAction<IRoutine> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ROUTINES,
  payload: axios.get<IRoutine>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IRoutine> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ROUTINE_LIST,
  payload: axios.get<IRoutine>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IRoutine> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ROUTINE,
    payload: axios.get<IRoutine>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRoutine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ROUTINE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRoutine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ROUTINE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IRoutine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ROUTINE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRoutine> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ROUTINE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
