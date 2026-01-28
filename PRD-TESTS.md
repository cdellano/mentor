## Tests
Genera tests JUnit 5 para TODOS los controladores rest.

Contexto:
- Spring Boot
- API REST
- Validaciones con @Valid
- Respuestas JSON
- Ya existe un GlobalExceptionHandler


Códigos de estado HTTP a cubrir:
- 200 OK
- 201 Created
- 204 No Content
- 400 Bad Request
- 401 Unauthorized
- 403 Forbidden
- 404 Not Found
- 409 Conflict
- 500 Internal Server Error

Usar:
- MockMvc
- jsonPath
- @SpringBootTest
- @AutoConfigureMockMvc