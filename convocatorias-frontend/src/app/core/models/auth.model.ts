import { RolUsuario } from './usuario.model';

export interface LoginRequest {
  correo: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  id: string;
  nombre: string;
  correo: string;
  rol: RolUsuario;
}
