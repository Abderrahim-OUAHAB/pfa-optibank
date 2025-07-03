import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { CustomersService } from 'src/app/customers/service/customers.service';
import { AccountService } from 'src/app/accounts/service/account.service';
import { AlertService } from 'src/app/alerts/services/alert.service';
import { CardService } from 'src/app/cards/service/card.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent {
users: any[] = []
  filteredUsers: any[] = []
  isLoading = true
  columns: string[] = ['email', 'name', 'status', 'actions']

  searchTerm = ''
  filterStatus = ''
  statuses = ['PENDING', 'APPROVED', 'REJECTED', 'SUSPENDED']

  constructor(
    private userService: AuthService,
    private toastr: ToastrService,
    private customerService: CustomersService,
   private accountService: AccountService,
   private  alertService: AlertService,
   private cardService: CardService
  ) {}

  ngOnInit() {
    this.loadUsers()
  }

  loadUsers() {
    this.isLoading = true
    this.userService.getAllUsers().subscribe(data => {
      this.users = data.filter((u: any) => u.role !== 'ADMIN')
      this.filteredUsers =  data.filter((u: any) => u.role !== 'ADMIN')
      this.isLoading = false
    })
  }

  applyFilters() {
    const term = this.searchTerm.trim().toLowerCase()
    const status = this.filterStatus

    this.filteredUsers = this.users.filter(u =>
      (!term ||
        u.email.toLowerCase().includes(term) ||
        u.firstName.toLowerCase().includes(term) ||
        u.lastName.toLowerCase().includes(term)) &&
      (!status || u.status === status)
    )
  }

  changeStatus(email: string, status: string) {
    this.userService.updateUserStatus(email, status).subscribe(() => {
      if (status === 'APPROVED') {
          this.userService.getUserByEmail(email).subscribe(user => {
            const customer = {
              customerId: user.email,
              firstName: user.firstName,
              lastName: user.lastName,
              email: user.email,
              phone: user.phone,
              address: user.address,
              birthDate: user.birthDate,
              kycVerified: true,
          }
           this.customerService.createCustomer(customer).subscribe(
            (response) => {
              const id= user.firstName[0]+user.lastName[0]+user.email[0]+Math.floor(Math.random() * 1000000).toString();
              const account={
                accountId:id,
                accountNumber:id,
                type: '-',
                balance: 0,
                status: 'APPROVED',
                openDate: new Date(),
                customerId: user.email
              }
            this.accountService.createAccount(account).subscribe(
              (response) => {
                const cardNumber = this.generateCardNumber();
const cvv = this.generateCVV();
const expiryDate = this.generateExpiryDate();

const card = {
  cardNumber,
  cvv,
  expiryDate,
  type: 'DEBIT',
  status: 'ACTIVE',
  accountId: id
};

this.cardService.createCard(card).subscribe(() => {
  this.toastr.success('Carte créée avec succès', 'Carte Bancaire');

       this.toastr.success('Client approuvé avec succès', 'Approuver Client')
                        this.toastr.success('Compte approuvé avec succès', 'Approuver Compte')
});

               
              }
            )
      
            }
           );
          }
              
        )
   
        this.toastr.success('Utilisateur approuvé avec succès', 'Approuver utilisateur')
      } else if (status === 'REJECTED') {
    // 1. D'abord trouver les comptes du client
    this.accountService.findAccountsByCustomerId(email).subscribe((data: any) => {
        // 2. Supprimer les cartes associées au compte
        this.cardService.deleteCardByAccountId(data.accountId).subscribe(() => {
            // 3. Supprimer les alertes associées au compte
            this.alertService.deleteAlert(data.accountId).subscribe(() => {
                // 4. Supprimer le compte lui-même
                this.accountService.deleteByCustomerId(email).subscribe(() => {
                    // 5. Enfin supprimer le client
                    this.customerService.deleteByEmail(email).subscribe(() => {
                        // Un seul message pour éviter la duplication
                        this.toastr.error('Client et comptes associés rejetés avec succès', 'Rejet terminé');
                    });
                });
            });
        });
    });
}
      this.loadUsers()
    })
  }

  getColor(status: string): string {
    if (status === 'APPROVED') return 'green'
    if (status === 'REJECTED') return 'red'
    if (status === 'SUSPENDED') return 'gray'
    return 'orange'
  }

  generateCardNumber(): string {
  const bin = '531101'; // Prefixe MasterCard fictif
  let number = bin + this.generateRandomDigits(10);
  return number;
}

generateCVV(): string {
  return this.generateRandomDigits(3);
}

generateExpiryDate(): string {
  const now = new Date();
  const future = new Date(now.getFullYear() + 4, now.getMonth());
  return future.toISOString().split('T')[0]; // format yyyy-MM-dd
}

generateRandomDigits(length: number): string {
  let result = '';
  for (let i = 0; i < length; i++) {
    result += Math.floor(Math.random() * 10).toString();
  }
  return result;
}

}
