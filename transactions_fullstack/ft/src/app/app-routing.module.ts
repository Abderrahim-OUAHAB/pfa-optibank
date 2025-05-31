import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule) },
  { path: 'home', loadChildren: () => import('./home/module/home.module').then(m => m.HomeModule) },
  { path: 'transactions', loadChildren: () => import('./transactions/module/transactions.module').then(m => m.TransactionsModule) },
  { path: 'accounts', loadChildren: () => import('./accounts/module/account.module').then(m => m.AccountModule) },
  { path: 'exchange-rates', loadChildren: () => import('./exchange-rates/modules/exchange-rates.module').then(m => m.ExchangeRatesModule) },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
