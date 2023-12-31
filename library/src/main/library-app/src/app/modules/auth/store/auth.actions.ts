import { createAction, props } from '@ngrx/store';
import { LoginData, IUser, RegisterData } from '../../core/models/auth.model';

const LOGIN = '[AUTH] Login';
const LOGIN_SUCCESS = '[AUTH] Login Success';
const LOGIN_FAILURE = '[AUTH] Login Failure';

const REGISTER = '[Auth] Register';
const REGISTER_SUCCESS = '[Auth] Register Success';
const REGISTER_FAILURE = '[Auth] Register Failure';

const LOGOUT = '[Auth] Logout';
const LOGOUT_SUCCESS = '[Auth] Logout Success';
const LOGOUT_FAILURE = '[Auth] Logout Failure';

const CLEAR_ERROR_TYPE = '[Auth] Clear Error';

//LOGIN
export const login = createAction(
  LOGIN,
  props<{loginData: LoginData }>()
);

export const loginSuccess = createAction(
  LOGIN_SUCCESS,
  props<{user: IUser }>()
);

export const loginFailure = createAction(
  LOGIN_FAILURE,
  props<{error: string }>()
);

//REGISTER
export const register = createAction(
  REGISTER,
  props<{ registerData: RegisterData }>()
);

export const registerSuccess = createAction(REGISTER_SUCCESS);

export const registerFailure = createAction(
  REGISTER_FAILURE,
  props<{ error: string }>()
);

//LOGOUT
export const logout = createAction(LOGOUT);

export const logoutSuccess = createAction(LOGOUT_SUCCESS);

export const logoutFailure = createAction(LOGOUT_FAILURE);

//CLEAR ERROR
export const clearError = createAction(CLEAR_ERROR_TYPE);