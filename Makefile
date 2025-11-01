.PHONY: help build test clean publish-local check-versions setup-hooks

help:
	@echo "Platform Gradle Plugins - Available Commands"
	@echo "=============================================="
	@echo ""
	@echo "  build           - Build the plugins"
	@echo "  test            - Run tests"
	@echo "  clean           - Clean build artifacts"
	@echo "  publish-local   - Publish to Maven Local"
	@echo "  check-versions  - Show current dependency versions"
	@echo "  setup-hooks     - Install pre-commit hooks"
	@echo ""

build:
	./gradlew build

test:
	./gradlew test

clean:
	./gradlew clean

publish-local:
	@echo "📦 Publishing platform-gradle-plugins to Maven Local..."
	@./gradlew publishToMavenLocal
	@echo ""
	@echo "✅ Published successfully!"
	@echo "📍 Location: ~/.m2/repository/io/github/platform/"
	@echo ""
	@echo "📝 Use in your projects:"
	@echo ""
	@echo "  // settings.gradle.kts"
	@echo "  pluginManagement {"
	@echo "      repositories {"
	@echo "          mavenLocal()"
	@echo "          gradlePluginPortal()"
	@echo "      }"
	@echo "  }"
	@echo ""
	@echo "  // build.gradle.kts"
	@echo "  plugins {"
	@echo "      id(\"io.github.platform.spring-conventions\") version \"1.0.0\""
	@echo "  }"

check-versions:
	@./gradlew -q displayVersions

setup-hooks:
	@echo "🔧 Setting up pre-commit hooks..."
	@if ! command -v pre-commit >/dev/null 2>&1; then \
		echo "❌ pre-commit not found. Installing..."; \
		if command -v brew >/dev/null 2>&1; then \
			echo "📦 Installing pipx..."; \
			brew install pipx 2>&1 | grep -v "Warning:" || true; \
			pipx ensurepath 2>&1 | grep -v "⚠️" || true; \
			echo "📦 Installing pre-commit via pipx..."; \
			pipx install pre-commit; \
		elif command -v python3.12 >/dev/null 2>&1; then \
			echo "📦 Using Python 3.12..."; \
			python3.12 -m pip install --user --break-system-packages pre-commit; \
		elif command -v python3 >/dev/null 2>&1; then \
			echo "📦 Using system Python..."; \
			python3 -m pip install --user pre-commit 2>/dev/null || \
			python3 -m pip install --user --break-system-packages pre-commit; \
		else \
			echo "❌ Python not found. Please install Python 3.12+ first:"; \
			echo "   brew install python@3.12"; \
			exit 1; \
		fi; \
	fi
	@echo "🔗 Installing git hooks..."
	@pre-commit install || { \
		echo "⚠️  Command not in PATH. Using full path..."; \
		$$HOME/.local/bin/pre-commit install; \
	}
	@pre-commit install --hook-type commit-msg || { \
		$$HOME/.local/bin/pre-commit install --hook-type commit-msg; \
	}
	@echo ""
	@echo "✅ Pre-commit hooks installed successfully!"
	@echo ""
	@echo "📝 Hooks configured:"
	@echo "  - Trailing whitespace removal"
	@echo "  - End-of-file fixer"
	@echo "  - YAML validation"
	@echo "  - Kotlin formatting (ktlint)"
	@echo "  - Commit message linting (conventional commits)"
	@echo ""
	@echo "💡 Run 'pre-commit run --all-files' to check all files"
