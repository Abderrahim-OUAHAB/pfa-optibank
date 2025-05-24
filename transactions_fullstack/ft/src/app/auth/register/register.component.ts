import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';  // Si vous souhaitez utiliser des notifications
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  success = '';
  error = '';
  isLoading: boolean = false;

  constructor(private fb: FormBuilder, private auth: AuthService, private toastr: ToastrService,private router: Router ) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      address: ['', Validators.required],
      city: ['', Validators.required],
      postalCode: ['', [Validators.required, Validators.pattern('^[0-9]{5}$')]],
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

    this.isLoading = true;
    const formValues = this.registerForm.value;
    const formData = {
      ...formValues,
      role: 'USER',
      status: 'PENDING'
    }
    this.auth.register(formData).subscribe({
      next: () => {
        this.success = 'Compte créé avec succès';
        this.toastr.success(this.success);
        this.registerForm.reset();
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      error: err => {
        this.error = err.error.message;
        this.toastr.error(this.error, "Erreur");
        console.log(err);
        this.isLoading = false;
      }
    });
  }
}
