import { Categoria } from './categoria.model';

export type EstadoConvocatoria = 'BORRADOR' | 'PUBLICADA' | 'CERRADA';

export interface Convocatoria {
  id: string;
  nombre: string;
  descripcion?: string;
  fechaInicio: string;
  fechaFin: string;
  cuposDisponibles: number;
  estado: EstadoConvocatoria;
  categorias?: Categoria[];
  fechaCreacion?: string;
}

export interface ConvocatoriaRequest {
  nombre: string;
  descripcion?: string;
  fechaInicio: string;
  fechaFin: string;
  cuposDisponibles: number;
  estado: EstadoConvocatoria;
  idsCategorias?: string[];
}
