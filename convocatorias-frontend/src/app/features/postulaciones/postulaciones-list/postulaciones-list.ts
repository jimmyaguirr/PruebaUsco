import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';

import { PostulacionService } from '../../../core/services/postulacion.service';
import { ConvocatoriaService } from '../../../core/services/convocatoria.service';
import { Postulacion, EstadoPostulacion } from '../../../core/models/postulacion.model';
import { Convocatoria } from '../../../core/models/convocatoria.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-postulaciones-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatSelectModule,
    MatFormFieldModule,
    FormsModule,
  ],
  templateUrl: './postulaciones-list.html',
  styleUrl: './postulaciones-list.css',
})
export class PostulacionesListComponent implements OnInit {
  postulaciones = signal<Postulacion[]>([]);
  convocatoriasPublicadas = signal<Convocatoria[]>([]);
  convocatoriaSeleccionada: string | null = null;

  columnasAdmin = ['estudiante', 'convocatoria', 'fechaPostulacion', 'estado', 'acciones'];
  columnasEstudiante = ['convocatoria', 'fechaPostulacion', 'estado'];

  constructor(
    private postulacionService: PostulacionService,
    private convocatoriaService: ConvocatoriaService,
    private snackBar: MatSnackBar,
    public authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.cargarPostulaciones();

    if (this.authService.tieneRol('ESTUDIANTE')) {
      this.cargarConvocatoriasDisponibles();
    }
  }

  get columnas(): string[] {
    const puedeGestionar =
      this.authService.tieneRol('ADMINISTRADOR') || this.authService.tieneRol('DOCENTE');
    return puedeGestionar ? this.columnasAdmin : this.columnasEstudiante;
  }

  cargarPostulaciones(): void {
    const usuarioId = this.authService.usuarioActual()?.id;
    const puedeGestionar =
      this.authService.tieneRol('ADMINISTRADOR') || this.authService.tieneRol('DOCENTE');

    const peticion = puedeGestionar
      ? this.postulacionService.listar()
      : this.postulacionService.listarPorUsuario(usuarioId!);

    peticion.subscribe({
      next: (data) => this.postulaciones.set(data),
      error: () =>
        this.snackBar.open('Error al cargar postulaciones', 'Cerrar', { duration: 3000 }),
    });
  }

  cargarConvocatoriasDisponibles(): void {
    this.convocatoriaService.listar().subscribe({
      next: (data) => {
        const publicadas = data.filter((c) => c.estado === 'PUBLICADA');
        this.convocatoriasPublicadas.set(publicadas);
      },
      error: () =>
        this.snackBar.open('Error al cargar convocatorias', 'Cerrar', { duration: 3000 }),
    });
  }

  postularse(): void {
    if (!this.convocatoriaSeleccionada) {
      this.snackBar.open('Selecciona una convocatoria primero', 'Cerrar', { duration: 3000 });
      return;
    }

    const usuarioId = this.authService.usuarioActual()?.id;

    this.postulacionService
      .postular({
        usuarioId: usuarioId!,
        convocatoriaId: this.convocatoriaSeleccionada,
      })
      .subscribe({
        next: () => {
          this.snackBar.open('Postulación enviada con éxito', 'Cerrar', { duration: 3000 });
          this.convocatoriaSeleccionada = null;
          this.cargarPostulaciones();
        },
        error: (error) => {
          const mensaje = error?.error?.mensaje ?? 'Error al enviar la postulación';
          this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
        },
      });
  }

  cambiarEstado(postulacion: Postulacion, nuevoEstado: EstadoPostulacion): void {
    this.postulacionService.cambiarEstado(postulacion.id, nuevoEstado).subscribe({
      next: () => {
        this.snackBar.open(`Postulación ${nuevoEstado.toLowerCase()}`, 'Cerrar', {
          duration: 3000,
        });
        this.cargarPostulaciones();
      },
      error: (error) => {
        const mensaje = error?.error?.mensaje ?? 'Error al cambiar el estado';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      },
    });
  }

  colorEstado(estado: EstadoPostulacion): string {
    switch (estado) {
      case 'APROBADA':
        return 'primary';
      case 'RECHAZADA':
        return 'warn';
      default:
        return '';
    }
  }
}
