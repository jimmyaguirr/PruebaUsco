import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';

import { CategoriaService } from '../../../core/services/categoria.service';
import { Categoria } from '../../../core/models/categoria.model';

@Component({
  selector: 'app-categoria-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: './categoria-form-dialog.html',
  styleUrl: './categoria-form-dialog.css',
})
export class CategoriaFormDialogComponent {
  formulario: FormGroup;
  esEdicion: boolean;

  constructor(
    private fb: FormBuilder,
    private categoriaService: CategoriaService,
    private dialogRef: MatDialogRef<CategoriaFormDialogComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: Categoria | null,
  ) {
    this.esEdicion = !!data;

    this.formulario = this.fb.group({
      nombre: [data?.nombre ?? '', [Validators.required, Validators.maxLength(100)]],
      descripcion: [data?.descripcion ?? '', [Validators.maxLength(255)]],
    });
  }

  guardar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valores = this.formulario.value;

    if (this.esEdicion && this.data) {
      this.categoriaService.actualizar(this.data.id, valores).subscribe({
        next: () => {
          this.snackBar.open('Categoría actualizada', 'Cerrar', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (error) => this.mostrarError(error),
      });
    } else {
      this.categoriaService.crear(valores).subscribe({
        next: () => {
          this.snackBar.open('Categoría creada', 'Cerrar', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (error) => this.mostrarError(error),
      });
    }
  }

  cancelar(): void {
    this.dialogRef.close(false);
  }

  private mostrarError(error: any): void {
    const mensaje = error?.error?.mensaje ?? 'Ocurrió un error al guardar la categoría';
    this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
  }
}
