import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js';

import { ReporteService } from '../../../core/services/reporte.service';
import {
  ConvocatoriaPorCategoria,
  PostulacionesPorConvocatoria,
  ResultadoPostulaciones,
} from '../../../core/models/reportes.model';

@Component({
  selector: 'app-reportes-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatTabsModule,
    MatCardModule,
    MatIconModule,
    BaseChartDirective,
  ],
  templateUrl: './reportes-list.html',
  styleUrl: './reportes-list.css',
})
export class ReportesListComponent implements OnInit {
  datosConvocatoriasPorCategoria = signal<ConvocatoriaPorCategoria[]>([]);
  columnasReporte1 = ['categoriaNombre', 'totalConvocatorias'];
  graficoReporte1: ChartData<'bar'> = { labels: [], datasets: [] };

  datosPostulacionesPorConvocatoria = signal<PostulacionesPorConvocatoria[]>([]);
  columnasReporte2 = ['convocatoriaNombre', 'totalPostulaciones'];
  graficoReporte2: ChartData<'bar'> = { labels: [], datasets: [] };

  datosResultadoPostulaciones = signal<ResultadoPostulaciones[]>([]);
  columnasReporte3 = ['estado', 'total'];
  graficoReporte3: ChartData<'doughnut'> = { labels: [], datasets: [] };

  // KPIs calculados para las tarjetas superiores
  totalConvocatorias = computed(() =>
    this.datosConvocatoriasPorCategoria().reduce((sum, d) => sum + d.totalConvocatorias, 0),
  );
  totalPostulaciones = computed(() =>
    this.datosPostulacionesPorConvocatoria().reduce((sum, d) => sum + d.totalPostulaciones, 0),
  );
  totalAprobadas = computed(
    () => this.datosResultadoPostulaciones().find((d) => d.estado === 'APROBADA')?.total ?? 0,
  );
  totalRechazadas = computed(
    () => this.datosResultadoPostulaciones().find((d) => d.estado === 'RECHAZADA')?.total ?? 0,
  );

  opcionesGraficoBarras: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        backgroundColor: '#1e293b',
        padding: 12,
        cornerRadius: 8,
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: { precision: 0 },
        grid: { color: '#f1f5f9' },
      },
      x: {
        grid: { display: false },
      },
    },
  };

  opcionesGraficoDoughnut: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    cutout: '65%',
    plugins: {
      legend: {
        position: 'bottom',
        labels: { padding: 16, usePointStyle: true },
      },
    },
  };

  constructor(
    private reporteService: ReporteService,
    private snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.cargarReporte1();
    this.cargarReporte2();
    this.cargarReporte3();
  }

  private cargarReporte1(): void {
    this.reporteService.convocatoriasPorCategoria().subscribe({
      next: (data) => {
        this.datosConvocatoriasPorCategoria.set(data);
        this.graficoReporte1 = {
          labels: data.map((d) => d.categoriaNombre),
          datasets: [
            {
              label: 'Convocatorias',
              data: data.map((d) => d.totalConvocatorias),
              backgroundColor: '#6366f1',
              borderRadius: 8,
              maxBarThickness: 48,
            },
          ],
        };
      },
      error: () =>
        this.snackBar.open('Error al cargar reporte de categorías', 'Cerrar', { duration: 3000 }),
    });
  }

  private cargarReporte2(): void {
    this.reporteService.postulacionesPorConvocatoria().subscribe({
      next: (data) => {
        this.datosPostulacionesPorConvocatoria.set(data);
        this.graficoReporte2 = {
          labels: data.map((d) => d.convocatoriaNombre),
          datasets: [
            {
              label: 'Postulaciones',
              data: data.map((d) => d.totalPostulaciones),
              backgroundColor: '#10b981',
              borderRadius: 8,
              maxBarThickness: 48,
            },
          ],
        };
      },
      error: () =>
        this.snackBar.open('Error al cargar reporte de postulaciones', 'Cerrar', {
          duration: 3000,
        }),
    });
  }

  private cargarReporte3(): void {
    this.reporteService.resultadoPostulaciones().subscribe({
      next: (data) => {
        this.datosResultadoPostulaciones.set(data);
        this.graficoReporte3 = {
          labels: data.map((d) => d.estado),
          datasets: [
            {
              data: data.map((d) => d.total),
              backgroundColor: ['#10b981', '#ef4444', '#f59e0b'],
              borderWidth: 0,
            },
          ],
        };
      },
      error: () =>
        this.snackBar.open('Error al cargar reporte de resultados', 'Cerrar', { duration: 3000 }),
    });
  }
}
