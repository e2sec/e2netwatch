import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { LoginModel } from '../models/login-model';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'e2nw-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  model: LoginModel;
  constructor(
    private titleService: Title,
    private authService: AuthService) { }

  ngOnInit() {
    this.titleService.setTitle('Login');
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
