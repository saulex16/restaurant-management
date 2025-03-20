# Restaurant Management Spring Boot Application
An implementation of a restaurant management system to put OOP design patterns into practice.
This project was developed as part of the **Application or use cases of various design patterns** course at **FH Technikum Wien**.

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
    "tableName": "Table Name",
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


### Orders



### Contributors
- [Lucía Digon](https://github.com/ludigon)
- [Saúl Castañeda](https://github.com/saulex16)
- [Axel Preiti Tasat](https://github.com/AxelPreitiT)