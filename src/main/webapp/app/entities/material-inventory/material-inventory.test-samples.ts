import { IMaterialInventory, NewMaterialInventory } from './material-inventory.model';

export const sampleWithRequiredData: IMaterialInventory = {
  id: 32758,
};

export const sampleWithPartialData: IMaterialInventory = {
  id: 8371,
  quantityOnHand: 2035.58,
  inventoryMonth: 1824,
  inventoryYear: 8498,
  price: 10237.92,
};

export const sampleWithFullData: IMaterialInventory = {
  id: 7895,
  quantityOnHand: 22004.75,
  inventoryMonth: 4962,
  inventoryYear: 8419,
  type: 17279,
  price: 2263.18,
};

export const sampleWithNewData: NewMaterialInventory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
