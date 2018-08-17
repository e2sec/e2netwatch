import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'e2nw-account-settings',
  templateUrl: './account-settings.component.html',
  styleUrls: ['./account-settings.component.less']
})
export class AccountSettingsComponent implements OnInit {
  profileForm: FormGroup;
  constructor() { }

  ngOnInit() {
    this.createForm();
  }
  private createForm() {
    this.profileForm = new FormGroup({
      firstname: new FormControl('', [Validators.required]),
      lastname: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      newPassword: new FormControl('', []),
      currentPassword: new FormControl('', [Validators.required])
    });
  }

  updateAccount() {
    if (this.profileForm.valid) {
      console.log(this.profileForm.value);
    }
  }
}
