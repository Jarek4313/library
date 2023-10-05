import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CategoryManagmentComponent } from './dashboard/category-managment/category-managment.component';
import { BookManagmentComponent } from './dashboard/book-managment/book-managment.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    children: [
      // {
      //   path: 'users',
      //   component: AddBookFormComponent,
      // },
      {
        path: 'books',
        component: BookManagmentComponent,
      },
      {
        path: 'category',
        component: CategoryManagmentComponent,
      },
    ],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdministrationRoutingModule {}
