export interface ConvocatoriaPorCategoria {
  categoriaId: string;
  categoriaNombre: string;
  totalConvocatorias: number;
}

export interface PostulacionesPorConvocatoria {
  convocatoriaId: string;
  convocatoriaNombre: string;
  totalPostulaciones: number;
}

export interface ResultadoPostulaciones {
  estado: string;
  total: number;
}
