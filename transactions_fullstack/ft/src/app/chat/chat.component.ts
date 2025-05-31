import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';

interface Message {
  role: 'user' | 'bot';
  content: string;
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy {
  messages: Message[] = [];
  input = '';
  search = '';
  isLoading = false;
  userLocation: { latitude: number; longitude: number } | null = null;
  locationError: string | null = null;

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.initDocs();
    window.addEventListener('beforeunload', this.resetConversationBeforeUnload);
  }

  ngOnDestroy(): void {
    window.removeEventListener('beforeunload', this.resetConversationBeforeUnload);
    this.resetConversation();
  }

  private resetConversationBeforeUnload = () => {
    this.resetConversation().catch(console.error);
  };

  private async initDocs(): Promise<void> {
    try {
      await this.http.post(`http://localhost:5000/init`, {}).toPromise();
      console.log('Docs chargés');
    } catch (err) {
      console.error('Erreur init:', err);
    }
  }

  async handleNewConversation(): Promise<void> {
    try {
      this.isLoading = true;
      this.messages = [];
      this.input = '';
      this.locationError = null;
      await this.resetConversation();
    } catch (error) {
      console.error('Erreur lors de la réinitialisation:', error);
      this.messages.push({
        role: 'bot',
        content: `Erreur lors de la réinitialisation: ${error instanceof Error ? error.message : String(error)}`
      });
    } finally {
      this.isLoading = false;
    }
  }

  private async resetConversation(): Promise<void> {
    try {
      await this.http.post(`http://localhost:5000/reset`, {}).toPromise();
    } catch (error) {
      console.error('Erreur reset:', error);
      throw error;
    }
  }

  formatMessageContent(content: string): string {
    if (!content) return '';
    return content
      .replace(/PROCÉDURE D'URGENCE :/g, '<strong class="urgent">PROCÉDURE D\'URGENCE :</strong>')
      .replace(/ÉTABLISSEMENT MÉDICAL PROCHE :/g, '<strong class="hospital">ÉTABLISSEMENT MÉDICAL PROCHE :</strong>')
      .replace(/(https?:\/\/[^\s]+)/g, '<a href="$1" target="_blank" rel="noopener noreferrer">$1</a>');
  }

  handleGetLocation(): void {
    if (this.userLocation) return;

    if (!navigator.geolocation) {
      this.messages.push({
        role: 'bot',
        content: 'La géolocalisation n\'est pas supportée par votre navigateur'
      });
      return;
    }

    this.isLoading = true;
    this.locationError = null;

    navigator.geolocation.getCurrentPosition(
      (position) => {
        this.userLocation = {
          latitude: position.coords.latitude,
          longitude: position.coords.longitude
        };
        this.isLoading = false;
      },
      (err) => {
        let errorMessage = 'Erreur de géolocalisation - ';
        switch (err.code) {
          case err.PERMISSION_DENIED:
            errorMessage += 'Vous avez refusé l\'accès à la localisation';
            break;
          case err.POSITION_UNAVAILABLE:
            errorMessage += 'Les informations de localisation ne sont pas disponibles';
            break;
          case err.TIMEOUT:
            errorMessage += 'La requête de localisation a expiré';
            break;
          default:
            errorMessage += 'Erreur inconnue';
        }
        this.locationError = errorMessage;
        this.messages.push({
          role: 'bot',
          content: errorMessage
        });
        this.isLoading = false;
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0
      }
    );
  }

  async handleSend(): Promise<void> {
    this.handleGetLocation();
    if (!this.input.trim()) return;

    const userMsg: Message = { role: 'user', content: this.input };
    this.messages = [...this.messages, userMsg];
    this.input = '';
    this.isLoading = true;

    try {
      const res = await this.http.post<any>(`http://localhost:5000/ask`, {
        message: userMsg.content,
        location: this.userLocation,
        email: localStorage.getItem('email')
      }).toPromise();

      if (res?.answer) {
        this.messages = [...this.messages, { role: 'bot', content: res.answer }];
      } else {
        throw new Error(res?.error || 'Format de réponse inattendu');
      }
    } catch (err: any) {
      console.error('Erreur API:', err);
      const errorContent = err.error?.details || err.error?.error || err.message || 'Erreur lors de la communication avec le serveur';
      this.messages = [...this.messages, { role: 'bot', content: `Erreur: ${errorContent}` }];
    } finally {
      this.isLoading = false;
    }
  }

  get filteredMessages(): Message[] {
    return this.messages.filter(msg => 
      msg.content.toLowerCase().includes(this.search.toLowerCase())
    );
  }
}