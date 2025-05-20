import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { TransactionType } from '../../models/transaction-type.enum';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-transaction-form',
  templateUrl: './transaction-form.component.html',
  styleUrls: ['./transaction-form.component.scss']
})
export class TransactionFormComponent implements OnInit {
  transactionForm!: FormGroup;
  isLoading = false;
  transactionTypes = Object.values(TransactionType);
  email:string|null="";
  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private snackBar: MatSnackBar,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.email=localStorage.getItem('email');

  }

  initForm(): void {
    this.transactionForm = this.fb.group({
      accountId: ['', Validators.required],
      transactionAmount: ['', [Validators.required, Validators.min(0.01)]],
      transactionType: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.transactionForm.invalid) return;

    // Génération des champs facultatifs
    const formData = {
      ...this.transactionForm.value,
      transactionId: this.generateTransactionId(),
      userEmail: this.email||'',
      location: this.generateLocation(),
      deviceId: this.generateDeviceId(),
      ipAddress: this.generateIpAddress(),
      merchantId: this.generateMerchantId(),
      channel: this.generateChannel(),
      customerAge: this.generateCustomerAge(),
      customerOccupation: this.generateCustomerOccupation(),
      transactionDuration: this.generateRandomNumber(30, 300), // en secondes
      loginAttempts: this.generateRandomNumber(0, 5),
      accountBalance: this.generateAccountBalance(),
      previousTransactionDate: this.generatePreviousTransactionDate(),
      transactionDate: new Date()
    };

    this.isLoading = true;

this.saveToCsv(formData).then(() => {
      // 2. Envoi au backend Spring
      this.transactionService.createTransaction({
        ...formData,
        userEmail: this.email || ''
      }).subscribe({
        next: () => {
          this.snackBar.open('Transaction effectuée avec succès', 'Fermer', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
          this.transactionForm.reset();
        },
        error: (err) => {
          this.snackBar.open(`Erreur lors de la transaction: ${err.message}`, 'Fermer', {
            duration: 5000,
            panelClass: ['error-snackbar']
          });
          console.error(err);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }).catch((error: { message: any; }) => {
      this.isLoading = false;
      this.snackBar.open(`Erreur lors de la sauvegarde: ${error.message}`, 'Fermer', {
        duration: 5000,
        panelClass: ['error-snackbar']
      });
    });
  }
private saveToCsv(transaction: any): Promise<void> {
    return new Promise((resolve, reject) => {
      // Formatage des données pour correspondre au CSV
      const csvData = {
        TransactionID: transaction.transactionId,
        AccountID: transaction.accountId,
        TransactionAmount: transaction.transactionAmount,
        TransactionDate: transaction.transactionDate,
        TransactionType: transaction.transactionType,
        Location: transaction.location,
        DeviceID: transaction.deviceId,
        'IP Address': transaction.ipAddress,
        MerchantID: transaction.merchantId,
        AccountBalance: transaction.accountBalance,
        PreviousTransactionDate: transaction.previousTransactionDate,
        Channel: transaction.channel,
        CustomerAge: transaction.customerAge,
        CustomerOccupation: transaction.customerOccupation,
        TransactionDuration: transaction.transactionDuration,
        LoginAttempts: transaction.loginAttempts
      };

      // Appel à une API pour sauvegarder dans le CSV
      this.http.post('http://localhost:5000/save-to-csv', csvData).subscribe({
        next: () => resolve(),
        error: (err: any) => reject(err)
      });
    });
  }

  // Fonctions de génération aléatoire
  private generateLocation(): string {
    const cities = ['Paris', 'Lyon', 'Marseille', 'Bordeaux', 'Nice'];
    return cities[Math.floor(Math.random() * cities.length)];
  }

  private generateDeviceId(): string {
    return `DEV-${Math.floor(1000 + Math.random() * 9000)}`;
  }

  private generateIpAddress(): string {
    return Array.from({ length: 4 }, () => Math.floor(Math.random() * 256)).join('.');
  }

  private generateMerchantId(): string {
    return `MCH-${Math.random().toString(36).substring(2, 8).toUpperCase()}`;
  }

  private generateChannel(): string {
    const channels = ['Mobile', 'Web', 'Terminal', 'ATM'];
    return channels[Math.floor(Math.random() * channels.length)];
  }

  private generateCustomerAge(): number {
    return Math.floor(Math.random() * (70 - 18 + 1)) + 18;
  }

  private generateCustomerOccupation(): string {
    const jobs = ['Employé', 'Cadre', 'Indépendant', 'Étudiant', 'Retraité'];
    return jobs[Math.floor(Math.random() * jobs.length)];
  }

  private generateRandomNumber(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  private generateAccountBalance(): number {
    return +(Math.random() * 100000).toFixed(2); // Entre 0 et 100 000
  }

    private generateTransactionId(): string {
    return `TX${Math.floor(100000 + Math.random() * 900000)}`;
  }
private generatePreviousTransactionDate(): Date {
    const now = new Date();
    const daysAgo = Math.floor(Math.random() * 30);
    const previousDate = new Date(now.getTime() - daysAgo * 24 * 60 * 60 * 1000);
    return previousDate;
  }
}