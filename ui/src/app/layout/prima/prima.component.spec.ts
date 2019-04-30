import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PrimaComponent } from './prima.component';

describe('PrimaComponent', () => {
  let component: PrimaComponent;
  let fixture: ComponentFixture<PrimaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PrimaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PrimaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
