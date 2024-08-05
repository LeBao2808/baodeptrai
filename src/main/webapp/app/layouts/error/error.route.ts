import { Routes } from '@angular/router';

import ErrorComponent from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    title: 'Trang thông tin lỗi!',
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      errorMessage: 'Bạn không có quyền xem trang này.',
    },
    title: 'Trang thông tin lỗi!',
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      errorMessage: 'Trang không tồn tại.',
    },
    title: 'Trang thông tin lỗi!',
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
