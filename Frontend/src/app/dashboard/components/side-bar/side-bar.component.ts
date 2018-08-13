import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'e2nw-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.less'],
})
export class SideBarComponent implements OnInit {
  collapseSidebar = false;
  sidebarState = 'extended';
  @Output() sideBarStateChanged = new EventEmitter<string>();
  constructor() { }

  ngOnInit() {
  }

  toggleSidebar() {
    this.collapseSidebar = !this.collapseSidebar;
    this.sideBarStateChanged.emit(this.sidebarState === 'collapsed' ? this.sidebarState = 'extended' : this.sidebarState = 'collapsed');
  }

}
