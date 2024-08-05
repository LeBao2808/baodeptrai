import { IProductInventory, NewProductInventory } from './product-inventory.model';

export const sampleWithRequiredData: IProductInventory = {
  id: 26608,
};

export const sampleWithPartialData: IProductInventory = {
  id: 4723,
  quantityOnHand: 11448.8,
  inventoryYear: 12231,
  type: 18992,
};

export const sampleWithFullData: IProductInventory = {
  id: 3869,
  quantityOnHand: 22589.97,
  inventoryMonth: 18597,
  inventoryYear: 28559,
  type: 12732,
  price: 4303.33,
};

export const sampleWithNewData: NewProductInventory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
