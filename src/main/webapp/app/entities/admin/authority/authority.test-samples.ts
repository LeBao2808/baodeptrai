import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'e5761317-76e4-48db-a4b2-4f51665fe1b2',
};

export const sampleWithPartialData: IAuthority = {
  name: '892f7153-ddbc-4f99-91a5-e72ec89312d2',
};

export const sampleWithFullData: IAuthority = {
  name: 'cc9eb9f0-9804-44cc-a77e-91931677fc64',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
