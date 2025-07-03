import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../auth/services/auth.service';
import { AccountService } from './service/account.service';
import { CustomersService } from '../customers/service/customers.service';
import {MatFormFieldControl} from '@angular/material/form-field';
import { CardService } from '../cards/service/card.service';
@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent {
 userData: any
  isAdmin = false
  isEditing = false
  balance = 0
  cardData: any
  constructor(private accountService: AccountService, private auth: AuthService, 
    private toastr: ToastrService,private cardService: CardService,
  private customerService: CustomersService) {}

  ngOnInit() {
    const email = localStorage.getItem('email') || '';
    this.accountService.findAccountsByCustomerId(email).subscribe(data => {
            this.userData = data
          this.balance = data.balance
      this.customerService.findByCustomerId(email).subscribe(customer => {
        this.userData = {...this.userData, ...customer}
      })
           this.cardService.getCardByAccountId(data.accountId).subscribe(card => {
        this.cardData = card
      })
      this.isAdmin = localStorage.getItem('role') === 'ADMIN'
      console.log(this.userData)
    })
  }

  saveCustomerInfo() {
    this.customerService.updateCustomer(this.userData.email, {
      firstName: this.userData.firstName,
      lastName: this.userData.lastName,
      phone: this.userData.phone,
      birthDate: this.userData.birthDate,
      address: this.userData.address
    }).subscribe(() => {
      this.toastr.success('Infos mises à jour')
      this.isEditing = false
    })
  }

  updateBalance() {
    this.accountService.updateBalance(this.userData.accountId, this.balance).subscribe(() => {
      this.toastr.success('Solde mis à jour')
    })
  }
  
  copyToClipboard(value: string) {
  navigator.clipboard.writeText(value).then(() => {
    this.toastr.success('Identifiant copié');
  })
}

}
