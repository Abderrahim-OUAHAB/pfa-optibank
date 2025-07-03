import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatRadioModule } from '@angular/material/radio';
import { UsersComponent } from './users/users.component';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { PendingComponent } from './pending/pending.component';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { RejectedComponent } from './rejected/rejected.component';
import { SuspendedComponent } from './suspended/suspended.component';
import { DetailCompteComponent } from './detail-compte/detail-compte.component';
@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    UsersComponent,
    PendingComponent,
    RejectedComponent,
    SuspendedComponent,DetailCompteComponent

  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatDatepickerModule,MatSelectModule,
    MatNativeDateModule,MatRadioModule ,MatTableModule
  ]
})
export class AuthModule { }
