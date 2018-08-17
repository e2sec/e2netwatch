import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountSettingsComponent } from './account-settings.component';
import { ReactiveFormsModule } from '../../../../../../node_modules/@angular/forms';
import { FontAwesomeModule } from '../../../../../../node_modules/@fortawesome/angular-fontawesome';

describe('AccountSettingsComponent', () => {
  let component: AccountSettingsComponent;
  let fixture: ComponentFixture<AccountSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, FontAwesomeModule],
      declarations: [ AccountSettingsComponent ]
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
