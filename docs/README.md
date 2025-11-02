# Platform Gradle Plugins - Documentation

Welcome to the comprehensive documentation for Platform Gradle Plugins.

## Documentation Structure

### Architecture
Understand the design decisions and system architecture:

- **[Architecture Decision Records (ADRs)](./architecture/decisions/)** - Design decisions and rationale
  - [ADR 001: Spotless Over Checkstyle](./architecture/decisions/001-spotless-over-checkstyle.md)

- **[Architecture Diagrams](./architecture/)** - Visual system documentation
  - [Plugin Composition](./architecture/plugin-composition.md) - How plugins layer together with visual diagrams

### Guides
Step-by-step guides for common tasks:

- **[Migration Guide](./guides/migration-guide.md)** - Adopt these plugins in your projects

## Quick Links

- [Main README](../README.md) - Project overview and quick start
- [Contributing Guide](../CONTRIBUTING.md) - Development workflow
- [Makefile Commands](../Makefile) - Available build commands

## Reading Order for New Contributors

1. Start with the main [README](../README.md)
2. Read [ADR 001: Spotless Over Checkstyle](./architecture/decisions/001-spotless-over-checkstyle.md)
3. Review [Plugin Composition diagrams](./architecture/plugin-composition.md)
4. Check out the [Migration Guide](./guides/migration-guide.md)
5. Follow the [Contributing Guide](../CONTRIBUTING.md)

## For Platform Engineers

If you're evaluating these plugins for adoption:

1. **Understand the Architecture** - Read ADRs to understand design trade-offs
2. **Visualize the System** - Review Mermaid diagrams
3. **Test Migration** - Follow the migration guide on a small project
4. **Customize as Needed** - Check customization guide *(coming soon)*

## For Developers Using These Plugins

Quick references:

- How do I format my code? → `./gradlew spotlessApply`
- How do I check formatting? → `./gradlew spotlessCheck`
- Where are coverage reports? → `build/reports/jacoco/test/html/index.html`
- How do I override versions? → See [Customization Guide](./guides/customization-guide.md) *(coming soon)*

## Contributing to Documentation

Documentation improvements are always welcome!

- Found a typo? Open a PR
- Missing a diagram? Propose one in an issue
- Have a better example? Share it
- Unclear explanation? Let us know

See [CONTRIBUTING.md](../CONTRIBUTING.md) for details.

---

**Documentation Version:** 1.0.0
**Last Updated:** 2025-01-02
**Maintainer:** Balamurugan Elangovan
