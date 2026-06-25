import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { RolUsuario } from '../models/usuario.model';

export function rolGuard(...rolesPermitidos: RolUsuario[]): CanActivateFn {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (!authService.estaAutenticado()) {
      router.navigate(['/login']);
      return false;
    }

    if (authService.tieneRol(...rolesPermitidos)) {
      return true;
    }

    router.navigate(['/dashboard']);
    return false;
  };
}
