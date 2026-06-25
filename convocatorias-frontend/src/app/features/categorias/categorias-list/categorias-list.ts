import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { CategoriaService } from '../../../core/services/categoria.service';
import { Categoria } from '../../../core/models/categoria.model';
import { CategoriaFormDialogComponent } from '../ categoria-form-dialog/categoria-form-dialog';

@Component({
  selector: 'app-categorias-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatDialogModule],
  templateUrl: './categorias-list.html',
  styleUrl: './categorias-list.css',
})
export class CategoriasListComponent implements OnInit {
  categorias = signal<Categoria[]>([]);
  columnas = ['nombre', 'descripcion', 'acciones'];

  constructor(
    private categoriaService: CategoriaService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.categoriaService.listar().subscribe({
      next: (data) => this.categorias.set(data),
      error: () => this.snackBar.open('Error al cargar categorías', 'Cerrar', { duration: 3000 }),
    });
  }

  abrirFormulario(categoria?: Categoria): void {
    const dialogRef = this.dialog.open(CategoriaFormDialogComponent, {
      width: '420px',
      data: categoria ?? null,
    });

    dialogRef.afterClosed().subscribe((resultado) => {
      if (resultado) {
        this.cargarCategorias();
      }
    });
  }

  eliminar(categoria: Categoria): void {
    if (!confirm(`¿Eliminar la categoría "${categoria.nombre}"?`)) {
      return;
    }

    this.categoriaService.eliminar(categoria.id).subscribe({
      next: () => {
        this.snackBar.open('Categoría eliminada', 'Cerrar', { duration: 3000 });
        this.cargarCategorias();
      },
      error: (error) => {
        const mensaje = error?.error?.mensaje ?? 'Error al eliminar la categoría';
        this.snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      },
    });
  }
}
