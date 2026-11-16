# LabTrack API Usage

LabTrack is a Spring Boot REST API for managing campus laboratory assets.

## Available Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | / | View service information |
| GET | /health | Check application health |
| GET | /api/assets | List laboratory assets |
| POST | /api/assets | Create an asset |
| PUT | /api/assets/{id} | Update an asset |
| DELETE | /api/assets/{id} | Delete an asset |

## Running the Application

On Windows, use the Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

## Health Verification

Open the `/health` endpoint on the running application.

Example response:

```json
{"status":true}
```

A `true` status indicates that the health endpoint is responding successfully.

## Storage

The current application uses in-memory asset storage. Data is not persisted after the application restarts.