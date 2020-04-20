import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CONFIG } from 'src/environments/config';

const FETCH_KEY_URL = `http://${CONFIG.host}:${CONFIG.port}/api/room/key`;

@Injectable({
  providedIn: 'root',
})
export class SocketService {
  private roomKey: string;
  private username: string;

  constructor(private http: HttpClient) {}

  /**
   * Fetch the key of a new room from backend for socket connection
   */
  fetchRoomKey(): void {
    console.log(FETCH_KEY_URL);
    let observable = this.http.get(FETCH_KEY_URL, { responseType: 'text' });
    observable.subscribe({
      next: (v) => {
        this.roomKey = v;
      },
      error: (e) => {
        console.log(e);
      },
      complete: () => {
        console.log(this.roomKey);
      },
    });
  }

  /**
   * Set room key for socket connection (if the key is known in advance)
   *
   * @param key
   */
  setRoomKey(key: string) {
    this.roomKey = key;
  }
}
