export interface Categoria {
  id: string;
  nombre: string;
  descripcion?: string;
}

export interface CategoriaRequest {
  nombre: string;
  descripcion?: string;
}
