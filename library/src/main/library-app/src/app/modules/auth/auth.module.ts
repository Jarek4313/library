import { NgModule } from '@angular/core';

import { AuthRoutingModule } from './auth-routing.module';
import { SharedModule } from '../shared/shared.module';
import { LoginComponent } from './components/login/login.component';
import { SigninComponent } from './components/signin/signin.component';
import { RegisterComponent } from './components/register/register.component';

@NgModule({
  declarations: [LoginComponent, SigninComponent, RegisterComponent],
  imports: [SharedModule, AuthRoutingModule],
})
export class AuthModule {}
