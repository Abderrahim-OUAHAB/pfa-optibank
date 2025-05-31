import { Component, OnInit } from '@angular/core';
import { Transaction } from '../../models/transaction.model';
import { TransactionService } from '../../services/transaction.service';
import { AlertService } from 'src/app/alerts/services/alert.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.scss']
})
export class TransactionListComponent implements OnInit {
  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];

  isLoading = true;
  userEmail = localStorage.getItem('email')||''; // remplace dynamiquement si besoin

  searchTerm = '';
  filterType = '';
  filterStatus = '';
  minAmount: number | null = null;
  maxAmount: number | null = null;

  transactionTypes = ['Debit', 'Credit'];
  statuses = ['PENDING', 'APPROVED', 'REJECTED'];

  displayedColumns = ['transactionId', 'accountId', 'transactionAmount', 'transactionType', 'status', 'transactionDate'];

  constructor(private transactionService: TransactionService,private alertService: AlertService,private toaster: ToastrService) {}

  ngOnInit(): void {
    this.transactionService.getTransactionsByUserEmail(this.userEmail).subscribe({
      next: (data:any) => {
        this.transactions = data;
        this.filteredTransactions = data.sort((a: any, b: any) => new Date(b.transactionDate).getTime() - new Date(a.transactionDate).getTime());
        this.isLoading = false;
        const uniqueAccountIds = [...new Set(data.map((t: any) => t.accountId))];
        uniqueAccountIds.forEach((accountId: any) => {
          this.alertService.getAlertsByAccount(accountId).subscribe((alerts: any[]) => {
            alerts
              .filter(alert => alert.status === 'UNREAD')
              .forEach(alert => {
                this.toaster.warning(alert.message, `⚠ Alerte: ${alert.type}`);
                this.alertService.updateAlert(alert.alertId, 'READ').subscribe();
              });
          });
        });
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    this.filteredTransactions = this.transactions.filter(t => {
      const matchesSearch =
        t.transactionId.includes(this.searchTerm) ||
        t.accountId.includes(this.searchTerm);

      const matchesType = this.filterType ? t.transactionType === this.filterType : true;
      const matchesStatus = this.filterStatus ? t.status === this.filterStatus : true;
      const matchesMin = this.minAmount !== null ? t.transactionAmount >= this.minAmount : true;
      const matchesMax = this.maxAmount !== null ? t.transactionAmount <= this.maxAmount : true;

      return matchesSearch && matchesType && matchesStatus && matchesMin && matchesMax;
    });
  }
}
