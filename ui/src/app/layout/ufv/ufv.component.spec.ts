import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UfvComponent } from './ufv.component';

describe('UfvComponent', () => {
  let component: UfvComponent;
  let fixture: ComponentFixture<UfvComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UfvComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UfvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
