
# 🐔 Rinha de Backend 2025 - André Nicoletti

Projeto desenvolvido como solução para o desafio da **Rinha de Backend 2025**. O objetivo é fornecer um serviço robusto, reativo e performático utilizando Java, Spring WebFlux, PostgreSQL e Docker.

---

## 🚀 Tecnologias

- **Java 21** com Spring Boot 3.5.3
- **Spring WebFlux** (Programação Reativa)
- **PostgreSQL 16**
- **Flyway** (para migrações de banco de dados)
- **Docker & Docker Compose**
- **Prometheus** (monitoramento)

---

## ⚙️ Como executar localmente

### 1. Clone o repositório

```bash
git clone https://github.com/andregnicoletti/rinha-de-backend-2025.git
cd rinha-de-backend-2025
```

### 2. Compile a aplicação

```bash
./mvnw clean package -DskipTests
```

### 3. Suba a aplicação usando Docker Compose

```bash
docker-compose up --build
```

- Após execução, a aplicação estará disponível em:  
  [http://localhost:9999](http://localhost:9999)

---

## 🛠️ Estrutura da Aplicação

- **`/payments`** (`POST`): Processa pagamentos, alternando automaticamente entre serviço `default` e `fallback`.
- **`/payments-summary?from=...&to=...`** (`GET`): Retorna um resumo dos pagamentos efetuados por endpoint em determinado período.

Exemplo de resposta:

```json
{
  "default": {
    "endpointType": "default",
    "totalRequests": 142,
    "totalAmount": 54124.55
  },
  "fallback": {
    "endpointType": "fallback",
    "totalRequests": 38,
    "totalAmount": 9421.00
  }
}
```

---

## 🔄 Mecanismo de fallback automático

A aplicação monitora constantemente o serviço de pagamento principal (`default`). Caso detecte falhas (por resposta negativa ou indisponibilidade), automaticamente ativa o serviço de pagamento alternativo (`fallback`).

Esse comportamento é totalmente gerenciado por programação reativa utilizando o WebClient do Spring WebFlux.

---

## 🐳 Docker & Docker Compose

- Dockerfile utiliza `eclipse-temurin:21-jre-alpine` com aplicação exposta na porta `9999`.

```dockerfile
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/rinha-router.jar app.jar

EXPOSE 9999

ENTRYPOINT ["java","-jar","-Dserver.port=9999","app.jar"]
```

- docker-compose.yml:

```yaml
version: "3.9"

services:
  db:
    image: postgres:16
    container_name: rinha-db
    restart: always
    environment:
      POSTGRES_USER: rinha
      POSTGRES_PASSWORD: rinha
      POSTGRES_DB: rinha
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  rinha-router:
    image: annicoletti/rinha-router:latest
    container_name: rinha-router
    ports:
      - "9999:9999"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/rinha
      SPRING_DATASOURCE_USERNAME: rinha
      SPRING_DATASOURCE_PASSWORD: rinha
      PAYMENT_SERVICE_URL_DEFAULT: http://host.docker.internal:8001
      PAYMENT_SERVICE_URL_FALLBACK: http://host.docker.internal:8002
      SERVER_PORT: 9999
    depends_on:
      - db

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

volumes:
  pgdata:
```

---

## ✅ Testes e Validação

Utilize ferramentas como Postman, Newman ou K6 para realizar testes de performance e garantir que a aplicação se comporte corretamente sob diferentes cargas.

---

## 🧑‍💻 Autor

- **André Nicoletti** - [GitHub](https://github.com/andregnicoletti)

---