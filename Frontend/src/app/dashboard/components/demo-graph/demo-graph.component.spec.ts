import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoGraphComponent } from './demo-graph.component';

describe('DemoGraphComponent', () => {
  let component: DemoGraphComponent;
  let fixture: ComponentFixture<DemoGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DemoGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DemoGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
