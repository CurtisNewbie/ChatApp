import { Component, OnInit } from '@angular/core';
import { SocketService } from '../socket.service';
import { Observable } from 'rxjs';
import { WebSocketSubject } from 'rxjs/webSocket';

@Component({
  selector: 'app-chat-room',
  templateUrl: './chat-room.component.html',
  styleUrls: ['./chat-room.component.css'],
})
export class ChatRoomComponent implements OnInit {
  private wsObs: WebSocketSubject<string> = null;
  username: string;
  roomKey: string;
  chatMsgs: string = '';
  currMsg: string = '';

  constructor(private socket: SocketService) {}

  ngOnInit(): void {}

  /**
   * Ask the backend to create a new chat room and fetches the key to this room
   */
  createRoom() {
    this.socket.fetchRoomKey().subscribe({
      next: (v) => {
        this.roomKey = v;
      },
      error: (e) => {
        console.log(e);
      },
    });
  }

  /**
   * Connect to the chat room
   */
  connectRoom() {
    this.wsObs = this.socket.openWsConn(this.username, this.roomKey);
    if (this.wsObs != null) {
      console.log('Connected to Server');
      let subscription = this.wsObs.subscribe({
        next: (msg: string) => {
          this.chatMsgs += msg + '\n';
          console.log(msg);
        },
        error: (err: any) => {
          console.log(err);
        },
        complete: () => {
          alert('Connection is closed');
          subscription.unsubscribe();
          this.wsObs = null;
        },
      });
    }
  }

  /**
   * Send a message
   */
  sendMsg() {
    if (this.wsObs != null) {
      this.wsObs.next(this.currMsg);
    } else {
      alert("You haven't connected to any room, try create or connect one");
    }
  }
}
