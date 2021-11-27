/* tslint:disable */
/* eslint-disable */
import { CreatorDto } from './creator-dto';
export interface MediaDto {
  createdAt: string;
  createdBy: CreatorDto;
  description: string;
  id: number;
  title: string;
}
