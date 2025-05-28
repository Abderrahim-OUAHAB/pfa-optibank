import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { debounceTime, switchMap, startWith, map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { AccountService } from '../../accounts/service/account.service';
import { AuthService } from '../../auth/services/auth.service';
import { TransactionService } from '../../transactions/services/transaction.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  searchControl = new FormControl();
  customers: any[] = [];
  filteredCustomers: Observable<any[]> | undefined;
  selectedCustomer: any = null;
  isLoading = false;

  // Data
  totalTransactions = 0;
  fraudTransactions = 0;
  validTransactions = 0;
  totalCustomers = 0;

  // Charts
  pieChartData: ChartData<'pie'> = {
    labels: ['Fraudes', 'Valides', 'En attente'],
    datasets: [{
      data: [0, 0, 0],
      backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
    }]
  };

  barChartData: ChartData<'bar'> = {
    labels: [],
    datasets: [{
      label: 'Transactions',
      data: [],
      backgroundColor: 'rgba(54, 162, 235, 0.7)'
    }]
  };

  chartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { position: 'bottom' },
      tooltip: {
        callbacks: {
          label: (context) => {
            const label:any = context.label || '';
            const value :any= context.raw as number;
            const total:any = context.dataset.data.reduce((a: any, b: any) => a + b, 0);
            const percentage = total ? Math.round((value / total) * 100) : 0;
            return `${label}: ${value} (${percentage}%)`;
          }
        }
      }
    }
  };

  constructor(
    private authService: AuthService,
    private transactionService: TransactionService,
    private accountService: AccountService
  ) {}

  ngOnInit() {
    this.loadGlobalData();
    this.setupCustomerSearch();
  }

  displayCustomer(customer: any): string {
    return customer ? `${customer.email}` : '';
  }

  private setupCustomerSearch() {
    this.filteredCustomers = this.searchControl.valueChanges.pipe(
      startWith(''),
      debounceTime(300),
      switchMap(value => this.filterCustomers(value || ''))
    );
  }

  private filterCustomers(value: string): Observable<any[]> {
    return this.authService.getAllUsers().pipe(
      map(users => {
        const filterValue = value.toLowerCase();
        return users.filter((u: { role: string; email: string; firstName: string; lastName: string; }) => 
          u.role === 'USER' && 
          (u.email.toLowerCase().includes(filterValue) || 
          u.firstName?.toLowerCase().includes(filterValue) || 
          u.lastName?.toLowerCase().includes(filterValue))
        );
      })
    );
  }

  selectCustomer(customer: any) {
    this.selectedCustomer = customer;
    this.loadCustomerData(customer.email);
  }

  clearSelection() {
    this.selectedCustomer = null;
    this.searchControl.reset();
    this.loadGlobalData();
  }

  loadGlobalData() {
    this.isLoading = true;
    this.authService.getAllUsers().subscribe({
      next: users => {
        this.customers = users.filter((u: { role: string; }) => u.role === 'USER');
        this.totalCustomers = this.customers.length;
        
        this.transactionService.getAllTransactions().subscribe({
          next: transactions => {
            this.updateChartData(transactions);
            this.isLoading = false;
          },
          error: () => this.isLoading = false
        });
      },
      error: () => this.isLoading = false
    });
  }

  loadCustomerData(email: string) {
    this.isLoading = true;
    this.transactionService.getTransactionsByUserEmail(email).subscribe({
      next: transactions => {
        this.updateChartData(transactions);
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  private updateChartData(transactions: any[]) {
    this.fraudTransactions = transactions.filter(t => t.status === 'REJECTED').length;
    this.validTransactions = transactions.filter(t => t.status === 'APPROVED').length;
    const pendingCount = transactions.filter(t => !['APPROVED', 'REJECTED'].includes(t.status)).length;
    this.totalTransactions = this.fraudTransactions + this.validTransactions + pendingCount;

    // Update pie chart
    this.pieChartData = {
      ...this.pieChartData,
      datasets: [{
        ...this.pieChartData.datasets[0],
        data: [this.fraudTransactions, this.validTransactions, pendingCount]
      }]
    };

    // Update bar chart
    const transactionsByType = transactions.reduce((acc, t) => {
      const type = t.transactionType;
      acc[type] = (acc[type] || 0) + 1;
      return acc;
    }, {} as Record<string, number>);

    this.barChartData = {
      labels: Object.keys(transactionsByType),
      datasets: [{
        label: this.selectedCustomer ? 'Transactions client' : 'Transactions globales',
        data: Object.values(transactionsByType),
        backgroundColor: this.selectedCustomer ? 'rgba(75, 192, 192, 0.7)' : 'rgba(54, 162, 235, 0.7)'
      }]
    };
  }
}