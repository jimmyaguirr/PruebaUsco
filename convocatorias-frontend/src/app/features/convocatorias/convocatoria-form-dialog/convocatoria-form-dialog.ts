import { Component, Inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar } from '@angular/material/snack-bar';

import { ConvocatoriaService } from '../../../core/services/convocatoria.service';
import { CategoriaService } from '../../../core/services/categoria.service';
import { Convocatoria } from '../../../core/models/convocatoria.model';
import { Categoria } from '../../../core/models/categoria.model';

@Component({
  selector: 'app-convocatoria-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  templateUrl: './convocatoria-form-dialog.html',
  styleUrl: './convocatoria-form-dialog.css',
})
export class ConvocatoriaFormDialogComponent implements OnInit {
  formulario: FormGroup;
  esEdicion: boolean;
  categoriasDisponibles = signal<Categoria[]>([]);
  estados = ['BORRADOR', 'PUBLICADA', 'CERRADA'];

  constructor(
    private fb: FormBuilder,
    private convocatoriaService: ConvocatoriaService,
    private categoriaService: CategoriaService,
    private dialogRef: MatDialogRef<ConvocatoriaFormDialogComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: Convocatoria | null,
  ) {
    this.esEdicion = !!data;

    this.formulario = this.fb.group({
      nombre: [data?.nombre ?? '', [Validators.required, Validators.maxLength(200)]],
      descripcion: [data?.descripcion ?? ''],
      fechaInicio: [data?.fechaInicio ? new Date(data.fechaInicio) : null, [Validators.required]],
      fechaFin: [data?.fechaFin ? new Date(data.fechaFin) : null, [Validators.required]],
      cuposDisponibles: [data?.cuposDisponibles ?? 1, [Validators.required, Validators.min(0)]],
      estado: [data?.estado ?? 'BORRADOR', [Validators.required]],
      idsCategorias: [data?.categorias?.map((c) => c.id) ?? []],
    });
  }

  ngOnInit(): void {
    this.categoriaService.listar().subscribe({
      next: (categorias) => this.categoriasDisponibles.set(categorias),
      error: () => this.snackBar.open('Error al cargar categorías', 'Cerrar', { duration: 3000 }),
    });
  }

  guardar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valores = this.formulario.value;
    const payload = {
      ...valores,
      fechaInicio: this.formatearFecha(valores.fechaInicio),
      fechaFin: this.formatearFecha(valores.fechaFin),
    };

    if (this.esEdicion && this.data) {
      this.convocatoriaService.actualizar(this.data.id, payload).subscribe({
        next: () => {
          this.snackBar.open('Convocatoria actualizada', 'Cerrar', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (error) => this.mostrarError(error),
      });
    } else {
      this.convocatoriaService.crear(payload).subscribe({
        next: () => {
          this.snackBar.open('Convocatoria creada', 'Cerrar', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (error) => this.mostrarError(error),
      });
    }
  }

  cancelar(): void {
    this.dialogRef.close(false);
  }

  private formatearFecha(fecha: Date): string {
    const año = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${año}-${mes}-${dia}`;
  }

  private mostrarError(error: any): void {
    const mensaje = error?.error?.mensaje ?? 'Ocurrió un error al guardar la convocatoria';
    this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
  }
}
