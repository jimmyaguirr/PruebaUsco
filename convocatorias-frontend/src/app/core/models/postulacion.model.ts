import { Usuario } from './usuario.model';
import { Convocatoria } from './convocatoria.model';

export type EstadoPostulacion = 'PENDIENTE' | 'APROBADA' | 'RECHAZADA';

export interface Postulacion {
  id: string;
  usuario: Usuario;
  convocatoria: Convocatoria;
  estado: EstadoPostulacion;
  fechaPostulacion: string;
  fechaResolucion?: string;
  observaciones?: string;
}

export interface PostulacionRequest {
  usuarioId: string;
  convocatoriaId: string;
}
