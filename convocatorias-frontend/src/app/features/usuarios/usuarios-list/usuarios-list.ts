import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';

import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../core/models/usuario.model';
import { UsuarioFormDialogComponent } from '../usuario-form-dialog/usuario-form-dialog';

@Component({
  selector: 'app-usuarios-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatChipsModule,
  ],
  templateUrl: './usuarios-list.html',
  styleUrl: './usuarios-list.css',
})
export class UsuariosListComponent implements OnInit {
  usuarios = signal<Usuario[]>([]);
  columnas = ['identificacion', 'nombre', 'correo', 'rol', 'estado', 'acciones'];

  constructor(
    private usuarioService: UsuarioService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.usuarioService.listar().subscribe({
      next: (data) => this.usuarios.set(data),
      error: () => this.snackBar.open('Error al cargar usuarios', 'Cerrar', { duration: 3000 }),
    });
  }

  abrirFormulario(usuario?: Usuario): void {
    const dialogRef = this.dialog.open(UsuarioFormDialogComponent, {
      width: '450px',
      data: usuario ?? null,
    });

    dialogRef.afterClosed().subscribe((resultado) => {
      if (resultado) {
        this.cargarUsuarios();
      }
    });
  }

  eliminar(usuario: Usuario): void {
    if (!confirm(`¿Eliminar al usuario ${usuario.nombre}?`)) {
      return;
    }

    this.usuarioService.eliminar(usuario.id).subscribe({
      next: () => {
        this.snackBar.open('Usuario eliminado', 'Cerrar', { duration: 3000 });
        this.cargarUsuarios();
      },
      error: (error) => {
        const mensaje = error?.error?.mensaje ?? 'Error al eliminar el usuario';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      },
    });
  }
}
