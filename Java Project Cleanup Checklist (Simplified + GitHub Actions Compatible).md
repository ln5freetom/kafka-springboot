# Java Project Cleanup Checklist \(Simplified \+ GitHub Actions Compatible\)

This checklist includes two practical versions, adapted for daily manual cleanup and automated verification scenarios respectively\. Its core goal is to help developers efficiently detect and handle unused dependencies and deprecated code in Java projects, reduce project redundancy, lower maintenance costs, and avoid potential compatibility and security risks\.

## I\. Simplified Version \(One\-Page for Daily Use\)

### 🧹 Unused Dependencies Check

- \[ \] Maven: Execute the command `mvn dependency:analyze`\. This command will automatically scan project dependencies and clearly display \&\#34;unused declared dependencies\&\#34; and \&\#34;used undeclared dependencies\&\#34;, making it easy to quickly locate redundant dependencies\.

- \[ \] Gradle: First apply the unused\-dependencies plugin in the project\&\#39;s build\.gradle file, then execute the command `gradle unusedDependencies`\. The plugin will accurately identify dependency packages that are not actually called in the project\.

- \[ \] Exclude false positives related to reflection/plugins\. Some dependencies, although not directly and explicitly called, may be relied on by the reflection mechanism, third\-party plugins, or optional modules in the project\. It is necessary to manually verify before deleting confirmed unused dependencies to avoid project abnormalities caused by incorrect deletion\.

### ⚠️ Deprecated Code Check

- \[ \] Check for strikethrough code in the IDE \(IDEs will by default mark deprecated methods and classes with strikethrough\), and perform a global check for deprecated code\. In IntelliJ, you can use the path \&\#34;Analyze → Run Inspection by Name → Deprecated\&\#34;; in Eclipse, you can use \&\#34;Warnings → Deprecated API\&\#34; to quickly scan the entire project for deprecated code\.

- \[ \] Maven: Enable compilation deprecation warnings in the maven\-compiler\-plugin of pom\.xml, then execute the command `mvn compile`\. The compilation log will print the location and reason for deprecation of the deprecated code in detail, facilitating precise positioning\.

- \[ \] \(Optional\) Execute `mvn spotbugs:gui` for in\-depth detection\. The SpotBugs tool can not only identify deprecated code but also link with the FindSecBugs plugin to simultaneously detect potential security risks caused by deprecated code, which is suitable for scenarios with high requirements for project quality\.

- \[ \] Replace deprecated code with recommended alternatives\. Check the Javadoc comments of the deprecated code to find the officially recommended alternative methods or classes, and replace them one by one to avoid code failure due to subsequent JDK or dependency version upgrades\.

### ✅ Final Verification

- \[ \] Rebuild the project by executing mvn clean compile \(Maven\) or \./gradlew clean compile \(Gradle\) to ensure there are no compilation errors after the cleanup operation, and dependencies and code references are normal\.

- \[ \] Run tests\. Execute all unit tests and integration tests of the project to confirm that the cleanup does not affect the original functions of the project, and there are no functional abnormalities or failed test cases\.

- \[ \] Commit cleanup changes\. Submit the changes such as deleted unused dependencies and replaced deprecated code separately, and mark clear commit information to facilitate subsequent traceability and rollback \(if exceptions occur\)\.

## II\. GitHub Actions Compatible Version \(Automated Verification\)

This version can be directly copied to the \.github/workflows directory of the project, named java\-cleanup\.yml, to realize automated detection during code submission and PR merging, reduce manual verification costs, and ensure the project remains clean for a long time\.

```yaml
name: Java Project Cleanup Check
on:
  push:
    branches: [ main, develop ]  # Monitor push operations on the main and develop branches
  pull_request:
    branches: [ main, develop ]  # Monitor PR submissions to the main and develop branches

jobs:
  check-unused-deps:
    runs-on: ubuntu-latest  # Run this task on the Ubuntu system
    steps:
      - name: Checkout code
        uses: actions/checkout@v4  # Pull the current project code
      
      - name: Set up JDK 11
        uses: actions/setup-java@v4
        with:
          java-version: '11'
          distribution: 'temurin'  # Use Eclipse Temurin JDK, stable and compatible with mainstream Java projects
      
      - name: Check Unused Dependencies (Maven)
        if: contains(github.workspace, 'pom.xml')  # If the project has pom.xml, execute Maven dependency detection
        run: |
          mvn dependency:analyze
          # Check if there are unused dependencies in the output; if yes, report an error and terminate the process
          if mvn dependency:analyze | grep -q "Unused declared dependencies"; then
            echo "Error: Found unused dependencies"
            exit 1
          fi
      
      - name: Check Unused Dependencies (Gradle)
        if: contains(github.workspace, 'build.gradle')  # If the project has build.gradle, execute Gradle dependency detection
        run: |
          ./gradlew unusedDependencies
          if ./gradlew unusedDependencies | grep -q "Unused dependencies found"; then
            echo "Error: Found unused dependencies"
            exit 1
          fi

  check-deprecated-code:
    runs-on: ubuntu-latest  # Use the same system as the dependency detection task to maintain environment consistency
    steps:
      - name: Checkout code
        uses: actions/checkout@v4  # Pull the code again (each job is independent and needs to be pulled again)
      
      - name: Set up JDK 11
        uses: actions/setup-java@v4
        with:
          java-version: '11'
          distribution: 'temurin'
      
      - name: Check Deprecated Code (Maven)
        if: contains(github.workspace, 'pom.xml')
        run: |
          # Enable deprecation warnings and execute compilation to ensure all deprecation-related warnings are captured
          mvn compile -Dmaven.compiler.showDeprecation=true
          # Check if there are deprecation warnings in the compilation log; by default, only prompt a warning without terminating the process
          if mvn compile -Dmaven.compiler.showDeprecation=true | grep -q "deprecated"; then
            echo "Warning: Found deprecated code"
            # exit 1 # Uncomment this line if strict verification is required (block submission/PR if deprecated code exists)
          fi
      
      - name: Check Deprecated Code (Gradle)
        if: contains(github.workspace, 'build.gradle')
        run: |
          # Enable all warning modes to ensure deprecation warnings are captured
          ./gradlew compileJava --warning-mode=all
          if ./gradlew compileJava --warning-mode=all | grep -q "deprecated"; then
            echo "Warning: Found deprecated code"
            # exit 1 # Uncomment this line if strict verification is required
          fi
```

### GitHub Actions Checklist Instructions

- \[ \] Maven/Gradle automatic detection branches \(main/develop\) are configured\. You can add or delete branches to be monitored \(such as release branches\) according to the actual branch structure of the project\.

- \[ \] Unused dependency detection: The default configuration is \&\#34;report an error if there are unused dependencies\&\#34;\. You can modify the script according to project needs, change exit 1 to an echo prompt, and switch to only warning without terminating the process\.

- \[ \] Deprecated code detection: The default configuration is \&\#34;prompt a warning if there is deprecated code\&\#34;, which does not affect submission and PR merging; if strict verification is required \(prohibiting the existence of deprecated code\), uncomment exit 1 in the script\.

- \[ \] Adapted to JDK 11\. You can modify the value of java\-version according to the actual JDK version used by the project \(such as 8, 17\) to ensure the detection environment is consistent with the project\&\#39;s running environment\.

- \[ \] It can be directly copied to the project\&\#39;s workflows directory for activation without additional configuration\. GitHub will automatically identify and execute this workflow, and automatically complete the detection every time code is pushed or a PR is submitted\.

> （注：文档部分内容可能由 AI 生成）
