import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import {
  Convocatoria,
  ConvocatoriaRequest,
  EstadoConvocatoria,
} from '../models/convocatoria.model';

@Injectable({ providedIn: 'root' })
export class ConvocatoriaService {
  private readonly baseUrl = `${environment.apiUrl}/convocatorias`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Convocatoria[]> {
    return this.http.get<Convocatoria[]>(this.baseUrl);
  }

  buscarPorId(id: string): Observable<Convocatoria> {
    return this.http.get<Convocatoria>(`${this.baseUrl}/${id}`);
  }

  crear(convocatoria: ConvocatoriaRequest): Observable<Convocatoria> {
    return this.http.post<Convocatoria>(this.baseUrl, convocatoria);
  }

  actualizar(id: string, convocatoria: ConvocatoriaRequest): Observable<Convocatoria> {
    return this.http.put<Convocatoria>(`${this.baseUrl}/${id}`, convocatoria);
  }

  eliminar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  cambiarEstado(id: string, estado: EstadoConvocatoria): Observable<Convocatoria> {
    return this.http.patch<Convocatoria>(`${this.baseUrl}/${id}/estado`, { estado });
  }
}
