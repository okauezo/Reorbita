# Evidencias de Execucao - REORBITA (Spring Boot)

Capturas reais de requisicao/resposta da API em execucao em http://localhost:8080, com banco H2 populado pelo seed inicial.

## 1. Listar satelites (GET /api/satellites)

```http
GET /api/satellites
```
```json
[
    {
        "id":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
        "name":  "Aurora-1",
        "operatorName":  "Helios Space",
        "noradId":  "55001",
        "status":  "DEGRADED",
        "designStandard":  "Orbit-Ready ORB-C 1.2",
        "supportsModularRepair":  true,
        "maintenanceComplexity":  1,
        "launchDate":  "2023-06-01T21:02:01.065695",
        "ageInYears":  3,
        "altitudeKm":  540.0,
        "inclinationDeg":  53.2,
        "batteryCapacityPercent":  38.0,
        "sensorCount":  3
    },
    {
        "id":  "2877bf45-11b5-4ecc-9158-7f6ea49f8694",
        "name":  "Vetus-7",
        "operatorName":  "Antares Telecom",
        "noradId":  "41888",
        "status":  "OPERATIONAL",
        "designStandard":  "Legacy (nao padronizado)",
        "supportsModularRepair":  false,
        "maintenanceComplexity":  4,
        "launchDate":  "2017-06-01T21:02:01.065695",
        "ageInYears":  9,
        "altitudeKm":  780.0,
        "inclinationDeg":  98.1,
        "batteryCapacityPercent":  55.0,
        "sensorCount":  2
    }
]
```

## 2. Detalhar satelite (GET /api/satellites/{id})

```http
GET /api/satellites/d1aeafc1-e04e-4bd3-a296-8748df94684f
```
```json
{
    "id":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
    "name":  "Aurora-1",
    "operatorName":  "Helios Space",
    "noradId":  "55001",
    "status":  "DEGRADED",
    "designStandard":  "Orbit-Ready ORB-C 1.2",
    "supportsModularRepair":  true,
    "maintenanceComplexity":  1,
    "launchDate":  "2023-06-01T21:02:01.065695",
    "ageInYears":  3,
    "altitudeKm":  540.0,
    "inclinationDeg":  53.2,
    "batteryCapacityPercent":  38.0,
    "sensorCount":  3
}
```

## 3. Registrar satelite Orbit-Ready (POST /api/satellites)

```http
POST /api/satellites

{
    "inclinationDeg":  51.6,
    "noradId":  "55777",
    "chargeCycles":  300,
    "velocityKmS":  7.5,
    "dockingInterfaceVersion":  "ORB-C 1.0",
    "operatorName":  "Nova Orbital",
    "name":  "Helios-9",
    "orbitReady":  true,
    "launchDate":  "2024-02-10T00:00:00",
    "batteryCapacityPercent":  88,
    "altitudeKm":  600
}
```
```json
{
    "id":  "e737712a-80ca-4a68-b06e-2688efcc0e40",
    "name":  "Helios-9",
    "operatorName":  "Nova Orbital",
    "noradId":  "55777",
    "status":  "OPERATIONAL",
    "designStandard":  "Orbit-Ready ORB-C 1.0",
    "supportsModularRepair":  true,
    "maintenanceComplexity":  1,
    "launchDate":  "2024-02-10T00:00:00",
    "ageInYears":  2,
    "altitudeKm":  600.0,
    "inclinationDeg":  51.6,
    "batteryCapacityPercent":  88.0,
    "sensorCount":  0
}
```

## 4. Ingerir telemetria (POST /api/satellites/{id}/telemetry)

```http
POST /api/satellites/d1aeafc1-e04e-4bd3-a296-8748df94684f/telemetry

{
    "solarOutputWatts":  1180,
    "fuelPercent":  58,
    "orbitDeviationKm":  3.4,
    "batteryPercent":  34.5,
    "temperatureCelsius":  2.1
}
```
```json
{
    "id":  "ffd4f9b9-70bc-467b-9e78-1426240e6814",
    "satelliteId":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
    "recordedAt":  "2026-06-01T21:02:38.0377441",
    "batteryPercent":  34.5,
    "fuelPercent":  58.0,
    "solarOutputWatts":  1180.0,
    "orbitDeviationKm":  3.4,
    "temperatureCelsius":  2.1
}
```

## 5. Historico de telemetria (GET .../telemetry?days=90)

```http
GET /api/satellites/d1aeafc1-e04e-4bd3-a296-8748df94684f/telemetry?days=90
```
Total de leituras retornadas: 14. Amostra (3 primeiras e 3 ultimas):
```json
[
    {
        "recordedAt":  "2026-04-02T21:02:01.065695",
        "batteryPercent":  92.0,
        "fuelPercent":  70.0
    },
    {
        "recordedAt":  "2026-04-07T21:02:01.065695",
        "batteryPercent":  87.5,
        "fuelPercent":  69.5
    },
    {
        "recordedAt":  "2026-04-12T21:02:01.065695",
        "batteryPercent":  83.0,
        "fuelPercent":  69.0
    }
]
```
```json
[
    {
        "recordedAt":  "2026-05-27T21:02:01.065695",
        "batteryPercent":  42.5,
        "fuelPercent":  64.5
    },
    {
        "recordedAt":  "2026-06-01T21:02:01.065695",
        "batteryPercent":  38.0,
        "fuelPercent":  64.0
    },
    {
        "recordedAt":  "2026-06-01T21:02:38.037744",
        "batteryPercent":  34.5,
        "fuelPercent":  58.0
    }
]
```

## 6. Manutencao preditiva / IA (POST .../maintenance/analyze)

```http
POST /api/satellites/d1aeafc1-e04e-4bd3-a296-8748df94684f/maintenance/analyze
```
```json
{
    "satelliteId":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
    "satelliteName":  "Aurora-1",
    "sampleCount":  14,
    "batteryDropPerDay":  0.9181,
    "currentBatteryPercent":  34.5,
    "predictedFailureAt":  "2026-06-17T16:05:38.108119",
    "daysUntilFailure":  16,
    "recommendation":  "Agendar troca de modulo de bateria imediatamente.",
    "alertRaised":  true
}
```

## 7. Alertas ativos (GET /api/alerts)

```http
GET /api/alerts
```
```json
{
    "id":  "2f4b5753-1748-4813-9f0e-a941f0d759a9",
    "satelliteId":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
    "severity":  "CRITICAL",
    "category":  "Bateria",
    "message":  "Projecao indica queda abaixo de 20% em aproximadamente 16 dias.",
    "createdAt":  "2026-06-01T21:02:38.108119",
    "predictedFailureAt":  "2026-06-17T16:05:38.108119",
    "daysUntilFailure":  16,
    "acknowledged":  false
}
```

## 8. Reconhecer alerta (POST /api/alerts/{id}/acknowledge)

```http
POST /api/alerts/2f4b5753-1748-4813-9f0e-a941f0d759a9/acknowledge
```
```json
{
    "id":  "2f4b5753-1748-4813-9f0e-a941f0d759a9",
    "satelliteId":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
    "severity":  "CRITICAL",
    "category":  "Bateria",
    "message":  "Projecao indica queda abaixo de 20% em aproximadamente 16 dias.",
    "createdAt":  "2026-06-01T21:02:38.108119",
    "predictedFailureAt":  "2026-06-17T16:05:38.108119",
    "daysUntilFailure":  16,
    "acknowledged":  true
}
```

## 9. Frota de robos (GET /api/robots/fleet)

```http
GET /api/robots/fleet
```
```json
{
    "id":  "85ce3f5f-17fd-4e7c-8ad6-ea3731e512c8",
    "name":  "Estacao-Mae Orion",
    "orbitAltitudeKm":  520.0,
    "availableRobots":  4,
    "robots":  [
                   {
                       "id":  "975dc781-fe62-42a9-ad30-bd06aa3dccb2",
                       "codename":  "RB-FUEL-01",
                       "specialty":  "REFUEL",
                       "available":  true
                   },
                   {
                       "id":  "9c8e9750-daaf-44e9-8e79-89155ee9ffd8",
                       "codename":  "RB-SWAP-01",
                       "specialty":  "MODULE_SWAP",
                       "available":  true
                   },
                   {
                       "id":  "54b2d9d5-e199-4ec6-893c-a08b26831dd1",
                       "codename":  "RB-TRAJ-01",
                       "specialty":  "TRAJECTORY_CORRECTION",
                       "available":  true
                   },
                   {
                       "id":  "d7796a38-1ee8-4722-a6e5-d5502d1cccdf",
                       "codename":  "RB-NET-01",
                       "specialty":  "DEBRIS_CAPTURE",
                       "available":  true
                   }
               ]
}
```

## 10. Despachar robo (POST /api/robots/dispatch)

```http
POST /api/robots/dispatch

{
    "notes":  "Troca preventiva de bateria",
    "satelliteId":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
    "kind":  "MODULE_SWAP"
}
```
```json
{
    "id":  "d8e4ea7a-a6f5-465b-bc72-4f3b3d99e0d4",
    "satelliteId":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
    "robotId":  "9c8e9750-daaf-44e9-8e79-89155ee9ffd8",
    "robotCodename":  "RB-SWAP-01",
    "kind":  "MODULE_SWAP",
    "status":  "DISPATCHED",
    "createdAt":  "2026-06-01T21:02:38.1687707",
    "scheduledFor":  "2026-06-02T21:02:38.1687707",
    "completedAt":  null,
    "estimatedMissionHours":  11.0,
    "notes":  "Troca preventiva de bateria"
}
```

## 11. Avancar ciclo de vida da ordem (POST .../orders/{id}/advance)

```http
POST /api/robots/orders/d8e4ea7a-a6f5-465b-bc72-4f3b3d99e0d4/advance
```
Apos passo 1, status = **IN_PROGRESS**
```json
{
    "id":  "d8e4ea7a-a6f5-465b-bc72-4f3b3d99e0d4",
    "satelliteId":  "d1aeafc1-e04e-4bd3-a296-8748df94684f",
    "robotId":  "9c8e9750-daaf-44e9-8e79-89155ee9ffd8",
    "robotCodename":  "RB-SWAP-01",
    "kind":  "MODULE_SWAP",
    "status":  "COMPLETED",
    "createdAt":  "2026-06-01T21:02:38.168771",
    "scheduledFor":  "2026-06-02T21:02:38.168771",
    "completedAt":  "2026-06-01T21:02:38.1927449",
    "estimatedMissionHours":  11.0,
    "notes":  "Troca preventiva de bateria"
}
```

## 12. Tratamento de erro - 404 (satelite inexistente)

```http
GET http://localhost:8080/api/satellites/00000000-0000-0000-0000-000000000000
```
```json
HTTP 404
{"type":"about:blank","title":"Recurso nao encontrado","status":404,"detail":"Satelite 00000000-0000-0000-0000-000000000000 nao encontrado.","instance":"/api/satellites/00000000-0000-0000-0000-000000000000"}
```

## 13. Tratamento de erro - 409 (NORAD duplicado)

```http
POST http://localhost:8080/api/satellites

{
    "inclinationDeg":  50,
    "noradId":  "55001",
    "chargeCycles":  100,
    "altitudeKm":  500,
    "operatorName":  "X",
    "name":  "Aurora Dup",
    "orbitReady":  true,
    "launchDate":  "2024-01-01T00:00:00",
    "batteryCapacityPercent":  80,
    "velocityKmS":  7.5
}
```
```json
HTTP 409
{"type":"about:blank","title":"Conflito de registro","status":409,"detail":"Ja existe um satelite registrado com o NORAD ID 55001.","instance":"/api/satellites"}
```

## 14. Tratamento de erro - 400 (telemetria invalida)

```http
POST http://localhost:8080/api/satellites/d1aeafc1-e04e-4bd3-a296-8748df94684f/telemetry

{
    "solarOutputWatts":  100,
    "fuelPercent":  50,
    "orbitDeviationKm":  1,
    "batteryPercent":  150,
    "temperatureCelsius":  0
}
```
```json
HTTP 400
{"type":"about:blank","title":"Erro de validacao","status":400,"detail":"Um ou mais campos sao invalidos.","instance":"/api/satellites/d1aeafc1-e04e-4bd3-a296-8748df94684f/telemetry","errors":{"batteryPercent":"deve ser menor que ou igual a 100"}}
```
