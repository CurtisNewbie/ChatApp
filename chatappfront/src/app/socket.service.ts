import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CONFIG } from 'src/environments/config';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { Observable } from 'rxjs';

const FETCH_KEY_URL = `http://${CONFIG.host}:${CONFIG.port}/api/room/key`;

@Injectable({
  providedIn: 'root',
})
export class SocketService {
  private wsConn: WebSocketSubject<string> = null;

  constructor(private http: HttpClient) {}

  /**
   * Fetch the key of a new room from backend for websocket connection
   */
  fetchRoomKey(): Observable<string> {
    console.log(FETCH_KEY_URL);
    return this.http.get(FETCH_KEY_URL, { responseType: 'text' });
  }

  /**
   * Open websocket connection and return an Observable of string
   *
   * @returns Observable of string or NULL if either the username or the roomKey is null or empty
   */
  openWsConn(username: string, roomKey: string): Observable<string> {
    if (username && roomKey) {
      this.wsConn = webSocket(
        `ws://${CONFIG.host}:${CONFIG.port}/chat/room/${roomKey}/name/${username}`
      );
      return this.wsConn.asObservable();
    }
  }
}
