import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { LoginModel } from '../models/login-model';
import { Router } from '@angular/router';

@Component({
  selector: 'e2nw-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  model: LoginModel;
  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.createForm();
  }

  private createForm() {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', Validators.required),
    });
  }

  login() {
    this.submitted = true;
    if (this.loginForm.valid) {
      this.model = this.loginForm.value;
      this.authService.login(this.model, '/');
    }

  }

}
