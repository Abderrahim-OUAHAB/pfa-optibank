import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailCompteComponent } from './detail-compte.component';

describe('DetailCompteComponent', () => {
  let component: DetailCompteComponent;
  let fixture: ComponentFixture<DetailCompteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailCompteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailCompteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
