# SimpleChat

⚠️ Warning: contains some messy code 😅

SimpleChat is a tiny sample chat app using **Ktor** and **Compose Multiplatform**.

## Project structure

* `composeApp` – client app with Compose
* `shared` – shared models and data classes
* `server` – Ktor server

## Run server

```bash
./gradlew :server:run
```

Server will start on the default Ktor port and handle messages.