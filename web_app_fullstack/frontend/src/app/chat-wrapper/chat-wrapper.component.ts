import { Component } from '@angular/core';

@Component({
  selector: 'app-chat-wrapper',
  template: `
    <div class="chat-overlay" *ngIf="showChat" (click)="toggleChat()"></div>
    <div class="chat-modal" [class.active]="showChat">
      <app-chat></app-chat>
    </div>
  `,
  styleUrls: ['./chat-wrapper.component.scss']
})
export class ChatWrapperComponent {
  showChat = false;

  toggleChat(): void {
    this.showChat = !this.showChat;
  }
}