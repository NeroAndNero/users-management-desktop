# users-menagement-desktop-hezagonal-ddd
Ejemplo de una aplicación Desktop que hace CRUDL y control de acceso de Usuarios, escrita en Java,  arquitectura Hexagonal y enfoque DDD, usando buenas practicas y Clean code
Este es un ejemplo de Proyecto Java con hexagonal para los alumnos de UdeC

## Project (CEA) — CRUDL con arquitectura hexagonal

Se añadió la entidad `Project` con CRUDL completo (listar, buscar, crear, actualizar, eliminar)
siguiendo la misma estructura que `User`. Persistencia: MySQL con JDBC (`ProjectRepositoryMySQL`);
existe además un adaptador en memoria (`ProjectRepositoryMemory`). Base de datos de prueba:
`database/backup_crud_usuarios.sql`.
Opciones del menú: 7 (listar), 8 (buscar), 9 (crear), 10 (actualizar), 11 (eliminar).