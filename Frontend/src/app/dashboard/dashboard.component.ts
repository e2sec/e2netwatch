import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Animations } from '../animations/animations';

@Component({
  selector: 'e2nw-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.less'],
  animations: [
    Animations.slideInOutSidebar,
    Animations.slideInOutMainContent
  ]
})
export class DashboardComponent implements OnInit {
  sidebarState: string;
  constructor(private titleService: Title) { }

  ngOnInit() {
    this.titleService.setTitle('Dashboard');
  }

  toggleSidebar(sideBarState: string) {
    this.sidebarState = sideBarState;
  }

}
