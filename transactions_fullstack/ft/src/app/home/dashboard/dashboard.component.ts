import { Component, OnInit } from '@angular/core';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { AccountService } from '../../accounts/service/account.service';
import { TransactionService } from '../../transactions/services/transaction.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  transactions: any[] = [];
  fraudCount = 0;
  legitimateCount = 0;
  totalBalance = 0;
  accountCount = 0;

  // Configuration des graphiques
  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    scales: {
      y: {
        beginAtZero: true
      }
    },
    plugins: { 
      legend: { 
        display: true,
        position: 'top' 
      } 
    }
  };

  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { 
      legend: { 
        position: 'bottom',
        labels: {
          font: {
            size: 14
          }
        }
      },
      tooltip: {
        bodyFont: {
          size: 14
        },
        callbacks: {
          label: (context) => {
            const label:any = context.label || '';
            const value:any = context.raw || 0;
            const total :any= context.dataset.data.reduce((a: any, b: any) => a + b, 0);
            const percentage = Math.round((value / total) * 100);
            return `${label}: ${value} (${percentage}%)`;
          }
        }
      }
    }
  };

  public barChartType: ChartType = 'bar';
  public pieChartType: ChartType = 'pie';

  public barChartData: ChartData<'bar'> = {
    labels: [],
    datasets: []
  };

  public pieChartData: ChartData<'pie', number[], string> = {
    labels: ['Frauduleuses', 'Légitimes'],
    datasets: [{
      data: [0, 0],
      backgroundColor: ['#FF6384', '#36A2EB'],
      hoverBackgroundColor: ['#FF6384', '#36A2EB']
    }]
  };

  constructor(
    private transactionService: TransactionService,
    private accountService: AccountService
  ) {}

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.transactionService.getAllTransactions().subscribe({
      next: (transactions) => {
        this.transactions = transactions;
        this.analyzeTransactions(transactions);
      },
      error: (err) => console.error('Error loading transactions', err)
    });

    this.accountService.getAllAccounts().subscribe({
      next: (accounts) => {
        this.accountCount = accounts.length;
        this.totalBalance = accounts.reduce((acc:any, a:any) => acc + a.balance, 0);
      },
      error: (err) => console.error('Error loading accounts', err)
    });
  }

  analyzeTransactions(transactions: any[]) {
    this.fraudCount = transactions.filter(t => t.status === 'REJECTED').length;
    this.legitimateCount = transactions.filter(t => t.status === 'APPROVED').length;

    // Mise à jour immuable du pie chart
    this.pieChartData = {
      ...this.pieChartData,
      datasets: [{
        ...this.pieChartData.datasets[0],
        data: [this.fraudCount, this.legitimateCount]
      }]
    };

    // Préparation des données pour le bar chart
    const typeGroups = transactions.reduce((acc: any, t: any) => {
      const type = t.transactionType;
      acc[type] = (acc[type] || 0) + 1;
      return acc;
    }, {});

    this.barChartData = {
      labels: Object.keys(typeGroups),
      datasets: [{
        label: 'Transactions par type',
        data: Object.values(typeGroups),
        backgroundColor: 'rgba(54, 162, 235, 0.7)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1
      }]
    };
  }
}