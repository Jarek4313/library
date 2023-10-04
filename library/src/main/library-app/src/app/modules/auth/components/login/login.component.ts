import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginForm } from '../../../core/models/forms.model';
import { AppState } from 'src/app/store/app.reducer';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../store/auth.actions';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  //może do ngOnInit??
  loginForm: FormGroup<LoginForm> = new FormGroup({
    login: new FormControl('', {
      validators: [
        Validators.required,
        // Validators.minLength(8),
        // Validators.maxLength(50),
      ],
      nonNullable: true,
    }),
    password: new FormControl('', {
      validators: [
        Validators.required,
        // Validators.minLength(8),
        // Validators.maxLength(75),
      ],
      nonNullable: true,
    }),
  });

  get controls() {
    return this.loginForm.controls;
  }

  constructor(
    private store: Store<AppState>
  ) {}

  onLogin() {
    this.store.dispatch(
      AuthActions.login({ loginData: this.loginForm.getRawValue() })
    )
  }

  ngOnDestroy(): void {
    this.store.dispatch(AuthActions.clearError());
  }
}
