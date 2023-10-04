import { Actions, createEffect, ofType } from "@ngrx/effects";
import * as AuthActions from './auth.actions';
import { catchError, map, of, switchMap } from "rxjs";
import { AuthService } from "../../core/services/auth-service";
import { Router } from "@angular/router";
import { Inject, Injectable } from "@angular/core";

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

  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private router: Router
  ) {}
}