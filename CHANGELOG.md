# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [7.1.0] - 2022-02-24
### Added
- `CHANGELOG.md`, due to increasing major releases, a change log has been added to help further elaborate
on why a release occurred, when, and major points related to it.

### Removed
- `PropertiesDeserializer.java` and the associated test contributed from Lockheed Martin. 
  The changed was predicated by a license header that is incompatible with the Apache 2.0 License, and
  causing some consumers grief. More at [the issue where this was reported](https://github.com/CycloneDX/cyclonedx-core-java/issues/178).
  Of note, a major release was skipped as this functionality was controlled by a property, and had to be opted in to.
  
## [7.0.0] - 2022-02-22
### Changed
- `toJsonObject` was changed to `toJsonNode`, removing a dependency on org.glassfish json-api, as well as javax json-api.
This was done because those dependencies are GPLv2 with Classpath Exception, and while they can be likely used with
  minimal grief, they still raise eyebrows due to the license being associated with GPLv2. This method was modified to
  return the Jackson equivalent of `JsonObject`.
  
## [6.0.0] - 2022-02-16
### Added
- Support for CycloneDX 1.4 Schema in XML, JSON and protobuf (schema only for protobuf).
- Notable support of `vulnerabilities` object, previously an extension. Limited support for the extension left in place.
