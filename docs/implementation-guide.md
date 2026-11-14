# Implementation Guide — LabTrack API

This guide separates **agent-created files** from the **exact commands you must execute** on Windows 11 and Kali Linux, along with the **expected success condition** for each step.

---

## 1. Agent-Created Files (Already in Repository)

- Application & Tests: `pom.xml`, `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties`, `src/main/...`, `src/test/...`
- CI Pipeline: `Jenkinsfile`, `.gitignore`, `repo_link.txt`
- Ansible: `ansible/inventory.ini`, `ansible/playbook.yml`, `ansible/bootstrap-role.sh`, `ansible/roles/labtrack_app/...`
- Docker & Kubernetes: `Dockerfile`, `.dockerignore`, `k8s/configmap.yaml`, `k8s/deployment.yaml`, `k8s/service.yaml`

---

## 2. Windows 11 Commands (PowerShell)

> **Prerequisites on Windows**: Git, Java (17+), Docker Desktop (engine running), Minikube, `kubectl`, and Jenkins. Global Maven is **not** required because `.\mvnw.cmd` is included.

### Step 2.1: Build and Run JUnit 5 Tests
```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```
- **Expected success condition**: `BUILD SUCCESS`, all tests in `AssetControllerTest` pass, and `target\labtrack-api.jar` is created.

### Step 2.2: Push to GitHub
```powershell
git init
git add .
git commit -m "Initial commit: LabTrack API"
git branch -M main
git remote add origin https://github.com/<your-username>/labtrack-api.git
git push -u origin main
```
- **Expected success condition**: Repository files appear on GitHub. Update `repo_link.txt` with your repository URL.

### Step 2.3: Run Jenkins CI Pipeline
1. Open Jenkins on Windows 11 and create a **Pipeline** job named `labtrack-api-ci`.
2. Under **Pipeline**, select **Pipeline script from SCM** -> **Git**, enter your GitHub URL, branch `*/main`, and Script Path `Jenkinsfile`.
3. Click **Build Now**.
- **Expected success condition**: `Checkout`, `Test`, and `Package` stages succeed; JUnit test results are published; `target/labtrack-api.jar` is archived with a fingerprint.

### Step 2.4: Build and Test Docker Container
*(Ensure Docker Desktop engine is running first.)*
```powershell
docker build -t labtrack-api:1.0 .
docker run -d -p 8080:8080 --name labtrack-api labtrack-api:1.0
docker ps
curl.exe http://localhost:8080/health
```
- **Expected success condition**: `docker ps` shows `labtrack-api` running (and `(healthy)` after ~20s); `curl.exe` returns `{"status":"UP","service":"labtrack-api"}`.
- Cleanup when done:
```powershell
docker stop labtrack-api
docker rm labtrack-api
```

### Step 2.5: Deploy and Scale on Minikube
```powershell
minikube start
minikube image load labtrack-api:1.0
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl get pods -l app=labtrack-api
```
- **Expected success condition**: 3 pods in `Running` state with `1/1` Ready.

Verify probes and service:
```powershell
kubectl describe deployment labtrack-deployment
kubectl get svc labtrack-service
minikube service labtrack-service --url
```

Scale to 5 replicas and back to 3:
```powershell
kubectl scale deployment labtrack-deployment --replicas=5
kubectl get pods -l app=labtrack-api
kubectl scale deployment labtrack-deployment --replicas=3
kubectl get pods -l app=labtrack-api
```
- **Expected success condition**: Pod count scales to 5 `Running` pods, then back to 3 `Running` pods.

---

## 3. Kali Linux Commands (Kali Bash)

> **Verified Kali Environment**:
> - **OpenJDK 21.0.9** at `/usr/bin/java` (runs Java 17-compatible bytecode).
> - **Git 2.51.0**, **Python 3.14.8**, **Ansible Core 2.22.0b2** (installed; `localhost` ping succeeded), and **systemd 258**.
> - **Maven** is not globally installed — use the included `./mvnw` wrapper.
> - **Docker and kubectl** are not installed on Kali because Docker and Kubernetes are handled on Windows 11.

### Step 3.1: Verify Ansible and `systemd` Availability
```bash
# Kali Bash
ansible --version
ansible -i ansible/inventory.ini localhost -m ping
systemctl --version
ps -p 1 -o comm=
```
- **Expected success condition**: `ansible` reports `2.22.0b2`, `localhost` ping returns `SUCCESS => {"ping": "pong"}`, `systemd 258` is printed, and PID 1 is `systemd`.

### Step 3.3: Clone Repository and Build JAR (using Maven Wrapper)
```bash
# Kali Bash
git clone https://github.com/<your-username>/labtrack-api.git
cd labtrack-api
chmod +x mvnw ansible/bootstrap-role.sh
./mvnw package -DskipTests
```
- **Expected success condition**: `target/labtrack-api.jar` is created using Java 17 bytecode target.

### Step 3.4: Role Bootstrap (`ansible-galaxy init` workflow)
The `ansible/roles/labtrack_app` role is already included. Running the bootstrap script safely runs `ansible-galaxy init` in a temporary directory and merges only missing standard directories/files without overwriting existing implementation files:
```bash
# Kali Bash
./ansible/bootstrap-role.sh
ls -la ansible/roles/labtrack_app
```
- **Expected success condition**: Prints that `labtrack_app` already exists and merged any missing skeleton items without overwriting existing files. Save this output as evidence of `ansible-galaxy init`.

### Step 3.5: Run Ansible Playbook
The playbook checks `which java` first and uses the existing OpenJDK 21.0.9 runtime without requiring an unavailable Java 17 package:
```bash
# Kali Bash
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml --ask-become-pass
```
- **Expected success condition**: Playbook completes with `failed=0`. Re-running it reports `changed=0` (idempotent).

### Step 3.6: Verify Systemd Service and API
```bash
# Kali Bash
sudo systemctl status labtrack
curl http://localhost:8080/health
curl http://localhost:8080/api/assets
```
- **Expected success condition**: `labtrack.service` is `active (running)` on port `8080`, and `/health` returns `{"status":"UP","service":"labtrack-api"}`.
