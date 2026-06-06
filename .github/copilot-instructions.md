# Copilot Instructions for javabean-tester

## Required toolchain
- JDK for builds: one of `[21,22)`, `[25,26)`, `[26,27)`, `[27,28)` (enforced by Maven Enforcer).
- Language target: Java 17 bytecode/source (`maven.compiler.release=17`).
- Maven: `>= 3.9.16` (use `./mvnw`).

## Build commands
- Full verification (recommended):
  - `./mvnw verify --batch-mode --no-transfer-progress --show-version -Dlicense.skip=true`
- Package only:
  - `./mvnw package --batch-mode --no-transfer-progress -Dlicense.skip=true`

## Test commands
- All tests (matches CI):
  - `./mvnw test --batch-mode --no-transfer-progress --show-version -Dlicense.skip=true`
- Single test class:
  - `./mvnw -Dtest=JavaBeanTesterTest test --batch-mode --no-transfer-progress -Dlicense.skip=true`

## Project structure
- `src/main/java/com/codebox/bean`: main public API and worker logic (`JavaBeanTester*`, `ValueBuilder`, `ByteBuddyBeanCopier`).
- `src/main/java/com/codebox/instance`: constructor/instance creation helpers.
- `src/main/java/com/codebox/builders`: runtime extension generation.
- `src/main/java/com/codebox/enums`: option flags used by the fluent builder.
- `src/main/java/com/codebox/util`: utility helpers.
- `src/test/java/com/codebox/...`: behavior and regression tests; examples of intended usage.
- `.github/workflows/*.yaml`: CI matrix and release/security automation.

## Coding conventions inferred from codebase
- Keep Apache-2.0 SPDX header block at top of Java files.
- 4-space indentation and organized imports (formatter + impsort plugins).
- Prefer `final` parameters and explicit access modifiers.
- Keep fluent API behavior backward compatible (`JavaBeanTester.builder(...).*.test()`).
- Use JUnit 5 tests (`org.junit.jupiter`) and existing assertion styles.
- Favor small, targeted changes; do not introduce unnecessary dependencies.

## Areas not to modify casually
- `pom.xml` enforcer/toolchain/compiler/jacoco settings (build gatekeepers).
- Public API classes in `com.codebox.bean` (`JavaBeanTester`, `JavaBeanTesterBuilder`).
- Reflection/bytecode logic in `JavaBeanTesterWorker`, `ValueBuilder`, `ByteBuddyBeanCopier`, and `builders/ExtensionBuilder`.
- CI workflow Java matrix and verification flags in `.github/workflows/ci.yaml`.

## Typical bug-fix workflow
1. Reproduce with an existing failing test or add a focused regression test under `src/test/java`.
2. Implement minimal fix in the relevant `src/main/java/com/codebox/...` class.
3. Run targeted test(s), then run full verification with JDK 21+:
   - `./mvnw verify --batch-mode --no-transfer-progress --show-version -Dlicense.skip=true`
4. Confirm no API behavior regressions in builder/worker paths.
5. Keep patch small, update tests with the fix, and avoid unrelated refactors.
