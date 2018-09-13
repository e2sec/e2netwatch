import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountSettingsComponent } from './account-settings.component';
import { ReactiveFormsModule } from '../../../../../../node_modules/@angular/forms';
import { FontAwesomeModule } from '../../../../../../node_modules/@fortawesome/angular-fontawesome';
import { StoreModule } from '@ngrx/store';
import { reducers } from '../../../../store/app.states';
import { EffectsModule } from '@ngrx/effects';
import { ProfileEffects } from '../../../../store/effects/profile.effects';
import { HttpClientModule } from '@angular/common/http';

describe('AccountSettingsComponent', () => {
  let component: AccountSettingsComponent;
  let fixture: ComponentFixture<AccountSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        ReactiveFormsModule,
        FontAwesomeModule,
        StoreModule.forRoot(reducers, {}),
        EffectsModule.forRoot([ProfileEffects]),
      ],
      declarations: [AccountSettingsComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
