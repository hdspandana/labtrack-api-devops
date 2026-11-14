# Evidence Checklist — LabTrack API

Use this checklist to collect screenshots and command outputs for the final academic report:

## Component 1: Git, Maven & Jenkins CI
- [ ] GitHub repository showing project structure and `repo_link.txt` updated.
- [ ] Local Maven test & package output (`.\mvnw.cmd test`, `.\mvnw.cmd package`).
- [ ] Jenkins Pipeline Stage View showing `Checkout`, `Test`, and `Package` green.
- [ ] Jenkins JUnit Test Result trend/report page.
- [ ] Jenkins Build Artifacts showing `labtrack-api.jar` with fingerprint.
- [ ] Jenkins SCM Polling log/configuration (`H/5 * * * *`).

## Component 2: Ansible, Docker & Kubernetes
- [ ] Kali Linux `ansible-playbook` execution output (including `changed` and `ok` tasks).
- [ ] `sudo systemctl status labtrack` showing `active (running)` on port 8080.
- [ ] `curl http://localhost:8080/health` and `curl http://localhost:8080/api/assets` on Kali Linux.
- [ ] `docker build -t labtrack-api:1.0 .` and `docker ps` showing `(healthy)` status.
- [ ] `kubectl apply -f k8s/` and `kubectl get pods -l app=labtrack-api` showing 3/3 `Running`.
- [ ] `kubectl describe pod <pod-name>` showing `Liveness` and `Readiness` probes on `/health:8080`.
- [ ] `kubectl scale deployment labtrack-deployment --replicas=5` showing 5/5 pods.
- [ ] `kubectl scale deployment labtrack-deployment --replicas=3` showing 3/3 pods.
