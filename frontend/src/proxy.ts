import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export default function proxy(request: NextRequest) {
  const token = request.cookies.get("cc_token")?.value;
  const role = request.cookies.get("cc_role")?.value;
  const path = request.nextUrl.pathname;

  // Protect all sub-routes of /admin, /faculty, /student, /super-admin, /hod
  const protectedPrefixes = ["/admin", "/faculty", "/student", "/super-admin", "/hod"];
  const isProtected = protectedPrefixes.some((prefix) => path.startsWith(prefix));

  if (isProtected && !token) {
    // Redirect unauthenticated users to login
    return NextResponse.redirect(new URL("/login", request.url));
  }

  // Prevent role mismatch routing
  if (token && role && path.startsWith(`/${role.toLowerCase().replace("_", "-")}`) === false && isProtected) {
      // Redirect authenticated users to their correct dashboard if they try to access another role's route
      return NextResponse.redirect(new URL(`/${role.toLowerCase().replace("_", "-")}/dashboard`, request.url));
  }

  // If user is at /login but already authenticated, redirect them to their dashboard
  if (path === "/login" && token && role) {
    return NextResponse.redirect(new URL(`/${role.toLowerCase().replace("_", "-")}/dashboard`, request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: [
    "/admin/:path*",
    "/faculty/:path*",
    "/student/:path*",
    "/super-admin/:path*",
    "/hod/:path*",
    "/login"
  ],
};
