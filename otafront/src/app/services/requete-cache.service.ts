import { Injectable } from '@angular/core';
import { HttpRequest, HttpResponse } from '@angular/common/http';
import { ServerSettings } from './const-server';

const maxAge = ServerSettings.CACHE_TIMEOUT;
@Injectable()
export class RequeteCache  {

  public static exceptions: string[] = [
    ServerSettings.BASE_URL + 'produits/getpanier',
    ServerSettings.BASE_URL + 'authentication/getadresses'
  ];

  public static exceptionsNoParam: string[] = [
    ServerSettings.BASE_URL + 'commandes/getcommandes'
  ];
  
  public static postWithNoLogin: string[] = [
    ServerSettings.BASE_URL + 'authentication/inscription',
    ServerSettings.BASE_URL + 'authentication/updatepassword',
    ServerSettings.BASE_URL + 'authentication/confirmEmail',
    ServerSettings.BASE_URL + 'authentication/recuperepassword',
    ServerSettings.BASE_URL + 'authentication/validerecupcode',
    ServerSettings.BASE_URL + 'authentication/validerecupmotdepasse',
    ServerSettings.BASE_URL + 'authentication/connect',
  ];
/*

TRY PERSIST CACHE WHEN RELOADED 
OR PERSIST AT EVERY NEW CACHED

*/
  cache = new Map();

  get(req: HttpRequest<any>): HttpResponse<any> | undefined {
    
    if (req.method === 'POST' || RequeteCache.exceptions.indexOf(req.urlWithParams) !== -1 
    || RequeteCache.exceptionsNoParam.indexOf(req.url.split('?')[0]) !== -1) {
      // console.log('skipping [' + req.method + '] : ' + req.urlWithParams);
      return;
    }
   // console.log('caching x' + req.urlWithParams);
      
    const url = req.urlWithParams;
    // console.log(this.cache);
    
    const cached = this.cache.get(url);
   // console.log('cached ' + cached);
    if (!cached) {
      return undefined;
    }

    const isExpired = cached.lastRead < (Date.now() - maxAge);
    if (isExpired) {
      
    }
    const expired = isExpired ? 'expired ' : '';
    return cached.response;
  }

  put(req: HttpRequest<any>, response: HttpResponse<any>): void {
    const url = req.url;
    const entry = { url, response, lastRead: Date.now() };
    this.cache.set(url, entry);
    const expired = Date.now() - maxAge;

    this.cache.forEach( expiredEntry => { 
      if (expiredEntry.lastRead < expired) { this.cache.delete(expiredEntry.url); } 
    });
    
    // localStorage.setItem(url, JSON.stringify(entry));
  }
}
