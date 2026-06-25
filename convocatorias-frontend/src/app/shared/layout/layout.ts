import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatTooltipModule,
    MatDividerModule,
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class LayoutComponent {
  sidebarColapsado = signal(false);

  constructor(
    public authService: AuthService,
    private router: Router,
  ) {}

  toggleSidebar(): void {
    this.sidebarColapsado.set(!this.sidebarColapsado());
  }

  cerrarSesion(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  esAdmin(): boolean {
    return this.authService.tieneRol('ADMINISTRADOR');
  }

  esAdminODocente(): boolean {
    return this.authService.tieneRol('ADMINISTRADOR') || this.authService.tieneRol('DOCENTE');
  }
}
