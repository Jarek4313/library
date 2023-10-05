import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-header-nav-item',
  templateUrl: './header-nav-item.component.html',
  styleUrls: ['./header-nav-item.component.scss']
})
export class HeaderNavItemComponent {
  @Input() route!: string; 
  @Input() text!: string;
  @Input() color!: string;
}
