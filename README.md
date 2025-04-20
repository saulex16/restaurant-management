# Restaurant Management with AI-powered Recipe Suggestions
An extension to the Restaurant Management API that allows to ask a RAG service to suggest recipes to add to the menu. After loading a PDF file with a list of recipes to its vectorial DB, the RAG fetches the available ingredients in the warehouse and offer feasible options.  
This project was developed as part of the **Application or use cases of various design patterns** course at **FH Technikum Wien**.

## Set-up
Requirements:
- Docker Engine
- cURL  


### Docker Compose
First, we need to start the containers for:
* The Restaurant Management Java API
* The vectorial DB for the RAG
* The Recipe Suggester Python API  

In a terminal, run the following command:
```sh
docker compose up --build -d
```

**Make sure the ports 8000 and 8080 are opened in your computer**

### Populate the Restaurant Management API
The _populate\_api.sh_ file creates a restaurant and some ingredients with stock.  

In a terminal, run the following command:
```sh
./populate_api.sh
```

### Populate the vectorial DB
The _multiple\_simple\_recipes.pdf_ file includes multiple recipes, each one of the them indicating the needed ingredients.  
Using a HTTP client (Postman, cURL, etc), upload the file to the vectorial DB through the Python API.  
```sh
curl -X POST "http://localhost:8000/upload/" \
  -F "file=@multiple_simple_recipes.pdf"
```

## Usage
### Chat
Start a conversation with a LLM using the PDF files as its sources.
```sh
curl -X POST "http://localhost:8000/chat/" \
  -H "Content-Type: application/json" \
  -d '{"message": "Your question goes here"}'
```

### Command
Receive recipes that can be added to the menu given the current available ingredients in the warehouse.
```sh
curl -X POST "http://localhost:8000/command/{RESTAURANT_ID}"
```

---

### Contributors
- [Lucía Digon](https://github.com/ludigon)
- [Saúl Castañeda](https://github.com/saulex16)
- [Axel Preiti Tasat](https://github.com/AxelPreitiT)
