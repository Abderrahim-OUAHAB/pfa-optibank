import { Component, ViewChild, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/services/auth.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  isAuthenticated: boolean = false;

  constructor(private authService: AuthService,private toastr: ToastrService) {}

  ngOnInit() {
    this.authService.authStatus$.subscribe((authenticated: boolean) => {
      this.isAuthenticated = authenticated;
    });
  }

  logout() {
    const token = this.authService.getToken();
    if (token) {
      this.authService.logout(token).subscribe();
      this.toastr.success('Déconnexion reussie', 'Success');
    }
  }
}