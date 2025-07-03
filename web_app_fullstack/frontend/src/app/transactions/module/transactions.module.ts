import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { TransactionFormComponent } from '../components/transaction-form/transaction-form.component';
import { TransactionListComponent } from '../components/transaction-list/transaction-list.component';
import { TransactionsRoutingModule } from './transactions-routing.module';
@NgModule({
  declarations: [
    TransactionFormComponent,
    TransactionListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatSnackBarModule,FormsModule,
    MatIconModule,
    TransactionsRoutingModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule,MatTableModule
  ]
})
export class TransactionsModule { }