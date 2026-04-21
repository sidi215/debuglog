<p align="center">
  <img src="https://img.shields.io/badge/version-1.0.0-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/java-17-orange?style=flat-square" />
  <img src="https://img.shields.io/badge/spring%20boot-3.3-green?style=flat-square" />
  <img src="https://img.shields.io/badge/AI-local%20only-purple?style=flat-square" />
  <img src="https://img.shields.io/badge/license-MIT-lightgrey?style=flat-square" />
</p>

<h1 align="center">DebugLog</h1>
<p align="center">Local AI-powered log analyzer for developers and DevSecOps engineers.<br/>Zero data leaves your machine.</p>

---

## Table of Contents

- [Overview](#overview)
- [How It Works](#how-it-works)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Choosing Your Model](#choosing-your-model)
- [CLI Usage](#cli-usage)
- [API Reference](#api-reference)
- [Running Tests](#running-tests)
- [Configuration](#configuration)
- [Security](#security)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

DebugLog is a self-hosted CLI tool that analyzes system logs and stack traces using a local Large Language Model. It runs entirely on your machine — no cloud API calls, no data leaks, no subscriptions.

**Core features:**

- PII sanitization before any AI processing (IPs, emails, API keys, passwords)
- Auto-detection of log formats: Spring Boot, JSON, Nginx, generic stack traces
- SQLite knowledge base that learns from confirmed solutions over time
- Supports any Ollama-compatible model via a single environment variable
- Works fully offline after the first model pull
- Color-coded terminal output via a Python CLI

---

## How It Works

```
┌─────────────────────────────────────────────────────────────────┐
│  Developer Machine                                              │
│                                                                 │
│  debuglog error.log                                             │
│       │                                                         │
│       ▼                                                         │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Spring Boot Backend (Docker)                           │   │
│  │                                                         │   │
│  │  Log Parser → PII Sanitizer → KB Lookup → Prompt       │   │
│  │                                      ↓                  │   │
│  │                              Spring AI / Ollama         │   │
│  │                                      ↓                  │   │
│  │                              Response Formatter         │   │
│  └─────────────────────────────────────────────────────────┘   │
│       │                              │                          │
│       ▼                              ▼                          │
│  Terminal output              SQLite KB (confirmed fixes)       │
└─────────────────────────────────────────────────────────────────┘
```

1. **Trigger** — run `debuglog error.log` or pipe any command output
2. **Parse** — auto-detect log format and extract structured fields
3. **Sanitize** — strip all PII before AI processing
4. **KB Lookup** — check SQLite for previously confirmed solutions
5. **Analyze** — send sanitized prompt to local LLM via Ollama
6. **Display** — render color-coded root cause and solution in terminal
7. **Learn** — confirm the fix to save it to the knowledge base

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.3 |
| AI Orchestration | Spring AI + Ollama |
| Local LLM | qwen2.5-coder:7b (default) |
| Database | SQLite + Flyway migrations |
| CLI | Python 3.10+, Rich |
| Infrastructure | Docker, Docker Compose |

---

## Project Structure

```
debuglog/
├── .env.example
├── .gitignore
├── docker-compose.yml
├── README.md
│
├── cli/
│   ├── debuglog.py          # CLI entry point
│   ├── formatter.py         # Terminal rendering
│   ├── install.sh           # Installation script
│   └── requirements.txt
│
└── backend/
    ├── Dockerfile
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/debuglog/
        │   │   ├── DebugLogApplication.java
        │   │   ├── controller/
        │   │   │   └── AnalysisController.java
        │   │   ├── service/
        │   │   │   ├── AnalysisService.java
        │   │   │   ├── SanitizationService.java
        │   │   │   ├── LogParserService.java
        │   │   │   └── KnowledgeBaseService.java
        │   │   ├── ai/
        │   │   │   └── PromptBuilder.java
        │   │   ├── parser/
        │   │   │   ├── LogParserStrategy.java
        │   │   │   ├── SpringBootLogParser.java
        │   │   │   ├── JsonLogParser.java
        │   │   │   ├── NginxLogParser.java
        │   │   │   └── GenericStackTraceParser.java
        │   │   ├── sanitizer/
        │   │   │   ├── SanitizerRule.java
        │   │   │   ├── PiiSanitizerChain.java
        │   │   │   └── rules/
        │   │   │       ├── IpAddressRule.java
        │   │   │       ├── EmailRule.java
        │   │   │       ├── ApiKeyRule.java
        │   │   │       └── PasswordRule.java
        │   │   ├── repository/
        │   │   │   └── ResolutionRepository.java
        │   │   ├── model/
        │   │   │   ├── ParsedLog.java
        │   │   │   ├── Resolution.java
        │   │   │   ├── AnalysisRequest.java
        │   │   │   └── AnalysisResponse.java
        │   │   └── exception/
        │   │       ├── GlobalExceptionHandler.java
        │   │       └── LogParsingException.java
        │   └── resources/
        │       ├── application.yml
        │       └── db/migration/
        │           └── V1__create_resolutions.sql
        └── test/
            └── java/com/debuglog/
                ├── service/
                │   ├── SanitizationServiceTest.java
                │   ├── LogParserServiceTest.java
                │   └── KnowledgeBaseServiceTest.java
                ├── controller/
                │   └── AnalysisControllerIntegrationTest.java
                └── parser/
                    ├── SpringBootLogParserTest.java
                    ├── JsonLogParserTest.java
                    └── NginxLogParserTest.java
```

---

## Prerequisites

| Tool | Minimum version | Check |
|---|---|---|
| Docker | 24.0 | `docker --version` |
| Docker Compose | v2.0 | `docker compose version` |
| Python | 3.10 | `python3 --version` |
| Java (optional) | 17 | `java --version` (only needed to run tests locally) |

> **Note:** Java is not required on your host machine if you only use Docker. The backend compiles and runs entirely inside the container.

---

## Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/yourname/debuglog
cd debuglog

# 2. Configure environment
cp .env.example .env

# 3. Start the full stack
# First run: pulls Ollama image + downloads the AI model (~4GB)
docker compose up -d

# 4. Watch startup progress
docker compose logs -f

# 5. Install the CLI
cd cli
pip install -r requirements.txt
bash install.sh

# 6. Verify everything is running
curl http://localhost:8080/actuator/health
debuglog --help
```

Once you see `{"status":"UP"}` from the health check, you are ready.

---

## Choosing Your Model

The default model is `qwen2.5-coder:7b` — purpose-built for code and stack trace analysis.

### Changing the model

Edit `.env` before starting the stack:

```env
OLLAMA_MODEL=qwen2.5-coder:7b
```

### Available models

| Model | Strengths | RAM required |
|---|---|---|
| `qwen2.5-coder:7b` | Best for code errors, default | ~8 GB |
| `qwen2.5-coder:3b` | Lighter, still code-aware | ~4 GB |
| `codellama:7b` | Java / Python stack traces | ~8 GB |
| `mistral:7b` | General errors, strong reasoning | ~8 GB |
| `llama3.2:3b` | Balanced, fast | ~4 GB |
| `phi3:mini` | Minimal hardware | ~2 GB |

### Using a model you already have locally

If Ollama is already running on your machine with models downloaded, mount your existing volume instead of pulling again:

```yaml
# In docker-compose.yml, under the ollama service:
volumes:
  - ~/.ollama:/root/.ollama
```

### First run vs offline

| Scenario | Behavior |
|---|---|
| First `docker compose up` | Pulls Ollama image + downloads model (needs internet) |
| Every subsequent run | Uses cached model from `ollama_data` volume, fully offline |
| After `docker compose down` | Volume persists, no re-download needed |
| After `docker compose down -v` | Volume deleted, model re-downloaded on next start |

---

## CLI Usage

### Analyze a log file

```bash
debuglog error.log
debuglog /var/log/myapp/app.log
```

### Pipe from any command

```bash
# Docker container logs
docker logs my-container 2>&1 | debuglog

# Failed Maven build
mvn test 2>&1 | debuglog

# Last 100 lines of a running log
tail -n 100 /var/log/app.log | debuglog

# Journald
journalctl -u myapp --since "10 min ago" | debuglog
```

### Confirm a solution (trains the knowledge base)

After each analysis, DebugLog asks:

```
Did this solve your issue? [y/N]:
```

Answering `y` saves the solution to SQLite. Next time the same error pattern appears, the answer is retrieved instantly without hitting the LLM.

### Environment variable

If the backend runs on a non-default host:

```bash
export DEBUGLOG_URL=http://192.168.1.100:8080
debuglog error.log
```

---

## API Reference

### POST `/api/v1/analyze`

Analyze a log and return a structured diagnosis.

**Request:**
```json
{
  "logContent": "ERROR c.d.UserService - Failed\njava.lang.NullPointerException..."
}
```

**Response:**
```json
{
  "format": "spring-boot",
  "errorType": "java.lang.NullPointerException",
  "rootCause": "Null reference on UserService line 42",
  "solution": "Check for null before calling getUser(). Add @NonNull or Optional<>.",
  "severity": "HIGH",
  "fromKnowledgeBase": false,
  "sessionId": "3-a1b2c3d4"
}
```

**Validation:**
- `logContent` must not be blank
- `logContent` max size: 50,000 characters

---

### POST `/api/v1/confirm/{sessionId}`

Confirm that a solution worked. Saves it to the knowledge base.

```bash
curl -X POST http://localhost:8080/api/v1/confirm/3-a1b2c3d4
```

**Response:**
```json
{ "status": "confirmed" }
```

---

### GET `/actuator/health`

Health check endpoint used by Docker Compose.

```json
{ "status": "UP" }
```

---

## Running Tests

### Unit and integration tests (requires Java 17 locally)

```bash
cd backend
mvn test
```

### Run a specific test class

```bash
mvn test -Dtest=SanitizationServiceTest
mvn test -Dtest=SpringBootLogParserTest
mvn test -Dtest=AnalysisControllerIntegrationTest
```

### Coverage report

```bash
mvn test jacoco:report
open target/site/jacoco/index.html
```

### Run tests inside Docker (no local Java needed)

```bash
docker compose run --rm backend mvn test
```

Expected output:

```
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Configuration

All configuration is done via `.env`. Never commit this file.

```env
# AI model — change to swap the LLM
OLLAMA_MODEL=qwen2.5-coder:7b

# Ollama internal URL (do not change unless using external Ollama)
OLLAMA_BASE_URL=http://ollama:11434

# Backend port exposed on your host
SERVER_PORT=8080

# Spring profile
SPRING_PROFILES_ACTIVE=prod
```

### Full `application.yml` reference

| Property | Default | Description |
|---|---|---|
| `spring.ai.ollama.chat.model` | `qwen2.5-coder:7b` | LLM model name |
| `spring.ai.ollama.chat.options.temperature` | `0.2` | Lower = more deterministic |
| `spring.ai.ollama.chat.options.num-predict` | `1024` | Max tokens in response |
| `spring.datasource.url` | `/app/data/aegislog.db` | SQLite file path |
| `server.port` | `8080` | HTTP port |

---

## Security

### What DebugLog protects against

| Threat | Mitigation |
|---|---|
| PII in logs sent to AI | Regex sanitization chain runs before every LLM call |
| Data leaving the network | Ollama runs locally, no external API calls |
| Oversized payloads | 50,000 character limit enforced at validation layer |
| Unhandled exceptions leaking internals | `GlobalExceptionHandler` returns generic messages |
| Running as root in container | Dedicated non-root `debuglog` user in Dockerfile |

### PII redaction patterns

| Pattern | Replaced with |
|---|---|
| IPv4 addresses | `[REDACTED-IP]` |
| Email addresses | `[REDACTED-EMAIL]` |
| API keys / tokens / secrets | `[REDACTED-KEY]` |
| Passwords | `[REDACTED-PASSWORD]` |

### What is NOT stored

- Raw log content is never persisted
- Only the sanitized error pattern + AI solution are saved to SQLite
- No telemetry, no analytics, no external calls of any kind

---

## Troubleshooting

### Backend fails to start — compilation error

Check for Unicode characters in Java source files (smart quotes, em dashes). Every string literal must use straight ASCII quotes `"`.

```bash
# Rebuild after fixing
docker compose down
docker compose up -d --build
```

### Model pull takes too long

The first pull downloads several gigabytes. You can monitor progress:

```bash
docker compose logs -f ollama-init
```

### `Cannot connect to DebugLog backend`

```bash
# Check backend is healthy
docker compose ps
curl http://localhost:8080/actuator/health

# Check logs for startup errors
docker compose logs backend
```

### `Request timed out`

The model may still be loading into memory on first call. Wait 30 seconds and retry.

### Port 8080 already in use

```env
# Change in .env
SERVER_PORT=9090
```

Then restart: `docker compose down && docker compose up -d`

### Reset everything (start fresh)

```bash
# Stops containers and deletes all volumes including the model cache
docker compose down -v
docker compose up -d
```

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Make your changes with tests
4. Run `mvn test` and ensure all tests pass
5. Open a pull request with a clear description

### Adding a new log parser

1. Create `backend/src/main/java/com/debuglog/parser/YourFormatParser.java`
2. Implement `LogParserStrategy` (two methods: `supports` and `parse`)
3. Annotate with `@Component` — Spring auto-registers it
4. Add a test in `src/test/java/com/debuglog/parser/`

### Adding a new PII sanitizer rule

1. Create `backend/src/main/java/com/debuglog/sanitizer/rules/YourRule.java`
2. Implement `SanitizerRule` and annotate with `@Component`
3. Add a test case in `SanitizationServiceTest`

---

## License

MIT License — see [LICENSE](LICENSE) for details.