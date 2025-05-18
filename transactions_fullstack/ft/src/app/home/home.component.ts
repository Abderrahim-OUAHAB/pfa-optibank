import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
role: string | null ='';
ngOnInit(): void {
    this.role = localStorage.getItem('role');
  console.log(this.role); 
}
constructor() {
  this.role = localStorage.getItem('role');
  console.log(this.role);
}




}
