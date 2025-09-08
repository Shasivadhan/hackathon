# KAKARA — Few API

**Endpoints**
- `POST /api/products` → create product (id, price, carbonPerUnit)
- `POST /api/cart/items` → add to cart (header **X-User-Id**)
- `POST /api/cart/apply-code` → apply discount coode
- `POST /api/orders/preview` → subtotal, discsount, total, **totalCarbon**
- `GET /api/cart` → view cart

**Run**
```
mvn spring-boot:run
# Swagger: http://localhost:8080/swagger-ui.html
```
**Mongo**: `mongodb://localhost:27017/ecomdb`

**Promo creation** (via Swagger):
```
POST /api/promos
{
  "code":"OFFER10","type":"PERCENT","value":10,
  "minOrderAmount":0,"startDate":"2025-01-01T00:00:00Z","endDate":"2025-12-31T23:59:59Z","active":true
}
```
Generated: 2025-09-08T05:52:31.908924Z
