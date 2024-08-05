import { IProduct } from 'app/entities/product/product.model';
import { IOffer } from 'app/entities/offer/offer.model';

export interface IOfferDetail {
  id: number;
  feedback?: string | null;
  product?: Pick<IProduct, 'id'> | null;
  offer?: Pick<IOffer, 'id'> | null;
}

export type NewOfferDetail = Omit<IOfferDetail, 'id'> & { id: null };
