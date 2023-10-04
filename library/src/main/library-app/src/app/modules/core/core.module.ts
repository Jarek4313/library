import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { HeaderComponent } from './components/header/header.component';
import { ErrorHandlingInterceptor } from './interceptors/error-handling.interceptor';

@NgModule({
  declarations: [HeaderComponent],
  imports: [SharedModule, HttpClientModule, RouterLink, RouterLinkActive],
  exports: [HeaderComponent],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlingInterceptor,
      multi: true,
    },
    // {
    //   provide: MatPaginatorIntl,
    //   useClass: MatPaginatorCustomIntl,
    // },
  ],
})
export class CoreModule {}
