import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoGraphComponent } from './demo-graph.component';
import { HttpClientModule } from '@angular/common/http';

describe('DemoGraphComponent', () => {
  let component: DemoGraphComponent;
  let fixture: ComponentFixture<DemoGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
      ],
      declarations: [DemoGraphComponent]
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
