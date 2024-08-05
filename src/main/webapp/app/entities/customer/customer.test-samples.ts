import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 18245,
};

export const sampleWithPartialData: ICustomer = {
  id: 5727,
  type: 4839,
};

export const sampleWithFullData: ICustomer = {
  id: 5517,
  name: 'realistic',
  address: 'greet adolescent',
  phone: '0202 1790 3048',
  email: 'LuongThien.Ha@gmail.com',
  type: 29158,
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
