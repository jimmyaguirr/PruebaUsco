import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatSnackBar } from '@angular/material/snack-bar';

import { ConvocatoriaService } from '../../../core/services/convocatoria.service';
import { Convocatoria, EstadoConvocatoria } from '../../../core/models/convocatoria.model';
import { ConvocatoriaFormDialogComponent } from '../convocatoria-form-dialog/convocatoria-form-dialog';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-convocatorias-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDialogModule,
    MatMenuModule,
  ],
  templateUrl: './convocatorias-list.html',
  styleUrl: './convocatorias-list.css',
})
export class ConvocatoriasListComponent implements OnInit {
  convocatorias = signal<Convocatoria[]>([]);
  columnas = ['nombre', 'categorias', 'fechas', 'cupos', 'estado', 'acciones'];

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    public authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.cargarConvocatorias();
  }

  cargarConvocatorias(): void {
    this.convocatoriaService.listar().subscribe({
      next: (data) => this.convocatorias.set(data),
      error: () =>
        this.snackBar.open('Error al cargar convocatorias', 'Cerrar', { duration: 3000 }),
    });
  }

  abrirFormulario(convocatoria?: Convocatoria): void {
    const dialogRef = this.dialog.open(ConvocatoriaFormDialogComponent, {
      width: '550px',
      data: convocatoria ?? null,
    });

    dialogRef.afterClosed().subscribe((resultado) => {
      if (resultado) {
        this.cargarConvocatorias();
      }
    });
  }

  cambiarEstado(convocatoria: Convocatoria, nuevoEstado: EstadoConvocatoria): void {
    this.convocatoriaService.cambiarEstado(convocatoria.id, nuevoEstado).subscribe({
      next: () => {
        this.snackBar.open(`Convocatoria ${nuevoEstado.toLowerCase()}`, 'Cerrar', {
          duration: 3000,
        });
        this.cargarConvocatorias();
      },
      error: (error) => {
        const mensaje = error?.error?.mensaje ?? 'Error al cambiar el estado';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      },
    });
  }

  eliminar(convocatoria: Convocatoria): void {
    if (!confirm(`¿Eliminar la convocatoria "${convocatoria.nombre}"?`)) {
      return;
    }

    this.convocatoriaService.eliminar(convocatoria.id).subscribe({
      next: () => {
        this.snackBar.open('Convocatoria eliminada', 'Cerrar', { duration: 3000 });
        this.cargarConvocatorias();
      },
      error: (error) => {
        const mensaje = error?.error?.mensaje ?? 'Error al eliminar la convocatoria';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      },
    });
  }

  colorEstado(estado: EstadoConvocatoria): string {
    switch (estado) {
      case 'PUBLICADA':
        return 'primary';
      case 'CERRADA':
        return 'warn';
      default:
        return '';
    }
  }
}
