import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  error = '';
  users:any[]=[];
  type:string="USER";
  constructor(private fb: FormBuilder, private auth: AuthService,private toastr: ToastrService,private router: Router ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.auth.login(this.loginForm.value).subscribe({
      next: res => {
        localStorage.setItem('token', res.token);

                this.router.navigate(['/home']);

        this.toastr.success('Connexion reussie', 'Success');

      },
      error: err => {
        console.log(err);
        this.error = 'Email ou mot de passe incorrect';
      }
    });
  }
  getRole(event:any){
  this.type=event.value;
}
}
