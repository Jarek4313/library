import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RegisterForm } from 'src/app/modules/core/models/forms.model';
import { AppState } from 'src/app/store/app.reducer';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../store/auth.actions';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup<RegisterForm> = new FormGroup({
    email: new FormControl('', {
      validators: [Validators.required, Validators.email],
      nonNullable: true,
    }),
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
    repeatedPassword: new FormControl('', {
      validators: [
        Validators.required,
        // Validators.minLength(8),
        // Validators.maxLength(75),
      ],
      nonNullable: true,
    }),
  });
  notMatchingPasswordsError = '';

  get controls(): RegisterForm {
    return this.registerForm.controls;
  }
  
  constructor(
    private store: Store<AppState>,
  ) {}

  onRegister() {
    const { login, email, password, repeatedPassword } = this.registerForm.getRawValue();

    if (password != repeatedPassword) {
      this.notMatchingPasswordsError = 'Hasła nie mogą być różne!';
    }

    this.store.dispatch(
      AuthActions.register({ registerData: {login, email, password}})
    )
  }

  ngOnDestroy(): void {
    this.store.dispatch(
      AuthActions.clearError()
    )
  }
}
