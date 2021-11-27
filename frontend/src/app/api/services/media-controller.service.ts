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

import { CommentRequestDto } from '../models/comment-request-dto';
import { PageCommentDto } from '../models/page-comment-dto';
import { PageMediaDto } from '../models/page-media-dto';
import { SearchRequestDto } from '../models/search-request-dto';
import { SingleMediaDto } from '../models/single-media-dto';

@Injectable({
  providedIn: 'root',
})
export class MediaControllerService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation getComments
   */
  static readonly GetCommentsPath = '/medias/{id}/comments';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getComments()` instead.
   *
   * This method doesn't expect any request body.
   */
  getComments$Response(params: {
    id: number;

    /**
     * Zero-based page index (0..N)
     */
    page?: number;

    /**
     * The size of the page to be returned
     */
    size?: number;

    /**
     * Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     */
    sort?: Array<string>;
  }): Observable<StrictHttpResponse<PageCommentDto>> {

    const rb = new RequestBuilder(this.rootUrl, MediaControllerService.GetCommentsPath, 'get');
    if (params) {
      rb.path('id', params.id, {});
      rb.query('page', params.page, {});
      rb.query('size', params.size, {});
      rb.query('sort', params.sort, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PageCommentDto>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getComments$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getComments(params: {
    id: number;

    /**
     * Zero-based page index (0..N)
     */
    page?: number;

    /**
     * The size of the page to be returned
     */
    size?: number;

    /**
     * Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     */
    sort?: Array<string>;
  }): Observable<PageCommentDto> {

    return this.getComments$Response(params).pipe(
      map((r: StrictHttpResponse<PageCommentDto>) => r.body as PageCommentDto)
    );
  }

  /**
   * Path part for operation createComment
   */
  static readonly CreateCommentPath = '/medias/{id}/comments';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createComment()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createComment$Response(params: {
    id: number;
    body?: CommentRequestDto
  }): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, MediaControllerService.CreateCommentPath, 'post');
    if (params) {
      rb.path('id', params.id, {});
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
   * To access the full response (for headers, for example), `createComment$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createComment(params: {
    id: number;
    body?: CommentRequestDto
  }): Observable<void> {

    return this.createComment$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

  /**
   * Path part for operation listMediaFiles
   */
  static readonly ListMediaFilesPath = '/medias';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `listMediaFiles()` instead.
   *
   * This method doesn't expect any request body.
   */
  listMediaFiles$Response(params?: {

    /**
     * Zero-based page index (0..N)
     */
    page?: number;

    /**
     * The size of the page to be returned
     */
    size?: number;

    /**
     * Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     */
    sort?: Array<string>;
  }): Observable<StrictHttpResponse<PageMediaDto>> {

    const rb = new RequestBuilder(this.rootUrl, MediaControllerService.ListMediaFilesPath, 'get');
    if (params) {
      rb.query('page', params.page, {});
      rb.query('size', params.size, {});
      rb.query('sort', params.sort, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PageMediaDto>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `listMediaFiles$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  listMediaFiles(params?: {

    /**
     * Zero-based page index (0..N)
     */
    page?: number;

    /**
     * The size of the page to be returned
     */
    size?: number;

    /**
     * Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     */
    sort?: Array<string>;
  }): Observable<PageMediaDto> {

    return this.listMediaFiles$Response(params).pipe(
      map((r: StrictHttpResponse<PageMediaDto>) => r.body as PageMediaDto)
    );
  }

  /**
   * Path part for operation getMediaFile
   */
  static readonly GetMediaFilePath = '/medias/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getMediaFile()` instead.
   *
   * This method doesn't expect any request body.
   */
  getMediaFile$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<SingleMediaDto>> {

    const rb = new RequestBuilder(this.rootUrl, MediaControllerService.GetMediaFilePath, 'get');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<SingleMediaDto>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getMediaFile$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getMediaFile(params: {
    id: number;
  }): Observable<SingleMediaDto> {

    return this.getMediaFile$Response(params).pipe(
      map((r: StrictHttpResponse<SingleMediaDto>) => r.body as SingleMediaDto)
    );
  }

  /**
   * Path part for operation deleteMediaFile
   */
  static readonly DeleteMediaFilePath = '/medias/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteMediaFile()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteMediaFile$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, MediaControllerService.DeleteMediaFilePath, 'delete');
    if (params) {
      rb.path('id', params.id, {});
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
   * To access the full response (for headers, for example), `deleteMediaFile$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteMediaFile(params: {
    id: number;
  }): Observable<void> {

    return this.deleteMediaFile$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

  /**
   * Path part for operation searchMediaByTitle
   */
  static readonly SearchMediaByTitlePath = '/medias/search';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `searchMediaByTitle()` instead.
   *
   * This method doesn't expect any request body.
   */
  searchMediaByTitle$Response(params: {
    searchDto: SearchRequestDto;

    /**
     * Zero-based page index (0..N)
     */
    page?: number;

    /**
     * The size of the page to be returned
     */
    size?: number;

    /**
     * Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     */
    sort?: Array<string>;
  }): Observable<StrictHttpResponse<PageMediaDto>> {

    const rb = new RequestBuilder(this.rootUrl, MediaControllerService.SearchMediaByTitlePath, 'get');
    if (params) {
      rb.query('searchDto', params.searchDto, {});
      rb.query('page', params.page, {});
      rb.query('size', params.size, {});
      rb.query('sort', params.sort, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PageMediaDto>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `searchMediaByTitle$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  searchMediaByTitle(params: {
    searchDto: SearchRequestDto;

    /**
     * Zero-based page index (0..N)
     */
    page?: number;

    /**
     * The size of the page to be returned
     */
    size?: number;

    /**
     * Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     */
    sort?: Array<string>;
  }): Observable<PageMediaDto> {

    return this.searchMediaByTitle$Response(params).pipe(
      map((r: StrictHttpResponse<PageMediaDto>) => r.body as PageMediaDto)
    );
  }

  /**
   * Path part for operation deleteComment
   */
  static readonly DeleteCommentPath = '/medias/{mediaId}/comments/{commentId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteComment()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteComment$Response(params: {
    mediaId: number;
    commentId: number;
  }): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, MediaControllerService.DeleteCommentPath, 'delete');
    if (params) {
      rb.path('mediaId', params.mediaId, {});
      rb.path('commentId', params.commentId, {});
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
   * To access the full response (for headers, for example), `deleteComment$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteComment(params: {
    mediaId: number;
    commentId: number;
  }): Observable<void> {

    return this.deleteComment$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

}
