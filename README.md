# Sistema de Autenticação REST com JWT e Filas

## 📋 Descrição

API REST desenvolvida em Spring Boot que implementa um sistema completo de autenticação e autorização utilizando JWT (JSON Web Tokens). O sistema permite registro e login de usuários, com envio assíncrono de emails através de filas (BlockingQueue) e integração com Mailtrap para desenvolvimento.

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security**
- **Spring Data JPA**
- **JWT**
- **H2 Database** (em memória)
- **Spring Mail** + **Mailtrap**
- **BlockingQueue** (processamento assíncrono)
- **Lombok**
- **SpringDoc OpenAPI 3** (Swagger)
- **Maven**

## ⚙️ Funcionalidades

- ✅ Registro de usuários
- ✅ Autenticação com JWT
- ✅ Autorização baseada em roles (USER/ADMIN)
- ✅ Envio de emails assíncrono com filas
- ✅ Processamento em background com BlockingQueue
- ✅ Integração com Mailtrap para desenvolvimento
- ✅ Documentação automática da API (Swagger)
- ✅ Console H2 para desenvolvimento

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/roadmap/fase1/
│   │   ├── config/           # Configurações (Security, JWT, Queue, OpenAPI)
│   │   ├── controller/       # Controllers REST
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Repositories
│   │   └── service/         # Serviços de negócio
│   └── resources/
│       ├── application.properties
│       ├── static/
│       └── templates/
└── test/
```

## 🔧 Configuração do Ambiente

### Pré-requisitos

- Java 17+
- Maven 3.6+
- Git
- Conta no Mailtrap (para desenvolvimento)

### Variáveis de Ambiente

Configure as seguintes variáveis de ambiente:

```bash
export TOKEN_SECRET=seu_token_secreto_jwt_super_seguro
```

## 🏃‍♂️ Como Executar

### 1. Clone o repositório
```bash
git clone <https://github.com/dudscode/rest-jwt-oauth-filas>
```

### 2. Configure as variáveis de ambiente
```bash
# No macOS/Linux
export TOKEN_SECRET=meu_token_super_secreto_jwt_muito_seguro
```

### 3. Configure o Mailtrap (Opcional - para envio de emails)
1. Crie uma conta gratuita em [Mailtrap.io](https://mailtrap.io)
2. Acesse sua inbox de desenvolvimento
3. Copie as credenciais SMTP
4. Atualize o `application.properties` com suas credenciais

### 5. Execute a aplicação
```bash
# Usando Maven
./mvnw spring-boot:run

# Ou compile e execute o JAR
./mvnw clean package
java -jar target/fase1-0.0.1-SNAPSHOT.jar
```

### 6. Acesse a aplicação
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

## 📚 API Endpoints

### Autenticação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/login` | Login com email/senha |
| POST | `/auth/register` | Registro de novo usuário |

### Usuários

| Método | Endpoint | Descrição | Permissão |
|--------|----------|-----------|-----------|
| GET | `/users/all` | Listar todos usuários | ADMIN 

## 📄 Documentação da API (Swagger)

A documentação completa da API está disponível através do Swagger UI:


## 🔐 Autenticação e Autorização

### JWT Token

O sistema utiliza JWT para autenticação. Após o login, inclua o token no header:

```bash
Authorization: Bearer {seu_jwt_token}
```

### Roles de Usuário

- **USER**: Acesso básico aos próprios dados
- **ADMIN**: Acesso completo ao sistema

### Exemplo de Uso

```bash
# Registro (dispara email automaticamente via fila)
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@exemplo.com",
    "password": "minhasenha",
    "phone": "11999999999",
    "role": "USER"
  }'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@exemplo.com",
    "password": "minhasenha"
  }'

# Usar token retornado
curl -X GET http://localhost:8080/users \
  -H "Authorization: Bearer {token_recebido}"
```

## 📧 Sistema de Emails com Mailtrap e BlockingQueue

### Como Funciona o Sistema de Filas

A aplicação implementa um sistema assíncrono de envio de emails utilizando **BlockingQueue** para melhorar a performance e experiência do usuário:

#### 🔄 Fluxo do Processo

1. **Registro do Usuário**: Quando um usuário se registra via `/auth/register`
2. **Enfileiramento**: O usuário é adicionado à fila `BlockingQueue<User>` 
3. **Processamento Assíncrono**: Uma thread em background processa a fila continuamente
4. **Envio de Email**: Para cada usuário na fila, um email de boas-vindas é enviado
5. **Logs**: O sistema registra logs do envio dos emails

#### ⚡ Vantagens do Sistema de Filas

- **Performance**: Registro do usuário não bloqueia esperando envio do email
- **Confiabilidade**: Emails são processados mesmo se houver falhas temporárias
- **Escalabilidade**: Fila gerencia múltiplos usuários simultaneamente
- **Separação de Responsabilidades**: Registro e envio de email são processos independentes

### 📮 Configuração do Mailtrap

O **Mailtrap** é uma ferramenta para testar emails em desenvolvimento sem enviar emails reais.

#### 🚀 Setup do Mailtrap

1. **Crie uma conta gratuita**: [Mailtrap.io](https://mailtrap.io)
2. **Acesse Email Testing**: No dashboard, vá para "Email Testing"
3. **Crie uma Inbox**: Clique em "Add Inbox" 
4. **Copie as credenciais SMTP**: Na aba "SMTP Settings"

#### ⚙️ Configuração no application.properties

```properties
# MAILTRAP - Configurações SMTP
spring.mail.host=smtp.mailtrap.io
spring.mail.port=587
spring.mail.username=seu_username_mailtrap
spring.mail.password=sua_senha_mailtrap
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.from=noreply@seuapp.com
```

## 🗄️ Banco de Dados

### H2 Database

- **Modo**: Em memória (para desenvolvimento)
- **Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (vazio)

### Modelo de Dados

```sql
-- Usuários
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    login VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);
```


## 🔧 Comandos Úteis

```bash
# Compilar
./mvnw compile

# Executar testes
./mvnw test

# Gerar JAR
./mvnw clean package

# Executar com profile específico
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

