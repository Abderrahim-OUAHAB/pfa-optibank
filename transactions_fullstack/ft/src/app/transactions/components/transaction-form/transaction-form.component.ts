import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TransactionService } from '../../services/transaction.service';
import { TransactionType } from '../../models/transaction-type.enum';
import { MatSnackBar } from '@angular/material/snack-bar';

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
    private snackBar: MatSnackBar
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
      accountBalance: this.generateAccountBalance()
    };

    this.isLoading = true;
    this.transactionService.createTransaction(formData).subscribe({
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
}