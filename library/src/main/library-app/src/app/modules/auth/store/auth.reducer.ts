import { createReducer, Action, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';
import { IUser } from '../../core/models/auth.model';

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
  //jeszcze register
  on(AuthActions.login, (state, action) => ({
    ...state,
    loading: true,
  }))
)

export function authRecuder(state: AuthState | undefined, action: Action) {
  return _authRecuder(state, action);
}

export function clearError(): any {
  throw new Error('Function not implemented.');
}

