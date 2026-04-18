# Setup Checklist - Configuration Required Before Use

This is a checklist of all the things you need to configure before running the CI/CD pipeline.

---

## Jenkins Configuration

| Item | Description | Where to Configure |
|------|-------------|--------------------|
| JDK 17 | Configure JDK 17 installation | Jenkins → Global Tool Configuration → JDK → Name: `jdk-17` |
| Maven 3.9 | Configure Maven installation | Jenkins → Global Tool Configuration → Maven → Name: `maven-3.9` |
| Docker | Docker installed and available on agent | Jenkins agent |
| GitHub credentials | Add GitHub Personal Access Token to Jenkins | Jenkins → Credentials → System → Global credentials → ID: `github-credentials` |
| Docker Hub credentials | Add Docker Hub credentials to Jenkins | Jenkins → Credentials → System → Global credentials → ID: `docker-hub-credentials` |
| SonarQube | Configure SonarQube server | Jenkins → System Configuration → SonarQube servers → Name: `SonarQube` → Add URL & token |

---

## SonarQube Configuration

| Item | Description |
|------|-------------|
| Server URL | Update `SONAR_HOST_URL` in **Jenkinsfile** line 18 to your SonarQube URL |
| Project Key | Update `SONAR_PROJECT_KEY` in **Jenkinsfile** line 17 to match your project key in SonarQube |
| Generate token | Generate analysis token in SonarQube (User → My Account → Security → Generate Token) |
| Webhook | Add Jenkins webhook in SonarQube for quality gate status callback |

---

## GitHub Configuration

| Item | Description |
|------|-------------|
| Webhook | Add Jenkins webhook in GitHub → Settings → Webhooks |
| Payload URL | `https://<YOUR-JENKINS-URL>/github-webhook/` |
| Content type | `application/json` |
| Trigger | Send everything |

---

## Docker Registry / Docker Hub

| Item | Description |
|------|-------------|
| Update image name | In **Jenkinsfile** line 15: `IMAGE_NAME = 'your-username/ecommerce-backend'` → replace with your Docker Hub username/repo |
| Credentials | Add Docker Hub username+password to Jenkins credentials with ID `docker-hub-credentials` |

---

## Email Configuration (Application)

| Item | Where | What to change |
|------|-------|----------------|
| SMTP settings | `src/main/resources/application.properties` lines 32-35 | Update `spring.mail.username` and `spring.mail.password` with your email credentials |
| Default recipient | In `BatchConfig.java` line 107: `admin@example.com` → Change to your email where you want daily export reports |

---

## Application Configuration (Local/Docker)

| Item | Description |
|------|-------------|
| Database credentials | In `docker-compose.yml` - already defaults to `root` / `root` for MySQL |
| Export cron | `application.properties` line 41: `app.export.cron=0 0 1 * * ?` → change if you want different time |
| Kafka bootstrap | `application.properties` already set to `localhost:9092` for local, updated automatically in Docker Compose |

---

## First Run with Docker Compose

```bash
# 1. Build the application first
mvn clean package

# 2. Start all containers
docker-compose up --build -d

# 3. Check logs
docker-compose logs -f ecommerce-app
```

After startup, the app will automatically create:
- All database tables via Hibernate ddl-auto=update
- Quartz scheduler tables
- Spring Batch tables
- `demo-export-email` Kafka topic is auto-created

---

## Production Deployment Notes

- [ ] Change `spring.jpa.hibernate.ddl-auto` to `none` in production
- [ ] Use a persistent volume for MySQL data (already configured in docker-compose)
- [ ] Add Spring Security for authentication (currently no auth for demo)
- [ ] Hash user passwords (currently stored in plain text for demo)
- [ ] Use external secrets management for database/email credentials instead of storing in properties
