import { Component, ViewChild, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { AuthService } from '../../auth/services/auth.service';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  
  authState: {isAuthenticated: boolean, role: string} = {
    isAuthenticated: false,
    role: ''
  };
 status: string = '';
  constructor(private authService: AuthService,private toaster: ToastrService) {}

  ngOnInit() {
    this.authService.authStatus$.subscribe(state => {
      this.authState = state;
        this.authService.getUserByEmail(localStorage.getItem('email') || '').subscribe(user => {
      this.status = user.status;
    })
    });
  
  }

  logout() {
    const token = localStorage.getItem('token');
    if (token) {
      this.authService.logout(token).subscribe();
      this.toaster.success('Déconnexion reussie', 'Success');
      

    }
  }
}