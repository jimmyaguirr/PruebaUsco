import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { rolGuard } from './core/guards/rol.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: '',
    loadComponent: () => import('./shared/layout/layout').then((m) => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent),
      },
      {
        path: 'usuarios',
        canActivate: [rolGuard('ADMINISTRADOR')],
        loadComponent: () =>
          import('./features/usuarios/usuarios-list/usuarios-list').then(
            (m) => m.UsuariosListComponent,
          ),
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },
      {
        path: 'categorias',
        canActivate: [rolGuard('ADMINISTRADOR')],
        loadComponent: () =>
          import('./features/categorias/categorias-list/categorias-list').then(
            (m) => m.CategoriasListComponent,
          ),
      },
      {
        path: 'convocatorias',
        loadComponent: () =>
          import('./features/convocatorias/convocatorias-list/convocatorias-list').then(
            (m) => m.ConvocatoriasListComponent,
          ),
      },
      {
        path: 'postulaciones',
        loadComponent: () =>
          import('./features/postulaciones/postulaciones-list/postulaciones-list').then(
            (m) => m.PostulacionesListComponent,
          ),
      },
      {
        path: 'reportes',
        canActivate: [rolGuard('ADMINISTRADOR')],
        loadComponent: () =>
          import('./features/reportes/reportes-list/reportes-list').then(
            (m) => m.ReportesListComponent,
          ),
      },
    ],
  },
  {
    path: '**',
    redirectTo: 'login',
  },
];
