# Architecture — LabTrack API

## 1. Split-Environment Overview

This project is designed to operate across two environments:

- **Windows 11 Host**: Primary development, Maven builds, Jenkins CI agent, Docker Desktop, and Minikube/kubectl.
- **Kali Linux VirtualBox VM**: Ansible control/target node (`localhost`) managing `/opt/labtrack` and the `labtrack.service` systemd unit.

## 2. Application Architecture

- **Controller (`AssetController`)**: Exposes REST endpoints (`/`, `/health`, `/api/assets`, `/api/assets/{id}`) and handles `MethodArgumentNotValidException` with HTTP 400 JSON responses.
- **Service (`AssetService`)**: Manages an in-memory `ConcurrentHashMap<Long, LabAsset>` with `AtomicLong` ID generation and 3 pre-seeded campus lab assets.
- **Model (`LabAsset`)**: POJO with Jakarta Bean Validation (`@NotBlank`, `@Size`).
- **Configuration (`application.properties`)**: Reads `SERVER_PORT` environment variable (defaults to `8080`).

## 3. CI/CD & Deployment Architecture

1. **Jenkins CI (`Jenkinsfile`)**:
   - Polls SCM every 5 minutes (`H/5 * * * *`).
   - Runs `mvnw.cmd test` and publishes JUnit XML reports (`target/surefire-reports/*.xml`).
   - Runs `mvnw.cmd package -DskipTests` and archives `target/*.jar` with fingerprinting.
2. **Ansible (`ansible/`)**:
   - Installs `openjdk-17-jre-headless` via `apt`.
   - Copies `target/labtrack-api.jar` to `/opt/labtrack/labtrack-api.jar`.
   - Templates `/etc/systemd/system/labtrack.service` and restarts only on file changes via handler.
3. **Docker (`Dockerfile`)**:
   - Uses `eclipse-temurin:17-jre-alpine`, exposes `8080`, and defines a `HEALTHCHECK` against `/health`.
4. **Kubernetes (`k8s/`)**:
   - `ConfigMap` (`labtrack-config`) supplies `SERVER_PORT: "8080"`.
   - `Deployment` (`labtrack-deployment`) runs 3 replicas with `livenessProbe` and `readinessProbe` on `GET /health:8080`.
   - `Service` (`labtrack-service`) exposes port `8080` via `NodePort` `30080`.
