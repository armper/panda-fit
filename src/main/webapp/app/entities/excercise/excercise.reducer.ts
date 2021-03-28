import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IExcercise, defaultValue } from 'app/shared/model/excercise.model';

export const ACTION_TYPES = {
  SEARCH_EXCERCISES: 'excercise/SEARCH_EXCERCISES',
  FETCH_EXCERCISE_LIST: 'excercise/FETCH_EXCERCISE_LIST',
  FETCH_EXCERCISE: 'excercise/FETCH_EXCERCISE',
  CREATE_EXCERCISE: 'excercise/CREATE_EXCERCISE',
  UPDATE_EXCERCISE: 'excercise/UPDATE_EXCERCISE',
  PARTIAL_UPDATE_EXCERCISE: 'excercise/PARTIAL_UPDATE_EXCERCISE',
  DELETE_EXCERCISE: 'excercise/DELETE_EXCERCISE',
  RESET: 'excercise/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IExcercise>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ExcerciseState = Readonly<typeof initialState>;

// Reducer

export default (state: ExcerciseState = initialState, action): ExcerciseState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_EXCERCISES):
    case REQUEST(ACTION_TYPES.FETCH_EXCERCISE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EXCERCISE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_EXCERCISE):
    case REQUEST(ACTION_TYPES.UPDATE_EXCERCISE):
    case REQUEST(ACTION_TYPES.DELETE_EXCERCISE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_EXCERCISE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_EXCERCISES):
    case FAILURE(ACTION_TYPES.FETCH_EXCERCISE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EXCERCISE):
    case FAILURE(ACTION_TYPES.CREATE_EXCERCISE):
    case FAILURE(ACTION_TYPES.UPDATE_EXCERCISE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_EXCERCISE):
    case FAILURE(ACTION_TYPES.DELETE_EXCERCISE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_EXCERCISES):
    case SUCCESS(ACTION_TYPES.FETCH_EXCERCISE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXCERCISE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_EXCERCISE):
    case SUCCESS(ACTION_TYPES.UPDATE_EXCERCISE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_EXCERCISE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_EXCERCISE):
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

const apiUrl = 'api/excercises';
const apiSearchUrl = 'api/_search/excercises';

// Actions

export const getSearchEntities: ICrudSearchAction<IExcercise> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_EXCERCISES,
  payload: axios.get<IExcercise>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IExcercise> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_EXCERCISE_LIST,
  payload: axios.get<IExcercise>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IExcercise> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EXCERCISE,
    payload: axios.get<IExcercise>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IExcercise> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EXCERCISE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IExcercise> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EXCERCISE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IExcercise> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_EXCERCISE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IExcercise> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EXCERCISE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
