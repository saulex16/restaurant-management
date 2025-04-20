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

  # Define cantidades para cada ingrediente
  declare -A INGREDIENTS_MAP
  INGREDIENTS_MAP["Eggs"]=12
  INGREDIENTS_MAP["Milk"]=8
  INGREDIENTS_MAP["Salt"]=5
  INGREDIENTS_MAP["Butter"]=4
  INGREDIENTS_MAP["Oil"]=6
  INGREDIENTS_MAP["Water"]=20

  declare -A INGREDIENT_IDS

  for NAME in "${!INGREDIENTS_MAP[@]}"; do
    # Precios definidos para los nuevos ingredientes
    PRICE=$(awk -v name="$NAME" '
      BEGIN {
        prices["Eggs"] = 0.2;
        prices["Milk"] = 0.6;
        prices["Salt"] = 0.1;
        prices["Butter"] = 1.5;
        prices["Oil"] = 1.2;
        prices["Water"] = 0.05;
        print prices[name];
      }')

    echo "Creando ingrediente $NAME (precio: $PRICE)..."

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
