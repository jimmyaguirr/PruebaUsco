import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Postulacion, PostulacionRequest, EstadoPostulacion } from '../models/postulacion.model';

@Injectable({ providedIn: 'root' })
export class PostulacionService {
  private readonly baseUrl = `${environment.apiUrl}/postulaciones`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Postulacion[]> {
    return this.http.get<Postulacion[]>(this.baseUrl);
  }

  listarPorUsuario(usuarioId: string): Observable<Postulacion[]> {
    return this.http.get<Postulacion[]>(`${this.baseUrl}?usuarioId=${usuarioId}`);
  }

  listarPorConvocatoria(convocatoriaId: string): Observable<Postulacion[]> {
    return this.http.get<Postulacion[]>(`${this.baseUrl}?convocatoriaId=${convocatoriaId}`);
  }

  postular(request: PostulacionRequest): Observable<Postulacion> {
    return this.http.post<Postulacion>(this.baseUrl, request);
  }

  cambiarEstado(id: string, estado: EstadoPostulacion): Observable<Postulacion> {
    return this.http.put<Postulacion>(`${this.baseUrl}/${id}/estado`, { estado });
  }
}
