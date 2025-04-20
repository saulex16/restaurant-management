#!/bin/bash

# Crear restaurante, warehouse, ingredientes y stock

echo "Creando restaurante..."

RESPONSE=$(curl -s -X POST http://localhost:8080/restaurants/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "La Trattoria",
    "managerName": "Giovanni Rossi",
    "vipTablePrice": 120.5,
    "kitchenLimit": 25,
    "waiterNames": ["Marco", "Luca", "Giulia"]
  }')

RESTAURANT_ID=$(echo $RESPONSE | grep -oE '[0-9]+')

echo "Restaurante creado con ID: $RESTAURANT_ID"

if [[ -n "$RESTAURANT_ID" ]]; then
  echo "Creando warehouse..."
  WAREHOUSE_RESPONSE=$(curl -s -X POST http://localhost:8080/restaurants/$RESTAURANT_ID/warehouse)
  echo "Respuesta warehouse: $WAREHOUSE_RESPONSE"

  # Extraer warehouseId desde JSON, suponemos que viene como {"id":123,...}
  WAREHOUSE_ID=$(echo $WAREHOUSE_RESPONSE | grep -oP '"id"\s*:\s*\K\d+')
  echo "Warehouse creado con ID: $WAREHOUSE_ID"

  echo "Agregando ingredientes..."

  declare -A INGREDIENTS_MAP
  INGREDIENTS_MAP["Tomato"]=10
  INGREDIENTS_MAP["Mozzarella"]=5
  INGREDIENTS_MAP["Basil"]=3
  INGREDIENTS_MAP["Olive Oil"]=4
  INGREDIENTS_MAP["Flour"]=6

  declare -A INGREDIENT_IDS

  for NAME in "${!INGREDIENTS_MAP[@]}"; do
    PRICE=$(awk -v name="$NAME" '
      BEGIN {
        prices["Tomato"] = 0.5;
        prices["Mozzarella"] = 1.2;
        prices["Basil"] = 0.3;
        prices["Olive Oil"] = 1.0;
        prices["Flour"] = 0.8;
        print prices[name];
      }')

    echo "Creando ingrediente $NAME..."

    INGREDIENT_RESPONSE=$(curl -s -X POST http://localhost:8080/ingredients \
      -H "Content-Type: application/json" \
      -d "{\"name\": \"$NAME\", \"price\": $PRICE}")

    echo "Respuesta: $INGREDIENT_RESPONSE"

    INGREDIENT_ID=$(echo $INGREDIENT_RESPONSE | grep -oP '"id"\s*:\s*\K\d+')
    INGREDIENT_IDS[$NAME]=$INGREDIENT_ID

    echo "✔️ $NAME creado con ID: $INGREDIENT_ID"
  done

  echo "📦 Agregando stock al warehouse..."

  for NAME in "${!INGREDIENT_IDS[@]}"; do
    INGREDIENT_ID=${INGREDIENT_IDS[$NAME]}
    QUANTITY=${INGREDIENTS_MAP[$NAME]}

    echo "Añadiendo $QUANTITY unidades de $NAME (ID: $INGREDIENT_ID) al warehouse..."

    curl -s -X POST http://localhost:8080/restaurants/$RESTAURANT_ID/warehouse/$WAREHOUSE_ID/stock \
      -H "Content-Type: application/json" \
      -d "{\"ingredient\": $INGREDIENT_ID, \"quantity\": $QUANTITY}"

    echo "✔️ Stock cargado para $NAME"
  done

  echo "🎉 Proceso completo."

else
  echo "❌ Error: No se pudo obtener el ID del restaurante."
fi
