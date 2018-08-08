import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { HeaderComponent } from './components/header/header.component';
import { Title, BrowserModule } from '../../../node_modules/@angular/platform-browser';

@NgModule({
  imports: [
    CommonModule,
    DashboardRoutingModule
  ],
  providers: [],
  declarations: [DashboardComponent, SideBarComponent, HeaderComponent]
})
export class DashboardModule { }
