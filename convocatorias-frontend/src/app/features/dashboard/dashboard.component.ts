import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../core/services/auth.service';
import { ReporteService } from '../../core/services/reporte.service';
import {
  ConvocatoriaPorCategoria,
  PostulacionesPorConvocatoria,
  ResultadoPostulaciones,
} from '../../core/models/reportes.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, MatCardModule, MatIconModule, MatButtonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  datosConvocatoriasPorCategoria = signal<ConvocatoriaPorCategoria[]>([]);
  datosPostulacionesPorConvocatoria = signal<PostulacionesPorConvocatoria[]>([]);
  datosResultadoPostulaciones = signal<ResultadoPostulaciones[]>([]);

  totalConvocatorias = computed(() =>
    this.datosConvocatoriasPorCategoria().reduce((sum, d) => sum + d.totalConvocatorias, 0),
  );
  totalPostulaciones = computed(() =>
    this.datosPostulacionesPorConvocatoria().reduce((sum, d) => sum + d.totalPostulaciones, 0),
  );
  totalAprobadas = computed(
    () => this.datosResultadoPostulaciones().find((d) => d.estado === 'APROBADA')?.total ?? 0,
  );
  totalPendientes = computed(() => {
    const aprobadas = this.totalAprobadas();
    const rechazadas =
      this.datosResultadoPostulaciones().find((d) => d.estado === 'RECHAZADA')?.total ?? 0;
    return this.totalPostulaciones() - aprobadas - rechazadas;
  });

  constructor(
    public authService: AuthService,
    private reporteService: ReporteService,
  ) {}

  ngOnInit(): void {
    if (this.authService.tieneRol('ADMINISTRADOR')) {
      this.cargarKpis();
    }
  }

  private cargarKpis(): void {
    this.reporteService.convocatoriasPorCategoria().subscribe({
      next: (data) => this.datosConvocatoriasPorCategoria.set(data),
    });
    this.reporteService.postulacionesPorConvocatoria().subscribe({
      next: (data) => this.datosPostulacionesPorConvocatoria.set(data),
    });
    this.reporteService.resultadoPostulaciones().subscribe({
      next: (data) => this.datosResultadoPostulaciones.set(data),
    });
  }
}
