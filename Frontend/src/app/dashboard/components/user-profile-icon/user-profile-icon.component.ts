import { Component, OnInit } from '@angular/core';
import { UserProfileService } from '../../../services/user-profile.service';
import { UserProfile } from '../../../models/user-profile';

@Component({
  selector: 'e2nw-user-profile-icon',
  templateUrl: './user-profile-icon.component.html',
  styleUrls: ['./user-profile-icon.component.less']
})
export class UserProfileIconComponent implements OnInit {

  constructor(private userProfileService: UserProfileService) { }
  profile: UserProfile;
  ngOnInit() {
    this.userProfileService.currentUserProfile.subscribe(profile => this.profile = profile);
  }

}
