import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/services/auth.service';
import { CustomersService } from 'src/app/customers/service/customers.service';
import { AccountService } from '../../accounts/service/account.service';

@Component({
  selector: 'app-detail-compte',
  templateUrl: './detail-compte.component.html',
  styleUrls: ['./detail-compte.component.scss']
})
export class DetailCompteComponent implements OnInit {
  userData: any = {};
  isAdmin = false;
  isEditing = false;
  balance = 0;
  isLoading = true;
  errorMessage = '';

  constructor(
    private accountService: AccountService,
    private auth: AuthService,
    private toastr: ToastrService,
    private customerService: CustomersService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const email = params.get('email');
      if (!email) {
        this.errorMessage = 'No email provided in URL';
        this.isLoading = false;
        return;
      }

      this.loadUserData(email);
    });

    this.checkAdminRole();
  }

  private loadUserData(email: string): void {
    this.accountService.findAccountsByCustomerId(email).subscribe({
      next: (accountData) => {
        this.userData = accountData;
        this.balance = accountData.balance || 0;
        
        this.customerService.findByCustomerId(email).subscribe({
          next: (customerData) => {
            this.userData = { ...this.userData, ...customerData };
            this.isLoading = false;
          },
          error: (err) => {
            this.handleError('Failed to load customer details', err);
          }
        });
      },
      error: (err) => {
        this.handleError('Failed to load account details', err);
      }
    });
  }

  private checkAdminRole(): void {
    this.isAdmin = localStorage.getItem('role') === 'ADMIN';
  }

  saveCustomerInfo(): void {
    if (!this.userData?.email) {
      this.toastr.error('No user data available');
      return;
    }

    const updateData = {
      firstName: this.userData.firstName,
      lastName: this.userData.lastName,
      phone: this.userData.phone,
      birthDate: this.userData.birthDate,
      address: this.userData.address
    };

    this.customerService.updateCustomer(this.userData.email, updateData).subscribe({
      next: () => {
        this.toastr.success('Information updated successfully');
        this.isEditing = false;
      },
      error: (err) => {
        this.handleError('Failed to update customer information', err);
      }
    });
  }

  updateBalance(): void {
    if (!this.userData?.accountId) {
      this.toastr.error('No account ID available');
      return;
    }

    this.accountService.updateBalance(this.userData.accountId, this.balance).subscribe({
      next: () => {
        this.toastr.success('Balance updated successfully');
      },
      error: (err) => {
        this.handleError('Failed to update balance', err);
      }
    });
  }

  copyToClipboard(value: string): void {
    if (!value) {
      this.toastr.warning('No value to copy');
      return;
    }

    navigator.clipboard.writeText(value).then(
      () => this.toastr.success('Copied to clipboard'),
      () => this.toastr.error('Failed to copy to clipboard')
    );
  }

  private handleError(message: string, error: any): void {
    console.error(message, error);
    this.errorMessage = message;
    this.isLoading = false;
    this.toastr.error(message);
  }
}