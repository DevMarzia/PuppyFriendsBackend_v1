# PuppyFriend Backend - Sistema per la Gestione del Rifugio e Cure Veterinarie

## 🐾 Panoramica del Progetto
Questo progetto è un'applicazione backend sviluppata con Spring Boot per facilitare la gestione di un rifugio per animali e il tracciamento delle visite veterinarie. L'applicazione implementa un sistema di sicurezza basato su JWT, integrazioni con API di terze parti (Cloudinary per le immagini e SMTP per le notifiche email) e una gestione complessa del dominio con tabelle relazionali.

## Tecnologie Utilizzate
* **Java 17 / Spring Boot 3.x**
* **Spring Security & JWT** (Stateless authentication)
* **PostgreSQL** (Database Relazionale)
* **Spring Data JPA**
* **Cloudinary API** (Gestione immagini)
* **Java Mail Sender** (Notifiche SMTP)
* **Maven** (Dependency Management)

## Funzionalità Principali
1.  **Gestione Utenti & Auth:** Registrazione e login basati su JWT. Gestione dei profili utente inclusa l'immagine del profilo tramite integrazione con Cloudinary.
2.  **Sistema di Ruoli:** Implementazione di ruoli distinti (ADOPTER, VOLUNTEER, ADMIN) con diversi livelli di accesso.
3.  **Gestione Animali:** Monitoraggio completo (Taglia, Specie, Stato di salute, Foto).
4.  **Servizio Medico:** Gestione delle visite veterinarie (VetVisit) e delle vaccinazioni (Vaccination) associate a ogni animale.
5.  **Notifiche Email:** Invio automatico di email di benvenuto alla registrazione.

## Struttura del Database
Il modello di dominio è composto da **8 tabelle** interconnesse:
* `users`: Dati personali e credenziali.
* `roles`: Definizione dei permessi.
* `animals`: Anagrafica degli animali.
* `sizes`: Classificazione taglie.
* `medical_records`: Cartella clinica principale.
* `vaccinations`: Registro dei vaccini.
* `vet_visits`: Registro delle visite mediche.
* `adoption_requests`: Gestione delle domande di adozione.
* `users_roles`: Tabella di giunzione per la gestione dei ruoli.

## Istruzioni per l'Avvio

### 1. Prerequisiti
Assicurati di avere installato:
* Java 17 o superiore
* Maven
* PostgreSQL

### 2. Configurazione Variabili d'Ambiente
Per far funzionare l'applicazione, rinomina il file `application.properties.example` in `application.properties`, il file si trova in `src/main/resources` e configura le seguenti variabili d'ambiente:

```properties
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/nome_tuo_db
SPRING_DATASOURCE_USERNAME=tuo_username_db
SPRING_DATASOURCE_PASSWORD=tua_password_db

# JWT Configuration
app.jwt-secret=tua_chiave_segreta_molto_lunga_e_sicura
app.jwt-expiration-milliseconds=86400000

# Cloudinary Configuration (Upload Immagini)
cloudinary.cloud_name=tuo_cloud_name
cloudinary.api_key=tua_api_key
cloudinary.api_secret=tua_api_secret

# Email Configuration (Mailtrap/SMTP)
SPRING_MAIL_HOST=sandbox.smtp.mailtrap.io
SPRING_MAIL_PORT=2525
SPRING_MAIL_USERNAME=tuo_username_mailtrap
SPRING_MAIL_PASSWORD=tua_password_mailtrap
```

###  3. Installazione e Avvio
Esegui i seguenti comandi nel terminale:

1. **Clona il repository**
```
git clone https://github.com/devmarzia/puppyfriendsbackend
```
2. **Entra nella cartella**
```
cd PuppyFriendsBackend
```
3. **Scarica le dipendenze e compila**
```
mvn clean install
```
4. **Avvia l'applicazione**
```
mvn spring-boot:run
```
## Testing con Postman
All'interno del progetto è presente il file PuppyFriend_Postman_Collection.json. 
