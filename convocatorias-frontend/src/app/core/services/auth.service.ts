import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { LoginRequest, LoginResponse } from '../models/auth.model';
import { RolUsuario } from '../models/usuario.model';

interface UsuarioSesion {
  id: string;
  nombre: string;
  correo: string;
  rol: RolUsuario;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly STORAGE_KEY_TOKEN = 'convocatorias_token';
  private readonly STORAGE_KEY_USUARIO = 'convocatorias_usuario';

  // Signal reactivo: cualquier componente puede leer el usuario actual
  // y se actualiza automáticamente en toda la app al hacer login/logout.
  usuarioActual = signal<UsuarioSesion | null>(this.cargarUsuarioGuardado());

  constructor(private http: HttpClient) {}

  login(credenciales: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, credenciales)
      .pipe(
        tap(respuesta => this.guardarSesion(respuesta))
      );
  }

  logout(): void {
    localStorage.removeItem(this.STORAGE_KEY_TOKEN);
    localStorage.removeItem(this.STORAGE_KEY_USUARIO);
    this.usuarioActual.set(null);
  }

  obtenerToken(): string | null {
    return localStorage.getItem(this.STORAGE_KEY_TOKEN);
  }

  estaAutenticado(): boolean {
    return !!this.obtenerToken();
  }

  obtenerRol(): RolUsuario | null {
    return this.usuarioActual()?.rol ?? null;
  }

  tieneRol(...roles: RolUsuario[]): boolean {
    const rolActual = this.obtenerRol();
    return rolActual !== null && roles.includes(rolActual);
  }

  private guardarSesion(respuesta: LoginResponse): void {
    localStorage.setItem(this.STORAGE_KEY_TOKEN, respuesta.token);
    const usuario: UsuarioSesion = {
      id: respuesta.id,
      nombre: respuesta.nombre,
      correo: respuesta.correo,
      rol: respuesta.rol
    };
    localStorage.setItem(this.STORAGE_KEY_USUARIO, JSON.stringify(usuario));
    this.usuarioActual.set(usuario);
  }

  private cargarUsuarioGuardado(): UsuarioSesion | null {
    const data = localStorage.getItem(this.STORAGE_KEY_USUARIO);
    return data ? JSON.parse(data) : null;
  }
}
