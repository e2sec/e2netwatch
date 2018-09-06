import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { LoginModel } from '../models/login-model';
import { Title } from '@angular/platform-browser';
import { AppState, selectAuthState } from '../store/app.states';
import { Store } from '@ngrx/store';
import { Login } from '../store/actions/auth.actions';
import { Observable } from 'rxjs';

@Component({
  selector: 'e2nw-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  model: LoginModel = new LoginModel();
  errorMessage: string | null;
  getState: Observable<any>;

  constructor(
    private titleService: Title,
    private store: Store<AppState>
  ) {
    this.getState = this.store.select(selectAuthState);
  }

  ngOnInit() {
    this.titleService.setTitle('Login');
    this.createForm();
    this.getState.subscribe((state) => {
      this.errorMessage = state.errorMessage;
    });
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
      this.store.dispatch(new Login(this.loginForm.value));
    }

  }

}
