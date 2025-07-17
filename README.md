# Rinha de Backend - API Router (Spring Boot)

Este projeto foi desenvolvido para a competição Rinha de Backend, utilizando Spring Boot, Postgres, Redis e Nginx, tudo orquestrado com Docker Compose.

## Arquitetura

- **API**: 2 instâncias do serviço principal em Java (Spring Boot)
- **Banco de Dados**: Postgres 16
- **Cache**: Redis
- **Proxy**: Nginx

## Como executar

1. **Clonar o repositório:**
    ```bash
    git clone https://github.com/andregnicoletti/rinha-router.git
    cd rinha-router
    ```

2. **Configurar variáveis de ambiente:**

   Crie um arquivo `.env` com as variáveis necessárias, exemplo:
    ```env
    POSTGRES_USER=postgres
    POSTGRES_PASSWORD=postgres
    POSTGRES_DB=rinha
    SPRING_REDIS_HOST=redis-service
    SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/rinha
    ```

3. **Subir os containers:**
    ```bash
    docker compose up --build -d
    ```

4. **Acesse a API:**
  - `http://localhost:9999` (via Nginx)

## Limites de recursos

Conforme exigido na competição:

- **CPU Total:** 1.5 unidades
- **Memória Total:** 350MB

Os limites estão definidos no arquivo `docker-compose.yml`.

## Observações

- Recomenda-se ajustar os parâmetros de memória e heap nas aplicações Java conforme necessário para não exceder os limites.
- O banco e o Redis estão com configurações mínimas, caso haja necessidade de mais recursos, adapte as instâncias de API.
- O proxy Nginx faz balanceamento simples entre as duas instâncias da API.

## Contato

André Nicoletti  
[LinkedIn](https://www.linkedin.com/in/andre-nicoletti/)  
[Email](mailto:andre.nicoletti.dev@gmail.com)

---

**Bons benchmarks! 🚀**