import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  registerForm: FormGroup;
  success = '';
  error = '';

  constructor(private fb: FormBuilder, private auth: AuthService) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      postalCode: ['', Validators.required],
      country: ['', Validators.required],
      cin: ['', Validators.required],
      nationality: ['', Validators.required],
      profession: ['', Validators.required],
      monthlyIncome: [0, Validators.required],
      birthDate: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) return;
    console.log(this.registerForm.value);

    this.auth.register(this.registerForm.value).subscribe({
     
      next: () => {
        this.success = 'Compte créé';
        this.registerForm.reset();
      },
      error: err => {
        this.error = err.error.message;
        console.log(err);
      }
    });
  }
}
