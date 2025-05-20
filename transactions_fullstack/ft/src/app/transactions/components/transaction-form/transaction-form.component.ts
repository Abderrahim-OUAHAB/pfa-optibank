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

 // Liste extraite directement depuis le CSV
private generateLocation(): string {
  const locations = [
    'Mesa', 'Raleigh', 'Atlanta', 'Oklahoma City', 'Columbus', 'Fresno',
    'Charlotte', 'Albuquerque', 'Dallas', 'Boston', 'Virginia Beach',
    'Denver', 'San Diego', 'Milwaukee', 'Las Vegas', 'Chicago',
    'Indianapolis', 'Seattle', 'Portland', 'Washington', 'Philadelphia',
    'New York', 'Memphis', 'Houston', 'Phoenix', 'Omaha', 'Sacramento',
    'Louisville', 'Kansas City', 'Tucson', 'Detroit', 'Fort Worth',
    'San Jose', 'Colorado Springs', 'Jacksonville', 'San Francisco',
    'El Paso', 'Austin', 'Los Angeles', 'Nashville', 'San Antonio',
    'Miami'
  ];
  return locations[Math.floor(Math.random() * locations.length)];
}

private generateDeviceId(): string {
  // Les DeviceID dans le CSV sont au format DXXXXX (ex: D000289, D000424)
  const suffix = Math.floor(Math.random() * 99999).toString().padStart(5, '0');
  return `D${suffix}`;
}

private generateIpAddress(): string {
  return Array.from({ length: 4 }, () => Math.floor(Math.random() * 256)).join('.');
}

private generateMerchantId(): string {
  // Les MerchantID dans le CSV sont au format MXXX (ex: M093, M060)
  const suffix = Math.floor(Math.random() * 999).toString().padStart(3, '0');
  return `M${suffix}`;
}

private generateChannel(): string {
  // Valeurs trouvées dans le CSV : Online, ATM, Branch, Terminal
  const channels = ['Online', 'ATM', 'Branch'];
  return channels[Math.floor(Math.random() * channels.length)];
}

private generateCustomerAge(): number {
  // Dans le CSV, les âges vont de 18 à ~80
  return Math.floor(Math.random() * (80 - 18 + 1)) + 18;
}

private generateCustomerOccupation(): string {
  // Professions extraites du CSV : Engineer, Doctor, Retired, Student
  const jobs = ['Engineer', 'Doctor', 'Retired', 'Student'];
  return jobs[Math.floor(Math.random() * jobs.length)];
}

private generateRandomNumber(min: number, max: number): number {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

private generateAccountBalance(): number {
  // Comme dans le CSV, plages typiques entre 0 et 15 000 environ
  return +(Math.random() * 15000).toFixed(2);
}

private generateTransactionId(): string {
  // Le format est TX suivi de 6 chiffres (ex: TX001474)
  const suffix = Math.floor(Math.random() * 999999).toString().padStart(6, '0');
  return `TX${suffix}`;
}

private generatePreviousTransactionDate(): Date {
  const now = new Date();
  const daysAgo = Math.floor(Math.random() * 30); // Transactions récentes
  const previousDate = new Date(now.getTime() - daysAgo * 24 * 60 * 60 * 1000);
  return previousDate;
}
}