import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserProfileRoutingModule } from './user-profile-routing.module';
import { UserProfileComponent } from './user-profile.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { AccountSettingsComponent } from './account-settings/account-settings.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ReactiveFormsModule } from '../../../../../node_modules/@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    UserProfileRoutingModule,
    TabsModule,
    FontAwesomeModule,
    ReactiveFormsModule
  ],
  declarations: [UserProfileComponent, AccountSettingsComponent]
})
export class UserProfileModule { }
