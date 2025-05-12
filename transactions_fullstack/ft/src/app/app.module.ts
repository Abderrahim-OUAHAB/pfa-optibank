import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './base/navbar/navbar.component';
import { FooterComponent } from './base/footer/footer.component';
@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
         timeOut: 5000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }
    )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
