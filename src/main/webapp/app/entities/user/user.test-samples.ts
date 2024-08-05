import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 25040,
  login: 'e@Gxd\\:8aqq\\2EXIoer\\3fizE\\nkz',
};

export const sampleWithPartialData: IUser = {
  id: 22565,
  login: 'AdrIC',
};

export const sampleWithFullData: IUser = {
  id: 2305,
  login: '16iQ',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
