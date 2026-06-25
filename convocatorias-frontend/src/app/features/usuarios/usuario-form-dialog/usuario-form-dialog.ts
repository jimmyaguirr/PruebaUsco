import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';

import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../core/models/usuario.model';

@Component({
  selector: 'app-usuario-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
  ],
  templateUrl: './usuario-form-dialog.html',
  styleUrl: './usuario-form-dialog.css',
})
export class UsuarioFormDialogComponent {
  formulario: FormGroup;
  esEdicion: boolean;
  roles = ['ADMINISTRADOR', 'DOCENTE', 'ESTUDIANTE'];
  estados = ['ACTIVO', 'INACTIVO'];

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private dialogRef: MatDialogRef<UsuarioFormDialogComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: Usuario | null,
  ) {
    this.esEdicion = !!data;

    this.formulario = this.fb.group({
      identificacion: [data?.identificacion ?? '', [Validators.required, Validators.maxLength(20)]],
      nombre: [data?.nombre ?? '', [Validators.required, Validators.maxLength(150)]],
      correo: [data?.correo ?? '', [Validators.required, Validators.email]],
      password: ['', this.esEdicion ? [] : [Validators.required]],
      rol: [data?.rol ?? 'ESTUDIANTE', [Validators.required]],
      estado: [data?.estado ?? 'ACTIVO', [Validators.required]],
    });
  }

  guardar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valores = this.formulario.value;

    if (this.esEdicion && this.data) {
      const payload = {
        ...valores,
        password: valores.password || 'sin-cambios', // el backend exige el campo; ajustar si se agrega lógica de "no cambiar password"
      };
      this.usuarioService.actualizar(this.data.id, payload).subscribe({
        next: () => {
          this.snackBar.open('Usuario actualizado', 'Cerrar', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (error) => this.mostrarError(error),
      });
    } else {
      this.usuarioService.crear(valores).subscribe({
        next: () => {
          this.snackBar.open('Usuario creado', 'Cerrar', { duration: 3000 });
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
    const mensaje = error?.error?.mensaje ?? 'Ocurrió un error al guardar el usuario';
    this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
  }
}
