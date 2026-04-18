# Upgrade TODO: JDK 8 + Spring Boot 2.6 + Hibernate 5 → JDK 17 + Spring Boot 3.5 + Hibernate 6

Complete step-by-step upgrade checklist.

---

## Phase 1: Pre-Upgrade Preparation (do on current version first)
- [ ] Commit all current changes, create a separate upgrade branch:
  ```bash
  git checkout -b upgrade-springboot3-jdk17
  ```
- [ ] Backup production database before any changes
- [ ] Fix all existing compilation errors and failing tests first
- [ ] Remove any unused dependencies and already deprecated code
- [ ] Update Maven wrapper (if used):
  ```bash
  mvn -N wrapper:wrapper
  ```
- [ ] **Use OpenRewrite to automate 80% of the upgrade (SAVE HOURS of manual work):**
  ```bash
  mvn -U org.openrewrite.maven:rewrite-maven-plugin:run \
    -Drewrite.activeRecipes=org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_5 \
    -Drewrite.parallel=true
  ```
  This automatically:
  - Changes `javax.*` imports to `jakarta.*`
  - Updates Spring Boot parent version
  - Renames deprecated application properties
  - Updates deprecated API usage

---

## Phase 2: Update Java Version
- [ ] Change Java version in `pom.xml`:
  ```xml
  <java.version>17</java.version>
  ```
- [ ] Verify/update `maven-compiler-plugin` configuration:
  ```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
      <source>17</source>
      <target>17</target>
      <annotationProcessorPaths>
        <!-- keep lombok and mapstruct entries updated -->
      </annotationProcessorPaths>
    </configuration>
  </plugin>
  ```
- [ ] Fix compilation errors from removed/internal JDK APIs:
  - [ ] Replace direct uses of `sun.misc.Unsafe`
  - [ ] Fix reflection code accessing internal JDK classes
  - [ ] Fix module descriptors if you use `module-info.java`
- [ ] Compile: `mvn clean compile` → fix all errors before proceeding

---

## Phase 3: Update Spring Boot from 2.6 → 3.5
- [ ] Update parent pom version:
  ```xml
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.0</version>
    <relativePath/>
  </parent>
  ```
- [ ] **Verify package imports (OpenRewrite should do this, check anyway):**
  | Old Package | New Package |
  |-------------|-------------|
  | `javax.persistence.*` | `jakarta.persistence.*` |
  | `javax.validation.*` | `jakarta.validation.*` |
  | `javax.mail.*` | `jakarta.mail.*` |
  | `javax.annotation.*` | `jakarta.annotation.*` |
  | `javax.servlet.*` | `jakarta.servlet.*` |
- [ ] Fix deprecated Spring APIs:
  - [ ] `WebMvcConfigurerAdapter` → remove, implement `WebMvcConfigurer` directly
  - [ ] Remove unnecessary `@Autowired` (constructor injection is default now)
  - [ ] `RestTemplate` → optional: migrate to new `RestClient`
- [ ] Check all application properties for renamed/deprecated properties
- [ ] Verify JPA/Hibernate configuration works with the new version

---

## Phase 4: Update Hibernate from 5 → 6
- [ ] Hibernate 6 is included automatically with Spring Boot 3, no manual version change needed
- [ ] Naming strategy changes:
  - Old: `ImprovedNamingStrategy` is deprecated
  - New: `SpringPhysicalNamingStrategy` is default in Spring Boot 3, remove explicit configuration if not needed
- [ ] Fix JPQL/HQL queries: Hibernate 6 has stricter syntax:
  - [ ] Fix implicit joins - must explicitly qualify now
  - [ ] Fix incorrect alias usage
  - [ ] Test all custom queries
- [ ] Verify type mappings (primitives, `byte[]/Blob`) work correctly

---

## Phase 5: Update Third-Party Dependencies
- [ ] Update Lombok to compatible version:
  ```xml
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <optional>true</optional>
  </dependency>
  ```
- [ ] Update MapStruct to 1.5.5+ (update both dependency and annotation processor):
  ```xml
  <dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
  </dependency>
  ```
- [ ] Spring Batch 5 (included with Spring Boot 3):
  - [ ] Job configuration changed: from `JobBuilderFactory` → use `JobBuilder` with injected `JobRepository`
  - [ ] Verify your order export batch job configuration works
- [ ] Spring Kafka 3:
  - [ ] Check consumer/producer configuration for any API changes
  - [ ] Verify `@KafkaListener` still works
- [ ] Remove deprecated starters that no longer exist in Spring Boot 3

---

## Phase 6: Specific Changes for THIS E-Commerce Project
- [ ] Update entity imports in `src/main/java/com/example/ecommerce/entity/`: `javax.persistence` → `jakarta.persistence`
- [ ] Update validation imports in `src/main/java/com/example/ecommerce/dto/`: `javax.validation` → `jakarta.validation`
- [ ] Update `EmailService.java`: `javax.mail` → `jakarta.mail`
- [ ] Verify `BatchConfig.java`: Spring Boot 3 requires `JobRepository` to be explicitly injected, check that your batch job definition is correct
- [ ] Verify `QuartzConfig.java`: check that dependency injection for `OrderExportJob` works correctly
- [ ] Verify MapStruct annotation processing works with JDK 17 (check the annotation processor path in pom.xml)

---

## Phase 7: Testing
- [ ] Run unit tests: `mvn test` → fix all failing tests
- [ ] Remove unnecessary `@ExtendWith(SpringExtension.class)` from tests (no longer needed with `@SpringBootTest`)
- [ ] Update Mockito version if needed to work with JDK 17
- [ ] Test all REST API endpoints
- [ ] Manually test the order export batch job → verify it produces correct CSV
- [ ] Test Kafka flow: verify export completion message is sent, email is triggered
- [ ] Test Quartz scheduling: verify the job triggers at the configured cron time
- [ ] Test all CRUD operations for Users, Products, Orders, Wishlist
- [ ] Verify database schema is generated correctly (Hibernate 6 may change column names/types slightly)

---

## Phase 8: Post-Upgrade
- [ ] Remove old workarounds for JDK 8/Spring Boot 2 that are no longer needed
- [ ] Optionally adopt JDK 17 features: records, sealed classes, pattern matching
- [ ] Update documentation to reflect new versions
- [ ] Run SonarQube analysis → fix any new issues
- [ ] Performance testing → check for any startup/response time regressions
- [ ] Deploy to staging environment for testing first
- [ ] After full validation, merge to main

---

## Common Gotchas
1. **`javax` → `jakarta` is the most common source of errors** → if you get "no persistence provider found" or startup errors related to annotations, you missed imports
2. **Hibernate 6 breaks many custom JPQL queries** due to stricter syntax → test all queries
3. **JDK 17 removes internal APIs** → some old third-party dependencies may need to be updated
4. **Spring Boot 3 changes auto-configuration** → some custom configuration may need to be adjusted
