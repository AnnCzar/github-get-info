# GitHub Repository Info API

Spring Boot aplikacja do pobierania informacji o repozytoriach GitHub użytkownika wraz z ich branchami.

## Opis

Aplikacja pobiera listę **nie-forkowanych** repozytoriów dla danego użytkownika GitHub oraz informacje o branchach każdego repozytorium (nazwa brancha i SHA ostatniego commita).

## Funkcjonalności

-  Pobieranie repozytoriów użytkownika z GitHub API
-  Automatyczne filtrowanie forków
-  Pobieranie informacji o branchach dla każdego repozytorium
-  Obsługa błędów (użytkownik nie istnieje, rate limit)

## Technologie

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Web MVC**
- **Gson** - parsowanie JSON
- **Maven** - zarządzanie zależnościami

## Wymagania

- Java 21+
- Maven 3.6+
- Połączenie internetowe (do GitHub API)

## Instalacja i uruchomienie

### 1. Klonowanie repozytorium
```aiignore
git clone https://github.com/AnnCzar/github-get-info.git
cd github-get-info
```

### 3. Uruchomienie
Windows (PowerShell/CMD)
```aiignore
.\mvnw spring-boot:run
```
macOS/Linux
```aiignore
./mvnw spring-boot:run
```

Aplikacja uruchomi się na porcie **8080**.

### Test
```aiignore
curl http://localhost:8080/getInfo/octocat
```
