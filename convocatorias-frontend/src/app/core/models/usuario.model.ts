export type RolUsuario = 'ADMINISTRADOR' | 'DOCENTE' | 'ESTUDIANTE';
export type EstadoUsuario = 'ACTIVO' | 'INACTIVO';

export interface Usuario {
  id: string;
  identificacion: string;
  nombre: string;
  correo: string;
  rol: RolUsuario;
  estado: EstadoUsuario;
  fechaCreacion?: string;
}

export interface UsuarioRequest {
  identificacion: string;
  nombre: string;
  correo: string;
  password: string;
  rol: RolUsuario;
  estado?: EstadoUsuario;
}
