import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatInputModule } from '@angular/material/input';

import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatRadioModule } from '@angular/material/radio';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { DetailCompteComponent } from '../../auth/detail-compte/detail-compte.component';
import { AccountsComponent } from '../accounts.component';
import { AccountRoutingModule } from './account-routing.module';
@NgModule({
  declarations: [
    AccountsComponent,
  ],
  imports: [
    CommonModule,
    AccountRoutingModule,ReactiveFormsModule,
    FormsModule,
    HttpClientModule,MatInputModule,
    MatToolbarModule,
        CommonModule,
    MatRadioModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,MatSnackBarModule,MatFormFieldModule,
    MatListModule,
    MatMenuModule,
  ]
})
export class AccountModule { }
