import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserProfileRoutingModule } from './user-profile-routing.module';
import { UserProfileComponent } from './user-profile.component';
import { TabsModule } from '../../../../../node_modules/ngx-bootstrap/tabs';

@NgModule({
  imports: [
    CommonModule,
    UserProfileRoutingModule,
    TabsModule
  ],
  declarations: [UserProfileComponent]
})
export class UserProfileModule { }
