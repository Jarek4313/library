import { NgModule } from '@angular/core';
import { AdministrationRoutingModule } from './administration-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SharedModule } from '../shared/shared.module';
import { CategoryManagmentComponent } from './dashboard/category-managment/category-managment.component';
import { BookManagmentComponent } from './dashboard/book-managment/book-managment.component';
import { AddBookFormComponent } from './dashboard/book-managment/add-book-form/add-book-form.component';

@NgModule({
  declarations: [
    DashboardComponent,
    CategoryManagmentComponent,
    BookManagmentComponent,
    AddBookFormComponent
  ],
  imports: [SharedModule, AdministrationRoutingModule],
})
export class AdministrationModule {}
