import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-logout',
  template: `
    <a (click)="onLogout()" >Déconnexion</a>
  `
})
export class LogoutComponent {
  constructor(private authService: AuthService,private router:Router,private toastr: ToastrService) {}

  onLogout() {
    // Récupérer le token depuis le stockage local/session
    const token = localStorage.getItem('token');
    
    if (token) {
      this.authService.logout(token).subscribe({
        next: (response) => {
          console.log('Déconnexion réussie', response);
          // Supprimer le token du stockage local
          localStorage.removeItem('token');
          // Rediriger vers la page de login
          this.toastr.success('Déconnexion reussie', 'Success');
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error('Erreur lors de la déconnexion', err);
        }
      });
    }
  }
}