/* tslint:disable */
/* eslint-disable */
import { MediaDto } from './media-dto';
import { PageableObject } from './pageable-object';
import { Sort } from './sort';
export interface PageMediaDto {
  content?: Array<MediaDto>;
  empty?: boolean;
  first?: boolean;
  last?: boolean;
  number?: number;
  numberOfElements?: number;
  pageable?: PageableObject;
  size?: number;
  sort?: Sort;
  totalElements?: number;
  totalPages?: number;
}
