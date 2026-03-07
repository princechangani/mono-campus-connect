# CampusConnect Frontend

Next.js 14 + Tailwind CSS frontend for the CampusConnect college management system.

## Setup

```bash
cd frontend
npm install
npm run dev
```

Runs on **http://localhost:3000** — proxies `/api/*` to `http://localhost:8081`.

## Structure

```
src/
├── app/
│   ├── (auth)/
│   │   ├── login/            # Login page
│   │   ├── register/         # Register page
│   │   └── forgot-password/  # OTP-based password reset
│   ├── student/
│   │   ├── dashboard/
│   │   ├── courses/
│   │   ├── attendance/
│   │   ├── exams/
│   │   ├── results/
│   │   ├── timetable/
│   │   ├── materials/
│   │   └── events/
│   ├── faculty/
│   │   └── dashboard/
│   ├── admin/
│   │   ├── dashboard/
│   │   ├── students/
│   │   └── faculty/
│   ├── super-admin/
│   │   └── dashboard/
│   ├── notifications/
│   └── profile/
├── components/
│   ├── layout/
│   │   ├── Sidebar.tsx       # Role-aware sidebar
│   │   └── DashboardLayout.tsx
│   └── ui/
│       └── StatCard.tsx
└── lib/
    ├── api.ts                # Axios instance with JWT interceptor
    ├── auth.ts               # Token/role/user cookie helpers
    └── utils.ts              # cn(), formatDate(), getInitials()
```

## Role Routing

| Role | Redirect |
|------|----------|
| SUPER_ADMIN | `/super-admin/dashboard` |
| ADMIN | `/admin/dashboard` |
| FACULTY | `/faculty/dashboard` |
| STUDENT | `/student/dashboard` |

