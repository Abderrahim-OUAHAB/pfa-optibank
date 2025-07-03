import { Component, OnInit } from '@angular/core';
import { ExchangeRate } from './models/exchange-rate.model';
import { ExchangeRateService } from './service/exchange-rates.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-exchange-rates',
  templateUrl: './exchange-rates.component.html',
  styleUrls: ['./exchange-rates.component.scss']
})
export class ExchangeRatesComponent implements OnInit {
  rates: ExchangeRate[] = [];
  newRate: ExchangeRate = {
    rateId: '',
    currencyFrom: 'USD',
    currencyTo: 'EUR',
    rate: 0
  };
  currencies = ['USD', 'EUR', 'GBP', 'JPY', 'CAD','MAD'];
  isEditing = false;
   role:any=localStorage.getItem('role');
  displayedColumns: string[] =this.role==='ADMIN' ? ['from', 'to', 'rate', 'actions'] : ['from', 'to', 'rate'];
 
  constructor(private exchangeRateService: ExchangeRateService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadRates();
  }

  loadRates(): void {
    this.exchangeRateService.getAllRates().subscribe(data => {
      this.rates = data;
    });
  }

  saveRate(): void {
    if (this.isEditing) {
      this.exchangeRateService.updateRate(this.newRate).subscribe(() => {
        this.toastr.success('Taux de change mis à jour avec succès', 'Taux de change');
        this.resetForm();
        this.loadRates();
      });
    } else {
      this.newRate.rateId = this.generateId();
      this.exchangeRateService.createRate(this.newRate).subscribe(() => {
        this.toastr.success('Taux de change ajouté avec succès', 'Taux de change');
        this.resetForm();
        this.loadRates();
      });
    }
  }

  editRate(rate: ExchangeRate): void {
    this.newRate = { ...rate };
    this.isEditing = true;
  }

  deleteRate(rateId: string): void {
    this.exchangeRateService.deleteRate(rateId).subscribe(() => {
      this.toastr.error('Taux de change supprimé avec succès', 'Taux de change');
      this.loadRates();
    });
  }

  private generateId(): string {
    return Math.random().toString(36).substring(2, 15);
  }

   resetForm(): void {
    this.newRate = {
      rateId: '',
      currencyFrom: 'USD',
      currencyTo: 'EUR',
      rate: 0
    };
    this.isEditing = false;
  }
}