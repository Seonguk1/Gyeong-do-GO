/* eslint-disable */
import * as Router from 'expo-router';

export * from 'expo-router';

declare module 'expo-router' {
  export namespace ExpoRouter {
    export interface __routes<T extends string | object = string> {
      hrefInputParams: { pathname: Router.RelativePathString, params?: Router.UnknownInputParams } | { pathname: Router.ExternalPathString, params?: Router.UnknownInputParams } | { pathname: `/`; params?: Router.UnknownInputParams; } | { pathname: `/_sitemap`; params?: Router.UnknownInputParams; } | { pathname: `/entry/createRoom`; params?: Router.UnknownInputParams; } | { pathname: `/entry/joinRoom`; params?: Router.UnknownInputParams; } | { pathname: `/entry/main`; params?: Router.UnknownInputParams; } | { pathname: `/room`; params?: Router.UnknownInputParams; } | { pathname: `/room/[roomId]`, params: Router.UnknownInputParams & { roomId: string | number; } };
      hrefOutputParams: { pathname: Router.RelativePathString, params?: Router.UnknownOutputParams } | { pathname: Router.ExternalPathString, params?: Router.UnknownOutputParams } | { pathname: `/`; params?: Router.UnknownOutputParams; } | { pathname: `/_sitemap`; params?: Router.UnknownOutputParams; } | { pathname: `/entry/createRoom`; params?: Router.UnknownOutputParams; } | { pathname: `/entry/joinRoom`; params?: Router.UnknownOutputParams; } | { pathname: `/entry/main`; params?: Router.UnknownOutputParams; } | { pathname: `/room`; params?: Router.UnknownOutputParams; } | { pathname: `/room/[roomId]`, params: Router.UnknownOutputParams & { roomId: string; } };
      href: Router.RelativePathString | Router.ExternalPathString | `/${`?${string}` | `#${string}` | ''}` | `/_sitemap${`?${string}` | `#${string}` | ''}` | `/entry/createRoom${`?${string}` | `#${string}` | ''}` | `/entry/joinRoom${`?${string}` | `#${string}` | ''}` | `/entry/main${`?${string}` | `#${string}` | ''}` | `/room${`?${string}` | `#${string}` | ''}` | { pathname: Router.RelativePathString, params?: Router.UnknownInputParams } | { pathname: Router.ExternalPathString, params?: Router.UnknownInputParams } | { pathname: `/`; params?: Router.UnknownInputParams; } | { pathname: `/_sitemap`; params?: Router.UnknownInputParams; } | { pathname: `/entry/createRoom`; params?: Router.UnknownInputParams; } | { pathname: `/entry/joinRoom`; params?: Router.UnknownInputParams; } | { pathname: `/entry/main`; params?: Router.UnknownInputParams; } | { pathname: `/room`; params?: Router.UnknownInputParams; } | `/room/${Router.SingleRoutePart<T>}${`?${string}` | `#${string}` | ''}` | { pathname: `/room/[roomId]`, params: Router.UnknownInputParams & { roomId: string | number; } };
    }
  }
}
