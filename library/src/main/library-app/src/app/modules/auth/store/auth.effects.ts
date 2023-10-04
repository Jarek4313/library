import { Actions, createEffect, ofType } from "@ngrx/effects";
import * as AuthActions from './auth.actions';
import { catchError, map, of, switchMap } from "rxjs";

import { Router } from "@angular/router";
import { Inject, Injectable } from "@angular/core";
import { AuthService } from "../../core/services/auth.service";

@Injectable()//bez  tego wywalało to Can't resolve all parameters for ParamDecorator: (?, ?, ?).
export class AuthEffects {

  login$ = createEffect(() => 
    this.actions$.pipe(
      ofType(AuthActions.login),//tylko ten typ akcji
      switchMap((action) => {
        return this.authService.login(action.loginData).pipe(
          map((user) => {
            this.router.navigate(['/']);
            return AuthActions.loginSuccess({ user: {...user} })
          }),
          catchError((err) => of(AuthActions.loginFailure({ error: err })))
        );//pipe
      })
    )
  );

  register$ = createEffect(() => 
      this.actions$.pipe(
        ofType(AuthActions.register),
        switchMap((action) => {
          return this.authService.register(action.registerData).pipe(
            map((user) => {
              this.router.navigate(['/login']);
              return AuthActions.registerSuccess();
            }),
            catchError((err) => {
              return of(AuthActions.loginFailure({error: err}));
            })
          )
        })
      )
  )

  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private router: Router
  ) {}
}