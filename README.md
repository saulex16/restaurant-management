# Restaurant Management Spring Boot Application
An implementation of a restaurant management system to put OOP design patterns into practice.
This project was developed as part of the **Application or use cases of various design patterns** course at **FH Technikum Wien**.

## Build Project
The project requires `Java 21` version.

Inside the project directory run `mvn clean package` to build the project.

## Run Project
Inside the project directory run `java -jar target/restaurant-management-0.0.1-SNAPSHOT.jar`
## Endpoints
### Restaurants
#### Create a new restaurant
_Method_: **POST** `/restaurants`  
_Body_:
```json
{
    "name": "Restaurant Name",
    "managerName": "Manager Name",
    "kitchenLimit": "The maximum number of orders that the kitchen can handle at the same time",
    "waiterNames": "List of waiter names",
    "vipTablePrice": "The price of a VIP table (optional)"
}
```
_Response_: The id of the created restaurant

#### Get restaurant by id
_Method_: **GET** `/restaurants/{id}`  
_Response_: The restaurant with the given id

#### Add a waiter to a restaurant
_Method_: **POST** `/restaurants/{id}/waiters/{name}`  
_Response_: The id of the created waiter

#### Set the price of a VIP table
_Method_: **POST** `/restaurants/{id}/tables/price`  
_Content-Type_: application/vnd.table.vip.v1+json  
_Body_:
```json
{
    "vipTablePrice": "The price of a VIP table"
}
```

#### Create a basic table
_Method_: **POST** `/restaurants/{id}/tables`    
_Content-Type_: application/vnd.table.basic.v1+json  
_Body_:
```json
{
    "tableName": "Table Name"
}
```
_Response:_ The id of the created table

#### Create a VIP table
_Method_: **POST** `/restaurants/{id}/tables`  
_Content-Type_: application/vnd.table.vip.v1+json  
_Body_:
```json
{
    "tableName": "Table Name"
}
```
_Response:_ The id of the created table

### Ingredients

#### Create a new ingredient

_Method_: **POST** `/ingredients`\
_Body_:

```json
{
    "name": "The name of the ingredient",
    "price": "The cost of the ingredient"
}
```

_Response_:

```json
{
    "id": "The unique identifier of the ingredient",
    "name": "The name of the ingredient",
    "price": "The cost of the ingredient"
}
```

####  Get ingredient by id

_Method_: **GET** `/ingredients/{id}`\
_Response_:

```json
{
    "id": "The unique identifier of the ingredient",
    "name": "The name of the ingredient",
    "price": "The cost of the ingredient"
}
```

_Response if not found_: `404 Not Found`

#### Update an ingredient

_Method_: **PUT** `/ingredients/{id}`\
_Body_:

```json
{
    "name": "The updated name of the ingredient",
    "price": "The updated cost of the ingredient"
}
```

_Response_:

```json
{
    "id": "The unique identifier of the ingredient",
    "name": "The updated name of the ingredient",
    "price": "The updated cost of the ingredient"
}
```

_Response if not found_: `404 Not Found`

#### Delete an ingredient

_Method_: **DELETE** `/ingredients/{id}`\
_Response_: `204 No Content`\
_Response if not found_: `404 Not Found`



### Dishes

#### Create a dish

_Method_: **POST** `/restaurants/{restaurantId}/dishes`  
_Body_:
```json
{
    "name": "The name of the dish",
    "baseIngredientsIds": "List of IDs of base ingredients required for the dish",
    "optionalIngredientsIds": "List of IDs of optional ingredients that can be added",
    "durationInMinutes": "The preparation time of the dish in minutes",
    "markup": "The profit margin applied to the dish price"
}
```
_Response_:
```json
{
    "name": "The name of the dish",
    "baseIngredientsIds": "List of IDs of base ingredients required for the dish",
    "optionalIngredientsIds": "List of IDs of optional ingredients that can be added",
    "durationInMinutes": "The preparation time of the dish in minutes",
    "markup": "The profit margin applied to the dish price"
}
```

#### Get all dishes

_Method_: **GET** `/restaurants/{restaurantId}/dishes`  
_Response_:
```json
[
    {
        "name": "The name of the dish",
        "baseIngredientsIds": "List of IDs of base ingredients required for the dish",
        "optionalIngredientsIds": "List of IDs of optional ingredients that can be added",
        "durationInMinutes": "The preparation time of the dish in minutes",
        "markup": "The profit margin applied to the dish price"
    }
]
```
_Response if not found_: `404 Not Found`

#### Get a dish by id

_Method_: **GET** `/restaurants/{restaurantId}/dishes/{id}`  
_Response_:
```json
{
    "name": "The name of the dish",
    "baseIngredientsIds": "List of IDs of base ingredients required for the dish",
    "optionalIngredientsIds": "List of IDs of optional ingredients that can be added",
    "durationInMinutes": "The preparation time of the dish in minutes",
    "markup": "The profit margin applied to the dish price"
}
```
_Response if not found_: `404 Not Found`

#### Update a dish

_Method_: **PUT** `/restaurants/{restaurantId}/dishes/{id}`  
_Body_:
```json
{
    "name": "The updated name of the dish",
    "baseIngredientsIds": "Updated list of base ingredient IDs",
    "optionalIngredientsIds": "Updated list of optional ingredient IDs",
    "durationInMinutes": "Updated preparation time in minutes",
    "markup": "Updated profit margin applied to the dish price"
}
```
_Response_:
```json
{
    "name": "The updated name of the dish",
    "baseIngredientsIds": "Updated list of base ingredient IDs",
    "optionalIngredientsIds": "Updated list of optional ingredient IDs",
    "durationInMinutes": "Updated preparation time in minutes",
    "markup": "Updated profit margin applied to the dish price"
}
```
_Response if not found_: `404 Not Found`

#### Delete a dish

_Method_: **DELETE** `/restaurants/{restaurantId}/dishes/{id}`  
_Response_: `204 No Content`  
_Response if not found_: `404 Not Found`

### Warehouse
#### Create a warehouse

_Method_: **POST** `/restaurants/{restaurantId}/warehouse`  
_Response_:
```json
{
    "id": "The unique identifier of the warehouse",
    "stock": "List of stock item IDs stored in the warehouse",
    "restaurant": "The restaurant associated with this warehouse"
}
```

#### Get a warehouse by id

_Method_: **GET** `/restaurants/{restaurantId}/warehouse/{id}`  
_Response_:
```json
{
    "id": "The unique identifier of the warehouse",
    "stock": "List of stock item IDs stored in the warehouse",
    "restaurant": "The restaurant associated with this warehouse"
}
```
_Response if not found_: `404 Not Found`

#### Create stock in a warehouse

_Method_: **POST** `/restaurants/{restaurantId}/warehouse/{warehouseId}/stock`  
_Body_:
```json
{
    "ingredient": "The unique identifier of the ingredient",
    "quantity": "The amount of this ingredient available in stock",
    "warehouse": "The unique identifier of the warehouse where this stock is stored"
}
```
_Response_:
```json
{
    "id": "The unique identifier of the stock item",
    "ingredient": "The unique identifier of the ingredient",
    "quantity": "The amount of this ingredient available in stock",
    "warehouse": "The unique identifier of the warehouse where this stock is stored"
}
```
_Response if not found_: `400 Bad Request` if the ingredient does not exist

#### Get stock by id

_Method_: **GET** `/restaurants/{restaurantId}/warehouse/{warehouseId}/stock/{stockId}`  
_Response_:
```json
{
    "id": "The unique identifier of the stock item",
    "ingredient": "The unique identifier of the ingredient",
    "quantity": "The amount of this ingredient available in stock",
    "warehouse": "The unique identifier of the warehouse where this stock is stored"
}
```
_Response if not found_: `404 Not Found`

#### Add stock to an existing ingredient

_Method_: **PUT** `/restaurants/{restaurantId}/warehouse/{warehouseId}/stock/{stockId}`  
_Body_:
```json
{
    "quantity": "The amount to add to the current stock"
}
```
_Response_:
```json
{
    "id": "The unique identifier of the stock item",
    "ingredient": "The unique identifier of the ingredient",
    "quantity": "The updated amount of this ingredient available in stock",
    "warehouse": "The unique identifier of the warehouse where this stock is stored"
}
```
_Response if not found_: `404 Not Found`



### Orders
#### Create an order

_Method_: **POST** `/orders`
_Body_:
```json
{
    "restaurantId": "The unique identifier of the restaurant",
    "tableId": "The unique identifier of the table",
    "waiterId": "The unique identifier of the waiter"
}
```
_Response_: The id of the created order

#### Get an order by id

_Method_: **GET** `/orders/{id}`
_Response_: The order with the given id

#### Add a dish to an order
_Method_: **PUT** `/orders/{id}/dishes`
_Body_:
```json
{
    "dishId": "The unique identifier of the dish",
    "addedIngredientsIds": "List of IDs of optional ingredients added to the dish"
}
```
_Response_: The id of the created order item

#### Queue an order

_Method_: **PUT** `/orders/{id}`
_Content-Type_: application/vnd.order.queue.v1+json

#### Get the bill of an order

_Method_: **GET** `/orders/{id}`
_Response_: The bill of the order with the given id

### Contributors
- [Lucía Digon](https://github.com/ludigon)
- [Saúl Castañeda](https://github.com/saulex16)
- [Axel Preiti Tasat](https://github.com/AxelPreitiT)