import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { importProvidersFrom, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatRadioModule } from '@angular/material/radio';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgChartsModule } from 'ng2-charts';
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LogoutComponent } from './auth/logout/logout.component';
import { FooterComponent } from './base/footer/footer.component';
import { NavbarComponent } from './base/navbar/navbar.component';
import { ChatWrapperComponent } from './chat-wrapper/chat-wrapper.component';
import { ChatComponent } from './chat/chat.component';
import { FilterUserMessagesPipe } from './chat/pipes/filter-user-messages.pipe';
import { TruncatePipe } from './chat/pipes/truncate.pipe';
import { CustomersComponent } from './customers/customers.component';
import { HomeComponent } from './home/home.component';
import { AlertsComponent } from './alerts/alerts.component';
import { CardsComponent } from './cards/cards.component';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    NavbarComponent,
    LogoutComponent,
    HomeComponent,
    ChatComponent,
    FilterUserMessagesPipe,
    TruncatePipe,
    ChatWrapperComponent,
    CustomersComponent,
    AlertsComponent,
    CardsComponent,
    
  ],
  imports: [
    BrowserModule,NgChartsModule,
    AppRoutingModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    BrowserAnimationsModule,
    MatToolbarModule,
        CommonModule,
FormsModule,
    MatRadioModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,MatSnackBarModule,
    MatListModule,
    MatMenuModule,
    ToastrModule.forRoot({
          timeOut: 3000,
          positionClass: 'toast-top-right',
          progressBar: true,
          progressAnimation: 'increasing',
          closeButton: true
        }),
  ],
  providers: [ importProvidersFrom(ToastrModule.forRoot())],
  bootstrap: [AppComponent]
})
export class AppModule { }
