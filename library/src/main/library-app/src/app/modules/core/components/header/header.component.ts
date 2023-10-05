import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { selectAuthUser } from 'src/app/modules/auth/store/auth.selectors';
import { User } from '../../models/auth.model';
import { AppState } from 'src/app/store/app.reducer';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../../auth/store/auth.actions';
import { AppConst } from '../../models/app.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  user$: Observable<User | null> = this.store.select(selectAuthUser);
  AppConst = AppConst;

  constructor(
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
      
  }

  logout() {
    this.store.dispatch(AuthActions.logout());
    // console.log(this.user$);
  }

  isAdmin(role: string) {
    return role === AppConst.ADMIN.toString();
  }
}
