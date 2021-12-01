/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';

/**
 * Global configuration
 */
@Injectable({
  providedIn: 'root',
})
export class ApiConfiguration {
  rootUrl: string = 'http://192.168.122.28:8080';
}

/**
 * Parameters for `ApiModule.forRoot()`
 */
export interface ApiConfigurationParams {
  rootUrl?: string;
}