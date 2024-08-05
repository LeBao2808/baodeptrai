import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'config',
    data: { pageTitle: 'Configs' },
    loadChildren: () => import('./config/config.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'Products' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'supplier',
    data: { pageTitle: 'Suppliers' },
    loadChildren: () => import('./supplier/supplier.routes'),
  },
  {
    path: 'production-site',
    data: { pageTitle: 'ProductionSites' },
    loadChildren: () => import('./production-site/production-site.routes'),
  },
  {
    path: 'material',
    data: { pageTitle: 'Materials' },
    loadChildren: () => import('./material/material.routes'),
  },
  {
    path: 'quantification',
    data: { pageTitle: 'Quantifications' },
    loadChildren: () => import('./quantification/quantification.routes'),
  },
  {
    path: 'planning',
    data: { pageTitle: 'Plannings' },
    loadChildren: () => import('./planning/planning.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'Customers' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'product-order',
    data: { pageTitle: 'ProductOrders' },
    loadChildren: () => import('./product-order/product-order.routes'),
  },
  {
    path: 'product-order-detail',
    data: { pageTitle: 'ProductOrderDetails' },
    loadChildren: () => import('./product-order-detail/product-order-detail.routes'),
  },
  {
    path: 'offer',
    data: { pageTitle: 'Offers' },
    loadChildren: () => import('./offer/offer.routes'),
  },
  {
    path: 'offer-detail',
    data: { pageTitle: 'OfferDetails' },
    loadChildren: () => import('./offer-detail/offer-detail.routes'),
  },
  {
    path: 'material-receipt',
    data: { pageTitle: 'MaterialReceipts' },
    loadChildren: () => import('./material-receipt/material-receipt.routes'),
  },
  {
    path: 'material-receipt-detail',
    data: { pageTitle: 'MaterialReceiptDetails' },
    loadChildren: () => import('./material-receipt-detail/material-receipt-detail.routes'),
  },
  {
    path: 'material-export',
    data: { pageTitle: 'MaterialExports' },
    loadChildren: () => import('./material-export/material-export.routes'),
  },
  {
    path: 'material-export-detail',
    data: { pageTitle: 'MaterialExportDetails' },
    loadChildren: () => import('./material-export-detail/material-export-detail.routes'),
  },
  {
    path: 'material-inventory',
    data: { pageTitle: 'MaterialInventories' },
    loadChildren: () => import('./material-inventory/material-inventory.routes'),
  },
  {
    path: 'product-receipt',
    data: { pageTitle: 'ProductReceipts' },
    loadChildren: () => import('./product-receipt/product-receipt.routes'),
  },
  {
    path: 'product-receipt-detail',
    data: { pageTitle: 'ProductReceiptDetails' },
    loadChildren: () => import('./product-receipt-detail/product-receipt-detail.routes'),
  },
  {
    path: 'product-inventory',
    data: { pageTitle: 'ProductInventories' },
    loadChildren: () => import('./product-inventory/product-inventory.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
