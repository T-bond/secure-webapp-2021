/* tslint:disable */
/* eslint-disable */
import { CreatorDto } from './creator-dto';
export interface CommentDto {
  comment: string;
  createdAt: string;
  createdBy: CreatorDto;
  id: number;
}
