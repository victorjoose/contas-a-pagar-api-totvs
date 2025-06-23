# 💸 Contas a Pagar API - Desafio Backend TOTVS 2024

API REST desenvolvida como parte do **Desafio Backend TOTVS 2024**. O sistema permite gerenciar contas a pagar com funcionalidades completas de autenticação via JWT, cadastro e atualização de contas, alteração de situação, importação de dados via CSV e listagem paginada com filtros.

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2+
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Flyway (migrações)
- Docker & Docker Compose
- Swagger/OpenAPI

---

## 📦 Como Executar Localmente

### Pré-requisitos
- Java 17
- Docker + Docker Compose

### Passo a passo

```bash
# Subir toda a aplicação e banco de dados
docker-compose up --build
```

A aplicação estará disponível em `http://localhost:8080`.

---

## 🔐 Autenticação

Autenticação via **JWT**. Siga o fluxo abaixo:

1. Registre um novo usuário
2. Faça login para obter o token
3. Utilize o token nas requisições protegidas

### Headers obrigatórios:
```
Authorization: Bearer {seu_token}
```

### Endpoints de autenticação

| Método | Endpoint              | Descrição                      |
|--------|-----------------------|--------------------------------|
| POST   | /auth/register        | Cadastro de novo usuário       |
| POST   | /auth/login           | Login e geração de token JWT   |

---

## 📑 Endpoints de Contas

| Método | Endpoint                      | Descrição                               |
|--------|-------------------------------|-----------------------------------------|
| POST   | /api/contas                   | Criação de nova conta                   |
| PUT    | /api/contas/{id}              | Atualização completa da conta           |
| PATCH  | /api/contas/{id}/situacao     | Alterar apenas a situação da conta      |
| GET    | /api/contas                   | Listagem paginada e com filtros         |
| GET    | /api/contas/{id}              | Buscar uma conta por ID                 |
| GET    | /api/contas/valor-total       | Somatório do valor pago por período     |
| POST   | /api/contas/importar          | Importar contas via arquivo CSV         |

---

## 📄 Importação via CSV

### Formato esperado:
```csv
nome,valor,vencimento,descricao,situacao
Conta de Luz,200.00,2025-07-10,Luz mensal,PAGA
```

- Endpoint: `POST /api/contas/importar`
- Content-Type: `multipart/form-data`

---

## 📃 Documentação Swagger

Acesse a documentação da API via Swagger UI:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🧪 Testes

Os testes unitários cobrem:

- Autenticação (login e registro)
- Serviços de contas e regras de negócio
- Importação de CSV
- Validação e segurança

Para executar:

```bash
./mvnw test
```

---

## 🧠 Organização do Projeto (DDD)

```
src/main/java/com/josefarias/contasapagar
├── application
│   ├── dto
│   └── service
├── controller
├── domain
│   ├── entity
│   ├── enums
│   └── repository
├── infrastructure
│   └── config
└── ContasapagarApplication.java
```

---

## 📬 Coleção Postman

Uma coleção de requisições está disponível para facilitar os testes com a API.

🗂 Caminho: `./postman/contasapagar.postman_collection.json`

Importe no Postman e utilize os exemplos de login, registro, contas e importação.

---

## 👨‍💻 Autor

Desenvolvido por **José Farias** para o **Desafio Backend TOTVS 2024**.
