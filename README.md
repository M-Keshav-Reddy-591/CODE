# OrderPulse: Live Order Operations

A runnable Spring Boot and React demonstration of concurrent order processing with database-protected inventory.

## Architecture

- `backend/`: Java 17, Spring Boot 3, Spring Data JPA, validation, Actuator, and MySQL persistence.
- `frontend/`: React + Vite, Recharts, Axios, and a responsive operations dashboard. Material UI is included for extending the component surface.
- Live updates use Server-Sent Events at `GET /api/orders/events`.

## Concurrency strategy

Order intake is submitted to a bounded `ThreadPoolTaskExecutor` with 5 core workers, 10 maximum workers, and a queue capacity of 100. The processing transaction loads the product with `PESSIMISTIC_WRITE`, checks availability, and decrements inventory while the row lock is held. Therefore two workers cannot reserve the same stock, and `availableInventory` cannot become negative. The test `InventoryConcurrencyTest` sends 100 one-unit orders to inventory of 10 and verifies 10 completions and zero remaining inventory.

## Run locally

Requirements: Java 17+, Maven 3.9+, Node 18+.

```powershell
mvn -f backend/pom.xml spring-boot:run
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. The backend connects to local MySQL at `localhost:3306` using database `orders`, user `root`, and no password. The database is created automatically when MySQL allows it. Override `DB_URL`, `DB_USER`, and `DB_PASSWORD` when needed.

## APIs

- `GET/POST /api/products`, `GET/PUT/DELETE /api/products/{id}`
- `GET/POST /api/orders`, `GET /api/orders/{id}`, `POST /api/orders/{id}/retry`
- `GET /api/orders/dead-letter`, `GET /api/orders/events`
- `GET /api/dashboard/summary`, `/api/dashboard/orders`, `/api/dashboard/inventory`
- `POST /api/simulation/orders?numberOfOrders=50`
- `GET /api/health`

Simulation payload: `{ "customerName": "Load test", "productId": 1, "quantity": 1 }`.

## Operational behavior

Products are seeded on first startup: Laptop (10), Keyboard (50), Mouse (100), Monitor (20), and Headphones (30). Orders are created as `PENDING`, processed asynchronously, and transition to `PROCESSING`, `COMPLETED`, or `OUT_OF_STOCK`. The retry model and DLQ endpoints are present for failed processing states; out-of-stock is intentionally terminal because retrying cannot create stock. The dashboard refreshes through SSE whenever an order changes state.
