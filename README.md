# core-aurora-performance

Servizio core (Spring Boot) — gestisce la logica di business e l'accesso al database.

## Start (locale)
```bash
docker compose up -d
```

## Deploy su GCP Cloud

### Prerequisiti
```bash
gcloud auth configure-docker europe-west8-docker.pkg.dev
gcloud container clusters get-credentials aurora-cluster --region europe-west8 --project aurora-perf-prod
```

### 1. Build JAR
```bash
./mvnw package -DskipTests -B
```

### 2. Build immagine Docker
```bash
docker build -t europe-west8-docker.pkg.dev/aurora-perf-prod/aurora-docker/core:latest .
```

### 3. Push su Artifact Registry
```bash
docker push europe-west8-docker.pkg.dev/aurora-perf-prod/aurora-docker/core:latest
```

### 4. Deploy su GKE
```bash
kubectl rollout restart deployment/core -n aurora
kubectl rollout status deployment/core -n aurora
```

> **Manifest K8s**: `k8s/20-core.yaml` — porta 8081
