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
      password: ['', Validators.required],
       type: ['USER', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    const loginData = {
      ...this.loginForm.value,
      role: this.type 
    };

    this.auth.login(loginData).subscribe({
      next: res => {
        this.toastr.success('Connexion réussie', 'Success');
        this.auth.getUserByEmail(loginData.email).subscribe(user => {
          if(user.status=="PENDING"){
            this.router.navigate(['/pending']);

          }else if (user.status=="REJECTED"){
            this.router.navigate(['/rejected']);

          }else if(user.status=="SUSPENDED"){
            this.router.navigate(['/suspended']);
          }
            else{
                    this.router.navigate(['/home']);

          }
        })
      },
      error: err => {
        console.log(err);
        this.error = 'Email ou mot de passe incorrect';
      }
    });
  }

  getRole(event: any) {
    this.type = event.value;
    console.log(this.type);
    this.loginForm.patchValue({ type: event.value });
  }
}
