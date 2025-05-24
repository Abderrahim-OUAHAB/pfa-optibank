import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { RegisterComponent } from './register/register.component';
import { UsersComponent } from './users/users.component';
import { PendingComponent } from './pending/pending.component';
import { RejectedComponent } from './rejected/rejected.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {path: 'logout', component: LogoutComponent},
  {path:'users',component:UsersComponent},
  { path: 'pending', component: PendingComponent },
   { path: 'rejected', component: RejectedComponent },
 

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {}
