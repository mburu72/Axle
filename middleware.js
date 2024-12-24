import { NextResponse } from 'next/server'

export function middleware(request) {
    const isAuthenticated = Boolean(request.cookies.get('token'));

    if (!isAuthenticated) {
      return NextResponse.redirect(new URL('/', request.url));
    }
  
    return NextResponse.next();
}
export const config = {
  matcher: ['/dashboard/:path*', '/profile', '/jobs/:path*', '/joblist',]
}