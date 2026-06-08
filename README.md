# core-aurora-performance

Servizio core (Spring Boot) — gestisce la logica di business e l'accesso al database.

## Start (locale)
```bash
docker compose up -d
```

## Deploy su OVH (single VM)

In produzione il servizio gira come container Docker sulla VM OVH, avviato
insieme al resto dello stack via `docker compose`.

```bash
# Sulla VM OVH, dalla root del workspace
docker compose --project-directory core-aurora-performance up -d --build
# oppure l'intero stack:
make up
```

Le immagini si buildano localmente sulla VM (Dockerfile multi-stage). Config e
segreti vengono letti dal file `.env` (vedi `.env.example`).

> Porta interna: 8081 (non esposto su Internet). Vedi `aurora-devops/GUIDA-DEPLOY-OVH.md`.
