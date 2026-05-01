# CampusConnect Complete Codebase Audit Report

## 1. 🔐 Security Vulnerabilities (CRITICAL & HIGH)

### [HIGH] Frontend XSS Vulnerability in Token Storage
- **Location**: [frontend/src/lib/auth.ts](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/lib/auth.ts) -> [setToken()](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/lib/auth.ts#10-12)
- **Issue**: The JWT token and the entire [User](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/lib/auth.ts#20-22) object are stored in `Cookies` (without `HttpOnly`) and `localStorage` respectively. Any successful Cross-Site Scripting (XSS) attack on the frontend can easily steal the session token and PII, leading to full account takeover.
- **Fix**: Move the JWT storage to an `HttpOnly`, `Secure`, `SameSite=Strict` cookie set by the backend. The frontend should not be able to read the token directly. Alternatively, use a robust `Next-Auth` setup.

### [HIGH] Potential Email Bombing / No Rate Limiting on OTPs
- **Location**: [OtpController.java](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/controller/OtpController.java), `/api/otp/send` and `/api/otp/resend` endpoints. These are whitelisted in [SecurityConfig.java](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/config/SecurityConfig.java).
- **Issue**: There is no visible rate-limiting mechanism preventing a malicious actor from hitting the `/send` endpoint thousands of times a minute for the same email address. This can lead to massive SMS/Email costs and spam.
- **Fix**: Implement a rate limiter (e.g., using Redis, Guava RateLimiter, or Bucket4j) limiting OTP requests to max 3-5 per email per hour.

### [MEDIUM] CORS Allows Credentials on Broad Origins
- **Location**: [SecurityConfig.java](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/config/SecurityConfig.java) -> [corsConfigurationSource()](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/config/SecurityConfig.java#84-102)
- **Issue**: The CORS configuration allows credentials (`setAllowCredentials(true)`) while supporting multiple origins (e.g. `http://localhost:3000`, `http://64.227.188.24:5173`). While acceptable in dev, in production, origins should be strictly restricted and validated.
- **Fix**: Externalize CORS origins to `application.yml` and only allow the exact production domain dynamically based on the active profile.

## 2. ⚡ Performance Issues

### [HIGH] Missing Next.js Middleware for Route Protection (SSR Blockage)
- **Location**: [frontend/src/app/page.tsx](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/app/page.tsx) and [DashboardLayout.tsx](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/components/layout/DashboardLayout.tsx)
- **Issue**: Authentication checks are performed inside `useEffect` (Client-Side Rendering). This means users will see a blank page or a loading spinner on every single page load before the client decides whether to redirect them or show the dashboard. It also completely defeats the purpose of Server-Side Rendering (SSR) in the Next.js App Router.
- **Fix**: Implement a `src/middleware.ts` to check token cookies on the edge/server and redirect unauthenticated users *before* the page even renders.

### [MEDIUM] Unused or Underutilized `react-query`
- **Location**: [package.json](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/package.json) and [DashboardLayout.tsx](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/components/layout/DashboardLayout.tsx)
- **Issue**: The project imports `react-query` v3 (which is outdated; v5 is current). On top of that, critical data fetching like unread notifications is done raw using `api.get` inside a `useEffect`. This leads to race conditions, no caching, no automatic retries, and manual `loading` state management.
- **Fix**: Upgrade to `@tanstack/react-query` and wrap all fetches in custom hooks using `useQuery`.

### [LOW] Over-fetching in `localStorage`
- **Location**: [frontend/src/lib/auth.ts](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/lib/auth.ts) -> [setUser()](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/lib/auth.ts#20-22)
- **Issue**: Storing the entire [User](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/frontend/src/lib/auth.ts#20-22) object stringified inside `localStorage` can lead to storage bloat over time if the user object grows. It also forces JSON parsing on the main thread during app load.

### [NOTE] Absence of ORM Relationships (JPA)
- **Location**: All PostgreSQL Models (e.g., [CourseAssignment](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/model/postgres/CourseAssignment.java#8-44), [Student](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/model/postgres/Student.java#14-51), etc.)
- **Observation**: Interestingly, instead of typical `@ManyToOne` bindings, foreign keys are stored as plain `Long` fields (e.g., `private Long batchId`). While this completely circumvents the notorious **JPA N+1 query problem** and avoids accidental eager fetching, it forces manual object assembly in the service layer or reliance on database Views.

## 3. 🐞 Bugs & Logical Issues

### [MEDIUM] No Validation on Role Parsing during Registration
- **Location**: [AuthService.java](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/service/AuthService.java) -> [register()](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/controller/AuthController.java#39-61)
- **Issue**: The parsed role is passed down from `request.getRole()`. A malicious user could send `SUPER_ADMIN` in the registration JSON if there is no check preventing public users from registering high-privilege roles. This is a severe logical flaw (IDOR / privilege escalation) if the [register](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/controller/AuthController.java#39-61) endpoint is publicly exposed (which it currently is!).
- **Fix**: Hardcode the registration role to `STUDENT` or explicitly reject admin roles in the public [register](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/controller/AuthController.java#39-61) endpoint.

## 4. 🧱 Code Quality & Architecture

### [LOW] Missing Caching Strategy (Redis)
- **Location**: Global Data Fetching (e.g. [CourseCanonicalRepository](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/repository/postgres/CourseCanonicalRepository.java#10-16), [DepartmentCanonicalRepository](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/repository/postgres/DepartmentCanonicalRepository.java#10-15))
- **Issue**: Master data like departments, courses, and roles are fetched from the database on every single request. Since these rarely change, this adds unnecessary load to PostgreSQL.
- **Fix**: Integrate `@EnableCaching` with Redis or Caffeine to cache static data.

### [LOW] Direct Usage of `Date` instead of `Instant` or `LocalDateTime`
- **Location**: Models and Services (e.g. [EventLogDocument.java](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/model/mongo/EventLogDocument.java), [OtpService.java](file:///Users/princechanagni/SPRING%20BOOT%20PROJECTS/mono-campus-connect/src/main/java/com/monocampusconnect/service/OtpService.java))
- **Issue**: Using `java.util.Date` is completely outdated and not thread-safe.
- **Fix**: Use Java 8 `java.time.Instant` or `java.time.LocalDateTime`.
