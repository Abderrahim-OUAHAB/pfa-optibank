import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { TransactionType } from '../../models/transaction-type.enum';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { AccountService } from 'src/app/accounts/service/account.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/services/auth.service';

@Component({
  selector: 'app-transaction-form',
  templateUrl: './transaction-form.component.html',
  styleUrls: ['./transaction-form.component.scss']
})
export class TransactionFormComponent implements OnInit {
  transactionForm!: FormGroup;
  isLoading = false;
  transactionTypes = Object.values(TransactionType);
  email:any;
  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private snackBar: MatSnackBar,
    private http: HttpClient,
    private accountScervice: AccountService,
    private toaster: ToastrService,
    private authservice: AuthService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.email=localStorage.getItem('email');

  }

  initForm(): void {
    this.transactionForm = this.fb.group({
      transactionAmount: ['', [Validators.required, Validators.min(0.01)]],
      transactionType: ['', Validators.required]
    });
  }

onSubmit(): void {
  if (this.transactionForm.invalid) return;
  this.accountScervice.findAccountsByCustomerId(this.email).subscribe(accounts => {
    const accountId = accounts.accountId;
    console.log(accounts);
    if (accounts.balance < this.transactionForm.value.transactionAmount &&
        this.transactionForm.value.transactionType === 'Debit') {
      this.toaster.error('Solde insuffisant', 'Error');
      return;
    }
    const balance = accounts.balance;
    this.authservice.getUserByEmail(this.email).subscribe(
      (response) => {
        const user = response;

        const formData = {
  ...this.transactionForm.value,
  accountId: accountId,
  transactionId: this.generateTransactionId(),
  userEmail: this.email || '',
  location: user.city,
  deviceId: user.cin,
  ipAddress: this.generateIpAddress(),
  merchantId: user.nationality,
  channel: this.generateChannel(),
  customerAge: this.calculateAge(user.birthDate),
  customerOccupation: user.profession,
  transactionDuration: this.generateRandomNumber(30, 300),
  loginAttempts: this.generateRandomNumber(1, 10),
  accountBalance: balance,
  previousTransactionDate: this.generatePreviousTransactionDate(),
  transactionDate: new Date()
};



        this.isLoading = true;

        this.transactionService.createTransaction({
          ...formData,
          userEmail: this.email || ''
        }).subscribe({
          next: () => {
            this.toaster.success('Transaction effectuée avec succès', 'Success');
            this.transactionForm.reset();
          },
          error: (err) => {
            console.log(err);
            this.toaster.error(err?.error?.message || 'Une erreur est survenue', 'Error');
          },
          complete: () => {
            this.isLoading = false;
          }
        });
      }
    );
  });
}


private calculateAge(birthDate: string): number {
  const birth = new Date(birthDate);
  const today = new Date();
  let age = today.getFullYear() - birth.getFullYear();
  const m = today.getMonth() - birth.getMonth();
  if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) {
    age--;
  }
  return age;
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

private generateCustomerOccupation(email:any): string {
    let occupation ="" ;
  
    this.authservice.getUserByEmail(email).subscribe(
              (response) => {
                occupation = response.profession;
                console.log(occupation)
                return occupation ;
              }
              
            )
return occupation;
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