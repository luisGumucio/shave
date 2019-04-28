import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UfvListComponent } from './ufv-list.component';

describe('UfvListComponent', () => {
  let component: UfvListComponent;
  let fixture: ComponentFixture<UfvListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UfvListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UfvListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
