import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemListDetailComponent } from './item-list-detail.component';

describe('ItemListDetailComponent', () => {
  let component: ItemListDetailComponent;
  let fixture: ComponentFixture<ItemListDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ItemListDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemListDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
