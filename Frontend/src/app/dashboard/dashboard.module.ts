import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { HeaderComponent } from './components/header/header.component';
import { UserProfileIconComponent } from './components/user-profile-icon/user-profile-icon.component';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { DemoGraphComponent } from './components/demo-graph/demo-graph.component';
import { HttpClientModule } from '@angular/common/http';

library.add(fas);

@NgModule({
  imports: [
    CommonModule,
    DashboardRoutingModule,
    PopoverModule,
    FontAwesomeModule,
    TooltipModule,
    HttpClientModule,
  ],
  providers: [],
  declarations: [DashboardComponent, SideBarComponent, HeaderComponent, UserProfileIconComponent, DemoGraphComponent]
})
export class DashboardModule { }