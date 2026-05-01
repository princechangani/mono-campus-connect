// =============================================================================
// CampusConnect — MongoDB Schema
// Run with:  mongosh "mongodb://localhost:27017/campusconnect" 02_mongodb_schema.js
// =============================================================================
// Convention:
//   • Every document has: public_id (UUID string), tenant_id (UUID string),
//     created_at, created_by (bigint ref to PG users), updated_at, updated_by,
//     deleted_at, deleted_by, is_deleted  (mirrors PostgreSQL audit fields)
//   • tenant_id is present on EVERY document — used as the first field in all
//     compound indexes for multi-tenant isolation
//   • Numeric FKs (course_id, faculty_id …) reference PostgreSQL BIGINT PKs
// =============================================================================

use campusconnect;

// ─────────────────────────────────────────────────────────────────────────────
// HELPER — drop + recreate a collection cleanly during dev
// ─────────────────────────────────────────────────────────────────────────────
function recreate(name) {
  try { db[name].drop(); } catch(e) {}
  db.createCollection(name);
  print("✔  collection created: " + name);
}


// =============================================================================
// COLLECTION 1 — study_materials
// Files, notes, videos, links uploaded by faculty for a course/batch
// =============================================================================
recreate("study_materials");

db.study_materials.createIndex({ tenant_id: 1, is_deleted: 1 });
db.study_materials.createIndex({ tenant_id: 1, course_assignment_id: 1, is_deleted: 1 });
db.study_materials.createIndex({ tenant_id: 1, course_id: 1, semester_number: 1, is_deleted: 1 });
db.study_materials.createIndex({ tenant_id: 1, batch_id: 1, is_deleted: 1 });
db.study_materials.createIndex({ tenant_id: 1, faculty_id: 1, is_deleted: 1 });
db.study_materials.createIndex({ tenant_id: 1, tags: 1, is_deleted: 1 });
db.study_materials.createIndex(
  { public_id: 1 },
  { unique: true }
);

// JSON Schema validation
db.runCommand({
  collMod: "study_materials",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["public_id", "tenant_id", "course_id", "faculty_id", "title",
                 "material_type", "visibility", "is_active", "is_deleted",
                 "created_at", "created_by"],
      properties: {
        public_id: {
          bsonType: "string",
          description: "UUID exposed in APIs — required"
        },
        tenant_id: {
          bsonType: "string",
          description: "UUID ref to PG tenants.tenant_public_id — required"
        },
        course_assignment_id: {
          bsonType: ["long", "int", "null"],
          description: "BigInt ref to PG course_assignments"
        },
        course_id: {
          bsonType: ["long", "int"],
          description: "BigInt ref to PG courses — required"
        },
        faculty_id: {
          bsonType: ["long", "int"],
          description: "BigInt ref to PG faculty — required"
        },
        batch_id: {
          bsonType: ["long", "int", "null"]
        },
        semester_number: {
          bsonType: ["int", "null"],
          minimum: 1
        },
        title: {
          bsonType: "string",
          minLength: 1,
          maxLength: 500
        },
        description: {
          bsonType: ["string", "null"]
        },
        material_type: {
          bsonType: "string",
          enum: ["notes", "video", "link", "pdf", "ppt", "image", "zip", "other"]
        },
        file_url: {
          bsonType: ["string", "null"]
        },
        file_size_bytes: {
          bsonType: ["long", "int", "null"],
          minimum: 0
        },
        mime_type: {
          bsonType: ["string", "null"]
        },
        thumbnail_url: {
          bsonType: ["string", "null"]
        },
        tags: {
          bsonType: ["array", "null"],
          items: { bsonType: "string" }
        },
        visibility: {
          bsonType: "string",
          enum: ["batch", "course", "program", "public"]
        },
        is_active: {
          bsonType: "bool"
        },
        view_count: {
          bsonType: ["int", "long"],
          minimum: 0
        },
        download_count: {
          bsonType: ["int", "long"],
          minimum: 0
        },
        // ── audit ──
        created_at:  { bsonType: "date" },
        created_by:  { bsonType: ["long", "int"] },
        updated_at:  { bsonType: ["date", "null"] },
        updated_by:  { bsonType: ["long", "int", "null"] },
        deleted_at:  { bsonType: ["date", "null"] },
        deleted_by:  { bsonType: ["long", "int", "null"] },
        is_deleted:  { bsonType: "bool" }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "warn"
});

// Sample document shape:
/*
{
  _id: ObjectId("..."),
  public_id: "550e8400-e29b-41d4-a716-446655440000",
  tenant_id: "tenant-uuid-here",
  course_assignment_id: NumberLong(15),
  course_id: NumberLong(3),
  faculty_id: NumberLong(7),
  batch_id: NumberLong(2),
  semester_number: 3,
  title: "Data Structures — Lecture 5: Binary Trees",
  description: "Covers binary tree traversal algorithms...",
  material_type: "pdf",
  file_url: "https://cdn.example.com/materials/ds-lec5.pdf",
  file_size_bytes: NumberLong(204800),
  mime_type: "application/pdf",
  thumbnail_url: null,
  tags: ["trees", "traversal", "BFS", "DFS"],
  visibility: "batch",
  is_active: true,
  view_count: 0,
  download_count: 0,
  created_at: new Date(),
  created_by: NumberLong(7),
  updated_at: null,
  updated_by: null,
  deleted_at: null,
  deleted_by: null,
  is_deleted: false
}
*/


// =============================================================================
// COLLECTION 2 — notifications
// Push / email / SMS / in-app notifications with per-recipient delivery state
// =============================================================================
recreate("notifications");

db.notifications.createIndex({ tenant_id: 1, is_deleted: 1 });
db.notifications.createIndex({ tenant_id: 1, status: 1, scheduled_at: 1 });
db.notifications.createIndex({ tenant_id: 1, category: 1, is_deleted: 1 });
// For querying unread notifications for a specific user
db.notifications.createIndex({ "recipients.user_id": 1, "recipients.read_at": 1 });
db.notifications.createIndex({ tenant_id: 1, "recipients.user_id": 1, is_deleted: 1 });
db.notifications.createIndex(
  { public_id: 1 },
  { unique: true }
);
// Auto-expire delivered notifications after 90 days
db.notifications.createIndex(
  { sent_at: 1 },
  { expireAfterSeconds: 7776000, partialFilterExpression: { status: "sent" } }
);

db.runCommand({
  collMod: "notifications",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["public_id", "tenant_id", "notification_type", "category",
                 "title", "body", "status", "is_deleted", "created_at", "created_by"],
      properties: {
        public_id:         { bsonType: "string" },
        tenant_id:         { bsonType: "string" },
        notification_type: {
          bsonType: "string",
          enum: ["push", "email", "sms", "in_app"]
        },
        category: {
          bsonType: "string",
          enum: ["attendance", "fee", "exam", "announcement", "leave", "system", "general"]
        },
        title:       { bsonType: "string", minLength: 1 },
        body:        { bsonType: "string", minLength: 1 },
        data: {
          bsonType: ["object", "null"],
          description: "Arbitrary metadata e.g. { student_id: 1, course_id: 2 }"
        },
        recipients: {
          bsonType: ["array", "null"],
          items: {
            bsonType: "object",
            required: ["user_id"],
            properties: {
              user_id:      { bsonType: ["long", "int"] },
              delivered_at: { bsonType: ["date", "null"] },
              read_at:      { bsonType: ["date", "null"] },
              failed_at:    { bsonType: ["date", "null"] },
              failure_reason: { bsonType: ["string", "null"] }
            }
          }
        },
        bulk_group:    { bsonType: ["string", "null"] },
        sent_at:       { bsonType: ["date", "null"] },
        scheduled_at:  { bsonType: ["date", "null"] },
        status: {
          bsonType: "string",
          enum: ["pending", "scheduled", "sending", "sent", "failed", "cancelled"]
        },
        // ── audit ──
        created_at:  { bsonType: "date" },
        created_by:  { bsonType: ["long", "int"] },
        updated_at:  { bsonType: ["date", "null"] },
        updated_by:  { bsonType: ["long", "int", "null"] },
        deleted_at:  { bsonType: ["date", "null"] },
        deleted_by:  { bsonType: ["long", "int", "null"] },
        is_deleted:  { bsonType: "bool" }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "warn"
});

// Sample document shape:
/*
{
  _id: ObjectId("..."),
  public_id: "uuid-...",
  tenant_id: "tenant-uuid",
  notification_type: "in_app",
  category: "attendance",
  title: "Attendance below 75%",
  body: "Your attendance in Data Structures is 68%. Please improve.",
  data: { course_id: NumberLong(3), course_name: "Data Structures" },
  recipients: [
    { user_id: NumberLong(101), delivered_at: new Date(), read_at: null, failed_at: null }
  ],
  bulk_group: "batch_id:2",
  sent_at: new Date(),
  scheduled_at: null,
  status: "sent",
  created_at: new Date(),
  created_by: NumberLong(1),
  updated_at: null,
  updated_by: null,
  deleted_at: null,
  deleted_by: null,
  is_deleted: false
}
*/


// =============================================================================
// COLLECTION 3 — audit_logs
// Immutable write trail for every mutation across the system.
// NOTE: documents in this collection are NEVER deleted — no soft-delete fields.
// =============================================================================
recreate("audit_logs");

db.audit_logs.createIndex({ tenant_id: 1, created_at: -1 });
db.audit_logs.createIndex({ tenant_id: 1, actor_user_id: 1, created_at: -1 });
db.audit_logs.createIndex({ tenant_id: 1, resource_type: 1, resource_id: 1, created_at: -1 });
db.audit_logs.createIndex({ tenant_id: 1, action: 1, created_at: -1 });
db.audit_logs.createIndex({ request_id: 1 });

db.runCommand({
  collMod: "audit_logs",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["tenant_id", "actor_user_id", "actor_role", "action",
                 "resource_type", "resource_id", "resource_table", "created_at"],
      properties: {
        tenant_id:      { bsonType: "string" },
        actor_user_id:  { bsonType: ["long", "int"] },
        actor_role:     { bsonType: "string" },
        action: {
          bsonType: "string",
          enum: ["CREATE", "UPDATE", "DELETE", "RESTORE", "LOGIN", "LOGOUT",
                 "LOGIN_FAILED", "EXPORT", "IMPORT", "PASSWORD_RESET", "ROLE_ASSIGNED"]
        },
        resource_type:  { bsonType: "string" },
        resource_id:    { bsonType: "string",  description: "public UUID of the resource" },
        resource_table: { bsonType: "string" },
        old_value:      { bsonType: ["object", "null"] },
        new_value:      { bsonType: ["object", "null"] },
        diff:           { bsonType: ["array",  "null"], items: { bsonType: "string" } },
        ip_address:     { bsonType: ["string", "null"] },
        user_agent:     { bsonType: ["string", "null"] },
        request_id:     { bsonType: ["string", "null"] },
        created_at:     { bsonType: "date" }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"   // hard fail — audit records must always be valid
});

// Sample document shape:
/*
{
  _id: ObjectId("..."),
  tenant_id: "tenant-uuid",
  actor_user_id: NumberLong(1),
  actor_role: "admin",
  action: "UPDATE",
  resource_type: "student",
  resource_id: "student-public-uuid",
  resource_table: "students",
  old_value: { status: "active" },
  new_value: { status: "suspended" },
  diff: ["status"],
  ip_address: "203.0.113.5",
  user_agent: "Mozilla/5.0 ...",
  request_id: "req-uuid",
  created_at: new Date()
}
*/


// =============================================================================
// COLLECTION 4 — event_logs
// Raw clickstream / feature-usage analytics. Auto-expires after 90 days.
// =============================================================================
recreate("event_logs");

db.event_logs.createIndex({ tenant_id: 1, user_id: 1, created_at: -1 });
db.event_logs.createIndex({ tenant_id: 1, event_name: 1, created_at: -1 });
db.event_logs.createIndex({ tenant_id: 1, session_id: 1 });
// TTL — auto-purge events older than 90 days
db.event_logs.createIndex(
  { created_at: 1 },
  { expireAfterSeconds: 7776000 }
);

db.runCommand({
  collMod: "event_logs",
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["tenant_id", "user_id", "event_name", "created_at"],
      properties: {
        tenant_id:   { bsonType: "string" },
        session_id:  { bsonType: ["string", "null"] },
        user_id:     { bsonType: ["long", "int"] },
        event_name: {
          bsonType: "string",
          description: "e.g. attendance_marked | material_downloaded | marks_viewed"
        },
        properties:  { bsonType: ["object", "null"] },
        device:  {
          bsonType: ["string", "null"],
          enum: ["web", "ios", "android", null]
        },
        os:          { bsonType: ["string", "null"] },
        browser:     { bsonType: ["string", "null"] },
        ip_address:  { bsonType: ["string", "null"] },
        created_at:  { bsonType: "date" }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "warn"
});

// Sample document shape:
/*
{
  _id: ObjectId("..."),
  tenant_id: "tenant-uuid",
  session_id: "session-uuid",
  user_id: NumberLong(101),
  event_name: "material_downloaded",
  properties: {
    material_id: "material-uuid",
    course_id: NumberLong(3),
    file_size_bytes: NumberLong(204800)
  },
  device: "web",
  os: "macOS 14",
  browser: "Chrome 122",
  ip_address: "203.0.113.5",
  created_at: new Date()
}
*/


// =============================================================================
// COLLECTION 5 — chat_messages  (optional: internal messaging module)
// =============================================================================
recreate("chat_messages");

db.chat_messages.createIndex({ tenant_id: 1, channel_id: 1, created_at: -1 });
db.chat_messages.createIndex({ tenant_id: 1, sender_id: 1, created_at: -1 });
db.chat_messages.createIndex({ tenant_id: 1, is_deleted: 1 });
db.chat_messages.createIndex(
  { public_id: 1 },
  { unique: true }
);

// Sample document shape:
/*
{
  _id: ObjectId("..."),
  public_id: "uuid-...",
  tenant_id: "tenant-uuid",
  channel_id: "channel-uuid",        // could be batch_id, course_id, or DM pair
  channel_type: "batch|course|dm|announcement",
  sender_id: NumberLong(101),
  message_type: "text|image|file|link",
  text: "Please check slide 10 in the uploaded PDF.",
  attachments: [
    { url: "https://cdn.../file.pdf", name: "lecture.pdf", size_bytes: 204800 }
  ],
  reply_to_message_id: null,
  reactions: [
    { emoji: "👍", user_ids: [NumberLong(102), NumberLong(103)] }
  ],
  read_by: [
    { user_id: NumberLong(102), read_at: new Date() }
  ],
  edited_at: null,
  created_at: new Date(),
  created_by: NumberLong(101),
  updated_at: null,
  updated_by: null,
  deleted_at: null,
  deleted_by: null,
  is_deleted: false
}
*/


// =============================================================================
// COLLECTION 6 — report_snapshots  (pre-computed heavy reports cached as docs)
// =============================================================================
recreate("report_snapshots");

db.report_snapshots.createIndex({ tenant_id: 1, report_type: 1, generated_at: -1 });
db.report_snapshots.createIndex({ tenant_id: 1, reference_id: 1, report_type: 1 });
// Auto-expire cached reports after 24 hours
db.report_snapshots.createIndex(
  { generated_at: 1 },
  { expireAfterSeconds: 86400 }
);
db.report_snapshots.createIndex(
  { public_id: 1 },
  { unique: true }
);

// Sample document shape:
/*
{
  _id: ObjectId("..."),
  public_id: "uuid-...",
  tenant_id: "tenant-uuid",
  report_type: "attendance_monthly|fee_summary|marks_sheet|student_progress",
  reference_type: "batch|student|department|tenant",
  reference_id: "uuid-of-batch-or-student",
  parameters: { month: 3, year: 2025, semester: 4 },
  data: { ... },              // the actual report payload — arbitrary shape
  generated_at: new Date(),
  generated_by: NumberLong(1),
  expires_at: new Date(Date.now() + 86400000)
}
*/


// =============================================================================
// SUMMARY
// =============================================================================
print("\n=== CampusConnect MongoDB schema applied ===");
print("Collections created:");
db.getCollectionNames().forEach(n => print("  • " + n));
print("\nDone.");
