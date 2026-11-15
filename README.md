# LabTrack API — Campus Laboratory Asset Management System

LabTrack API is a lightweight Spring Boot 3 (Java 17 bytecode target) REST API for managing campus laboratory assets in memory, complete with Jenkins CI, Ansible configuration management, Docker containerization, and Minikube Kubernetes orchestration.

## Technology Stack

- **Application**: Java 17 target, Spring Boot 3.2.5, Maven Wrapper (`mvnw` / `mvnw.cmd`), JUnit 5
- **CI (Windows 11)**: Git, GitHub, Jenkins Pipeline (`Jenkinsfile`)
- **Configuration Management (Kali Linux)**: Ansible + systemd (`openjdk-21` / `default-jre-headless` compatible)
- **Containerization**: Docker (`eclipse-temurin:17-jre-alpine`)
- **Orchestration**: Kubernetes (Minikube, `kubectl`)

## API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/` | Service metadata and status |
| `GET` | `/health` | Health check endpoint (`{"status":"UP","service":"labtrack-api"}`) |
| `GET` | `/api/assets` | List all laboratory assets |
| `POST` | `/api/assets` | Create a new laboratory asset (validated) |
| `PUT` | `/api/assets/{id}` | Update an existing asset by ID |
| `DELETE` | `/api/assets/{id}` | Delete an asset by ID |

## Environment Separation

1. **Agent-Created Files**: All source code, tests, `pom.xml`, Maven Wrapper, `Jenkinsfile`, `Dockerfile`, `ansible/` role & playbook, `k8s/` manifests, and `docs/` are pre-created in this repository.
2. **Windows 11 (PowerShell)**: Used for Maven Wrapper builds/tests, Git/GitHub push, Jenkins CI, Docker image build/run, and Minikube/kubectl deployment.
3. **Kali Linux (Bash)**: Used for Ansible configuration management (`localhost` target) and systemd service verification.

See `docs/implementation-guide.md` for step-by-step beginner-friendly instructions and expected success conditions, and `docs/evidence-checklist.md` for assignment verification.
Jenkins SCM polling verification
