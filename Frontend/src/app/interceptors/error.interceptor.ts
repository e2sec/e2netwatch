import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Store } from '@ngrx/store';
import { AppState } from '../store/app.states';
import { Logout } from '../store/actions/auth.actions';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private store: Store<AppState>) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(error => {
            if (error.status === 401) {
                this.store.dispatch(new Logout());
            }
            return throwError(error);
        }));
    }
}
