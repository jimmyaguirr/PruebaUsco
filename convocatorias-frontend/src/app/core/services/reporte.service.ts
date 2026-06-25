import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import {
  ConvocatoriaPorCategoria,
  PostulacionesPorConvocatoria,
  ResultadoPostulaciones,
} from '../models/reportes.model';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private readonly baseUrl = `${environment.apiUrl}/reportes`;

  constructor(private http: HttpClient) {}

  convocatoriasPorCategoria(): Observable<ConvocatoriaPorCategoria[]> {
    return this.http.get<ConvocatoriaPorCategoria[]>(`${this.baseUrl}/convocatorias-categoria`);
  }

  postulacionesPorConvocatoria(): Observable<PostulacionesPorConvocatoria[]> {
    return this.http.get<PostulacionesPorConvocatoria[]>(
      `${this.baseUrl}/postulaciones-convocatoria`,
    );
  }

  resultadoPostulaciones(): Observable<ResultadoPostulaciones[]> {
    return this.http.get<ResultadoPostulaciones[]>(`${this.baseUrl}/resultado-postulaciones`);
  }
}
