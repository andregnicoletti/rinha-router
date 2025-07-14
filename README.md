
# 🐔 Rinha de Backend 2025 - [André Nicoletti](https://github.com/andregnicoletti)

Projeto desenvolvido como solução para o desafio da **Rinha de Backend 2025**. O objetivo é fornecer um serviço robusto, reativo e performático utilizando **Java 21**, **Spring WebFlux**, **PostgreSQL** e **Docker**.

---

## 🚀 Tecnologias

- **Java 21** (Temurin)
- **Spring Boot 3.5.3**
  - Spring WebFlux (Reativo)
  - Spring Data JPA
  - Flyway (migração de banco)
- **PostgreSQL 16**
- **Docker & Docker Compose**
- **Prometheus** (monitoramento via Actuator)

---

## ⚙️ Como executar localmente

### 1. Clone o projeto

```bash
git clone https://github.com/andregnicoletti/rinha-router.git
cd rinha-router
```

### 2. Compile o projeto

```bash
./mvnw clean package -DskipTests
```

### 3. Suba a stack Docker

```bash
docker compose up -d
```

✅ **Atenção**: a rede Docker `payment-processor` deve estar criada, caso contrário utilize:
```bash
docker network create payment-processor
```

---

## Endpoints principais

- **`POST /payments`**: Processa pagamentos com fallback automático.
- **`GET /payments-summary?from=...&to=...`**: Retorna resumo de pagamentos por tipo (`default` e `fallback`).

---

## 🐳 Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/rinha-router.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java","-jar","app.jar"]
```

### docker-compose.yml

> Exemplo completo no repositório. Principais serviços:
> - Banco `PostgreSQL 16`
> - Serviço backend `rinha-router`
> - Monitoramento `Prometheus`
> - Exposição na porta `9999`

---

## ✅ Testes Sugeridos

Ferramentas recomendadas:
- Postman / Newman
- K6 para testes de carga

---

## 👨‍💻 Autor

**André Nicoletti**  
GitHub: [https://github.com/andregnicoletti](https://github.com/andregnicoletti)