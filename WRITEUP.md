# Employee REST API - Design Writeup

**Samyuktha Syamsundar**

## What I Built

Three REST endpoints for an employee management API that will interface with the Employees-R-US SaaS platform:

- `GET /api/v1/employee` - returns all employees
- `GET /api/v1/employee/{uuid}` - returns a single employee by UUID
- `POST /api/v1/employee` - creates a new employee

## Design Decisions

**Controller-Service separation.** The prompt explicitly asked for a service layer, so I split concerns cleanly: the controller handles HTTP mapping and validation, the service owns the business logic and data access. This keeps each layer testable on its own and makes it straightforward to swap in a real persistence layer later.

**`EmployeeEntity` with Lombok.** The project already had Lombok as a dependency, so I used `@Data` and `@Builder` to implement the `Employee` interface without boilerplate. Builder pattern keeps object construction readable, especially when there are 10+ fields.

**`ConcurrentHashMap` for in-memory storage.** Since no persistence layer was required, I went with a thread-safe map keyed by UUID. It's simple, handles concurrent requests out of the box, and the seeded mock data makes the API immediately usable without any setup.

**Dedicated request DTO with Jakarta Validation.** Instead of accepting a raw `Object` on the create endpoint, I created `CreateEmployeeRequest` with `@NotBlank`, `@Email`, `@Min`, etc. This way invalid input gets rejected at the controller boundary with a 400 before it ever reaches the service. Keeps error handling clean and predictable.

**Proper HTTP semantics.** `GET` for retrieval, `POST` for creation. The create endpoint returns `201 Created` instead of the default `200`. A missing UUID returns `404 Not Found`. These are small details but they matter for a production-quality API.

## How I Tested

Ran the app with `./gradlew bootRun` and hit each endpoint with `curl`:

```bash
# All employees - returns 3 seeded records
curl http://localhost:8080/api/v1/employee

# Single employee - grab a UUID from the list above
curl http://localhost:8080/api/v1/employee/{uuid}

# Create - returns 201 with the new employee and a generated UUID
curl -X POST http://localhost:8080/api/v1/employee \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Williams","salary":90000,"age":32,"jobTitle":"Tech Lead","email":"alice.williams@company.com","contractHireDate":"2024-01-10T00:00:00Z"}'

# 404 on a nonexistent UUID
curl http://localhost:8080/api/v1/employee/00000000-0000-0000-0000-000000000000

# 400 on invalid input (empty body, missing fields, etc.)
curl -X POST http://localhost:8080/api/v1/employee \
  -H "Content-Type: application/json" -d '{"firstName":""}'
```

All endpoints returned the expected status codes and response bodies. Also ran `./gradlew build` to confirm the project compiles and passes Spotless formatting checks.

## My Take

I kept things minimal on purpose. No custom exception handlers, no pagination, no HATEOAS links - just the three endpoints the problem asked for, built cleanly enough that any of those things could be added without restructuring. The interface-based service layer is the one piece of "future-proofing" I included because it was explicitly requested and it genuinely makes the code easier to extend.
