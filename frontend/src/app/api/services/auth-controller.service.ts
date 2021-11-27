/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';
import { RequestBuilder } from '../request-builder';
import { Observable } from 'rxjs';
import { map, filter } from 'rxjs/operators';

import { AuthenticationRequestDto } from '../models/authentication-request-dto';
import { UserCreateRequestDto } from '../models/user-create-request-dto';

@Injectable({
  providedIn: 'root',
})
export class AuthControllerService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation signUp
   */
  static readonly SignUpPath = '/auth/sign-up';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `signUp()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  signUp$Response(params: {
    body: UserCreateRequestDto
  }): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, AuthControllerService.SignUpPath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'text',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `signUp$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  signUp(params: {
    body: UserCreateRequestDto
  }): Observable<void> {

    return this.signUp$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

  /**
   * Path part for operation signIn
   */
  static readonly SignInPath = '/auth/sign-in';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `signIn()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  signIn$Response(params: {
    body: AuthenticationRequestDto
  }): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, AuthControllerService.SignInPath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }
    
    return this.http.request(rb.build({
      responseType: 'text',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `signIn$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  signIn(params: {
    body: AuthenticationRequestDto
  }): Observable<void> {

    return this.signIn$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

  /**
   * Path part for operation logout
   */
  static readonly LogoutPath = '/auth/logout';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `logout()` instead.
   *
   * This method doesn't expect any request body.
   */
  logout$Response(params?: {
  }): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, AuthControllerService.LogoutPath, 'delete');
    if (params) {
    }

    return this.http.request(rb.build({
      responseType: 'text',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `logout$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  logout(params?: {
  }): Observable<void> {

    return this.logout$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

}
