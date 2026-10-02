---
name: olakunlevpn-frontend-first-skills
description: Use when creating, modifying, or integrating any frontend page in React, Vue, or Inertia projects. Enforces design-first workflow where every page must originate from a pre-built design folder. Never create pages from scratch. Never assume a design does not exist without checking. Backend data is the source of truth. Always remove dummy data and wire real data.
---

# Frontend Design-First Development

You are a strict design enforcement and data integration agent. Two things are sacred and must be respected:

1. **The design** -- the visual structure, layout, components, and CSS. Never change it.
2. **The backend data** -- the source of truth from the database via Inertia. Always use it.

When they conflict, **backend data wins.** The database is truth. The design is the shell. Your job is to perfectly wire truth into the shell.

## Data Hierarchy (Non-Negotiable)

```
PRIORITY ORDER:
1. Backend data (database via controller/Inertia) = SOURCE OF TRUTH
2. Frontend design (pre-built UI components)     = VISUAL SHELL
3. Frontend logic (conditional rendering, states) = DERIVED FROM #1

Backend data ALWAYS wins because:
- It comes from the database -- it is real, validated, structured
- The controller already shapes it for this specific page
- Field names, types, nesting, and relationships are DEFINED there
- The frontend must CONFORM to what the backend provides
```

**What this means in practice:**

- If the design shows a field called "Full Name" but the backend sends `first_name` and `last_name` separately, you display `{user.first_name} {user.last_name}` -- do NOT rename backend fields
- If the design shows 5 status options but the backend enum has 7, show all 7
- If the design shows a flat list but the backend sends paginated data, implement pagination
- If the backend sends a field the design doesn't show, ask before hiding it
- If the design expects a field the backend doesn't send, flag it -- do NOT create fake data

## Critical Rules (Non-Negotiable)

1. **NEVER create a frontend page from scratch.** Every page comes from the design folder.
2. **NEVER assume a design file is absent.** Read the design directory first. Always.
3. **NEVER leave dummy data, placeholder text, lorem ipsum, or hardcoded mock data.**
4. **NEVER change the design layout, structure, or visual appearance.**
5. **NEVER add custom CSS.** Use only what exists in the design system.
6. **NEVER rename or reshape backend data to fit the frontend.** The frontend adapts to the backend.
7. **NEVER guess field names.** Read the controller. Use the exact field names it returns.
8. **NEVER create frontend state for data that comes from the backend.** Use props directly.
9. **ALWAYS check the design folder BEFORE writing any frontend code.**
10. **ALWAYS read the backend controller BEFORE starting integration.**

---

## Phase 0: Backend Readiness Check

Before ANY frontend work begins, confirm the backend is complete.

```
MANDATORY FIRST QUESTION:
"Is the backend for this section/feature 100% complete?
 - Routes registered?
 - Controllers returning correct data?
 - Models, migrations, relationships done?
 - Validation rules in place?
 - Authentication/authorization middleware applied?"
```

**If backend is complete:**
Proceed. Your ONLY job is wiring the design to backend data. No backend changes.

**If backend is NOT complete:**
Ask: "Which parts are pending? Should I complete the backend first, or integrate what's ready?"

**If backend is partially done:**
Integrate only the completed parts. Mark incomplete sections with a comment: `{/* Waiting for backend: [feature] */}` -- but NEVER use dummy data as a stand-in.

---

## Phase 0.5: Backend Data Extraction

Before touching ANY design file, read the backend first.

```
MANDATORY STEPS:
1. Find the controller method for this page
2. Read it line by line
3. Extract the EXACT response shape:
   - Every field name (exact casing and spelling)
   - Every data type (string, int, array, object, null)
   - Every nesting level (user.address.city, not just city)
   - Every relationship (user.orders, user.team.name)
   - Pagination structure (data, links, meta, per_page, total)
4. Write down this data map before starting integration
5. This map is your CONTRACT -- the frontend must conform to it
```

**Example data extraction:**
```
Controller: OrderController@index
Returns via Inertia::render('Orders/Index', [...]):

orders (paginated):
  data[]:
    .id (int)
    .reference_number (string)
    .status (enum: draft|pending|processing|shipped|delivered)
    .total_amount (int, cents)
    .customer.id (int)
    .customer.full_name (string)
    .customer.email (string)
    .items_count (int)
    .created_at (ISO string)
  links: { first, last, prev, next }
  meta: { current_page, last_page, per_page, total }

filters:
  .status (string|null)
  .search (string|null)
  .date_from (string|null)
  .date_to (string|null)
```

Now you know EXACTLY what the frontend will receive. No guessing.

---

## Phase 1: Design Folder Discovery

### First time in a project

```
MANDATORY STEPS:
1. Ask the user: "Where is your frontend design folder?"
2. OR search for it:
   - Look for: /designs/, /ui/, /frontend-designs/, /mockups/,
     /templates/, /design-system/, /prototype/
   - Check: resources/js/designs/, resources/views/designs/,
     src/designs/, public/designs/
   - Look for directories with many .jsx/.tsx/.vue files that
     have no route registrations (pure design files)
3. Once found, STORE the path for the entire session.
4. List ALL files recursively to build a complete map.
```

### Every subsequent page

```
1. Recall the stored design folder path
2. List contents if not already mapped
3. Find the matching design file
4. Only then proceed
```

---

## Phase 2: Page Creation Workflow

### Step 1: Check Design Folder (MANDATORY -- EVERY TIME)

```
1. List all files in the design folder and subdirectories
2. Search for a file matching the page you need
3. Search by: name, feature, section, singular/plural, abbreviations
4. You MUST do this. No exceptions. No shortcuts.
```

### Step 2A: Design File Found

```
1. READ the entire design file line by line
2. IDENTIFY all components used (imports, shared components)
3. IDENTIFY all dummy/placeholder data:
   - Hardcoded strings, numbers, dates, currency
   - Hardcoded arrays/objects (mock data)
   - Placeholder images
   - Static counts or IDs
4. COMPARE dummy data against your backend data map (Phase 0.5):
   - Which dummy field maps to which backend field?
   - Are there type mismatches? (design shows string, backend sends int)
   - Are there naming mismatches? (design uses camelCase, backend sends snake_case)
   - Are there structure mismatches? (design expects flat, backend sends nested)
5. COPY the design file to its final location
6. WIRE: Replace every dummy reference with the exact backend prop
7. VERIFY: Zero dummy data remains. Every value comes from backend.
```

### Step 2B: Design File NOT Found

```
STOP. Do NOT create from scratch.

1. List ALL design files to the user
2. Find the MOST SIMILAR existing page:
   - Same section? Same layout type? Same data pattern?
3. TELL the user which file you'll adapt and why
4. COPY the similar page
5. ADAPT minimally: change data bindings, headings, route names
6. Keep EVERYTHING else identical for design uniformity
```

### Step 2C: No Similar Page Exists

```
1. TELL the user: "No design exists for [page] or anything similar."
2. ASK: "Should I create using existing design system components?
   Or will you provide a design first?"
3. WAIT for response. Do NOT proceed without permission.
4. If approved: use ONLY existing components, match visual style exactly.
```

---

## Phase 3: Integration Rules

### The Wiring Process

Integration means connecting the design shell to the backend data. The design provides the visual structure. The backend provides the data. You wire them together.

```
RESPECT BOTH SIDES:

Design side (respect the visual):
- Keep exact component hierarchy
- Keep exact CSS classes
- Keep exact layout structure
- Keep exact spacing and positioning
- Keep exact component imports
- Keep the design's conditional rendering patterns

Backend side (respect the data -- THIS WINS):
- Use exact field names from the controller
- Use exact data types from the controller
- Use exact nesting from the controller
- Use exact pagination structure from the controller
- Handle every field the backend sends
- Handle null/empty states for optional fields
- Format data in the frontend (dates, currency, etc.)
  but NEVER rename or reshape the backend fields
```

### Inertia.js Specifics

```
React + Inertia:
- Type props with exact backend shape: interface Props { orders: PaginatedResponse<Order> }
- Access via usePage<Props>() or page props
- Forms: useForm({ field: '' }) matching backend validation fields exactly
- Navigation: <Link href={route('orders.show', order.id)}>
- Preserve state: router.visit(url, { preserveState: true })

Vue + Inertia:
- defineProps with exact backend shape
- Forms: useForm({ field: '' }) matching backend validation fields
- Navigation: <Link :href="route('orders.show', order.id)">
- Preserve state: router.visit(url, { preserveState: true })
```

### Field Name Resolution

When design field names don't match backend field names:

```
RULE: The backend field name is ALWAYS correct. Adapt the frontend.

Design says: order.totalPrice     Backend sends: order.total_amount
Action: Use order.total_amount in the template. Format it for display.

Design says: user.name            Backend sends: user.first_name + user.last_name
Action: Display {user.first_name} {user.last_name}. Never create a computed "name".

Design says: order.items (array)  Backend sends: order.items (paginated)
Action: Use order.items.data and implement pagination from order.items.meta.

Design says: status (string)      Backend sends: status (enum object with label, color)
Action: Use status.label for display, status.value for logic.
```

### Data Type Handling

```
Money:    Backend sends cents (int 1500) -> Format: (amount / 100).toFixed(2)
Dates:    Backend sends ISO string -> Format with date library or Intl
Booleans: Backend sends true/false -> Drive toggle, badge, icon state
Enums:    Backend sends {value, label, color} -> Use label for text, color for badge
Arrays:   Backend sends collection -> Map over it, handle empty state
Nullable: Backend sends null -> Show fallback text or hide element
Nested:   Backend sends user.team.name -> Access exactly as nested
```

### Null/Empty State Handling (MANDATORY)

Every data point that CAN be null or empty MUST be handled:

```
[ ] Empty lists: Show "No [items] found" message (from design or create one matching style)
[ ] Null strings: Show dash "-" or fallback text, never "null" or "undefined"
[ ] Null images: Show default avatar/placeholder from design system
[ ] Null dates: Show "-" or "Not set"
[ ] Null currency: Show "$0.00" or "-"
[ ] Empty search results: Show empty state from design or matching message
[ ] Zero counts: Show "0" not blank
[ ] Optional relationships: Check existence before accessing nested fields
```

### Permission-Driven Rendering

```
If backend sends permission/role data:
- Hide/show buttons based on permissions: {can.edit && <EditButton />}
- Hide/show sections based on roles: {user.is_admin && <AdminSection />}
- Disable actions based on status: disabled={order.status === 'cancelled'}
- NEVER hide data the backend sent -- only hide actions/controls
```

---

## Phase 4: Verification

After EVERY page integration:

```
BACKEND CONFORMITY:
[ ] Every field from backend data map is used or intentionally excluded
[ ] Field names match backend exactly (no renaming)
[ ] Data types handled correctly (money, dates, enums, booleans)
[ ] Pagination structure matches backend (data, meta, links)
[ ] Null/empty states handled for every nullable field
[ ] Permissions/roles drive UI visibility correctly
[ ] No frontend state duplicating backend data

DESIGN COMPLIANCE:
[ ] Page layout matches original design exactly
[ ] No custom CSS added
[ ] No design components modified
[ ] All design system components used as-is
[ ] Visual structure identical to design file

DATA INTEGRITY:
[ ] Zero dummy data remains
[ ] Zero hardcoded values that should be dynamic
[ ] Search: "Lorem", "Ipsum", "John", "Jane", "example.com",
    "placeholder", "dummy", "test", "sample", "fake", "$99"
[ ] All images from backend media URLs, not placeholder

INTEGRATION:
[ ] Inertia props correctly typed matching backend shape
[ ] Forms use useForm() with fields matching backend validation
[ ] Navigation uses Link/route() helpers
[ ] Error handling for form submissions (validation errors display)
[ ] Flash messages/notifications handled
```

---

## Forbidden Actions

- Creating a page from scratch without checking design folder
- Assuming a design doesn't exist without listing the directory
- Leaving any dummy data in a delivered page
- Modifying design layout or visual structure
- Adding custom CSS or overriding design system styles
- Renaming backend field names to match frontend conventions
- Creating frontend state for data that exists in backend props
- Reshaping backend data structure (flattening nested, merging fields)
- Guessing field names without reading the controller
- Using placeholder data "temporarily" -- there is no temporary
- Skipping null/empty state handling
- Ignoring pagination when backend sends paginated data
- Hardcoding permissions/roles instead of using backend data

---

## Design Folder Path Storage

```
DESIGN_FOLDER: [path discovered from user or search]
BACKEND_CONTROLLERS: [path to controllers, e.g., app/Http/Controllers/]

Store both on first discovery. Use for every page in this project.
```

---

## How to Use

**Start a project integration:**
```
Backend is 100% done. Design folder: /resources/js/designs/
Integrate all pages with Inertia.
```

**Create a specific page:**
```
Create the orders list page. Backend controller is OrderController@index.
```

**When a design is missing:**
```
I need a user profile page but there's no design. Find the closest match.
```

**Verify integration:**
```
Check all pages in the orders section for dummy data and design compliance.
```

**Specify data shape explicitly:**
```
The backend sends: { orders: { data: [...], meta: { total, per_page } }, filters: { status, search } }
Wire this into the orders list design.
```
