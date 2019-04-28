import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UfvUploadComponent } from './ufv-upload.component';

describe('UfvUploadComponent', () => {
  let component: UfvUploadComponent;
  let fixture: ComponentFixture<UfvUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UfvUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UfvUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
