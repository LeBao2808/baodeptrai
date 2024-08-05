import dayjs from 'dayjs/esm';

import { IOffer, NewOffer } from './offer.model';

export const sampleWithRequiredData: IOffer = {
  id: 20110,
};

export const sampleWithPartialData: IOffer = {
  id: 3817,
  date: dayjs('2024-08-04T18:33'),
  status: 29712,
  code: 'drafty generate next',
};

export const sampleWithFullData: IOffer = {
  id: 9428,
  date: dayjs('2024-08-05T00:23'),
  status: 1062,
  code: 'mostly gosh',
};

export const sampleWithNewData: NewOffer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
