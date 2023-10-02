import { LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from './modules/core/core.module';
import { registerLocaleData } from '@angular/common';
import localePL from '@angular/common/locales/pl';
import { AuthModule } from './modules/auth/auth.module';

registerLocaleData(localePL);

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    CoreModule,
    AuthModule,
    // AuthModule
    // StoreModule.forRoot({ auth: authReducer }),
    // EffectsModule.forRoot([AuthEffects]),
    // NotifierModule.withConfig(customNotifier),
  ],
  providers: [
    {
      provide: LOCALE_ID,
      useValue: 'pl',
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
