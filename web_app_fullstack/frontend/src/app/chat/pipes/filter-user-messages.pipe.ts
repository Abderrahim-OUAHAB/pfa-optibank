import { Pipe, PipeTransform } from '@angular/core';
import { Message } from '../models/message.model';
@Pipe({
  name: 'filterUserMessages'
})
export class FilterUserMessagesPipe implements PipeTransform {
  transform(messages: Message[]): Message[] {
    return messages.filter(msg => msg.role === 'user');
  }
}