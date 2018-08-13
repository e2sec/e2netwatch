import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { RouterTestingModule } from '@angular/router/testing';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { Component } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  @Component({ selector: 'e2nw-user-profile-icon', template: '' })
  class UserProfileIconStubComponent { }

  @Component({ selector: 'e2nw-header', template: '' })
  class HeaderStubComponent { }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, TooltipModule.forRoot(), FontAwesomeModule, BrowserAnimationsModule],
      declarations: [DashboardComponent, HeaderStubComponent, SideBarComponent, UserProfileIconStubComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
