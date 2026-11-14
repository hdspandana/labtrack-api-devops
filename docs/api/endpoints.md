# LabTrack API Endpoint Reference

Base URL: http://localhost:8080

## Health Check

GET /health

Checks whether the application is running.

Expected response:

{
  "status": "UP",
  "service": "labtrack-api"
}

## List Assets

GET /api/assets

Returns all registered laboratory assets.

## Create Asset

POST /api/assets

Creates a new laboratory asset.

Content-Type: application/json

## Update Asset

PUT /api/assets/{id}

Updates an existing asset using its ID.

## Delete Asset

DELETE /api/assets/{id}

Deletes an asset using its ID.


