import { createReducer, Action, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';
import { IUser, User } from '../../core/models/auth.model';

export interface AuthState {
  user: IUser | null;
  loading: boolean;
  error: string | null;
}

const initialState: AuthState = {
  user: null,
  loading: false,
  error: null
}

const _authRecuder = createReducer(
  initialState,
  on(AuthActions.login, AuthActions.register, (state, action) => ({
    ...state,
    loading: true,
  })),
  on(AuthActions.loginSuccess, (state, action) => ({
    ...state,
    loading: false,
    user: new User(action.user.login, action.user.email, action.user.role),
    error: null,
  })),
  on(AuthActions.registerSuccess, (state, action) => ({
    ...state,
    loading: false,
    error: null,
  })),
  on(AuthActions.logout, AuthActions.logoutFailure, (state, action) => ({
    ...state,
  })),
  on(AuthActions.logoutSuccess, (state, action) => ({
    ...state,
    user: null,
    error: null,
  })),
  on(AuthActions.loginFailure, AuthActions.registerFailure, (state, action) => ({
    ...state,
    loading: false,
    error: action.error,
  })),
)

export function authRecuder(state: AuthState | undefined, action: Action) {
  return _authRecuder(state, action);
}

export function clearError(): any {
  throw new Error('Function not implemented.');
}

