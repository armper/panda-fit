import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICycle, defaultValue } from 'app/shared/model/cycle.model';

export const ACTION_TYPES = {
  SEARCH_CYCLES: 'cycle/SEARCH_CYCLES',
  FETCH_CYCLE_LIST: 'cycle/FETCH_CYCLE_LIST',
  FETCH_CYCLE: 'cycle/FETCH_CYCLE',
  CREATE_CYCLE: 'cycle/CREATE_CYCLE',
  UPDATE_CYCLE: 'cycle/UPDATE_CYCLE',
  PARTIAL_UPDATE_CYCLE: 'cycle/PARTIAL_UPDATE_CYCLE',
  DELETE_CYCLE: 'cycle/DELETE_CYCLE',
  RESET: 'cycle/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICycle>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type CycleState = Readonly<typeof initialState>;

// Reducer

export default (state: CycleState = initialState, action): CycleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CYCLES):
    case REQUEST(ACTION_TYPES.FETCH_CYCLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CYCLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CYCLE):
    case REQUEST(ACTION_TYPES.UPDATE_CYCLE):
    case REQUEST(ACTION_TYPES.DELETE_CYCLE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_CYCLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_CYCLES):
    case FAILURE(ACTION_TYPES.FETCH_CYCLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CYCLE):
    case FAILURE(ACTION_TYPES.CREATE_CYCLE):
    case FAILURE(ACTION_TYPES.UPDATE_CYCLE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_CYCLE):
    case FAILURE(ACTION_TYPES.DELETE_CYCLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CYCLES):
    case SUCCESS(ACTION_TYPES.FETCH_CYCLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CYCLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CYCLE):
    case SUCCESS(ACTION_TYPES.UPDATE_CYCLE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_CYCLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CYCLE):
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

const apiUrl = 'api/cycles';
const apiSearchUrl = 'api/_search/cycles';

// Actions

export const getSearchEntities: ICrudSearchAction<ICycle> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CYCLES,
  payload: axios.get<ICycle>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<ICycle> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CYCLE_LIST,
  payload: axios.get<ICycle>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ICycle> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CYCLE,
    payload: axios.get<ICycle>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICycle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CYCLE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICycle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CYCLE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICycle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_CYCLE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICycle> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CYCLE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
