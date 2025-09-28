[![Build Status](https://github.com/CycloneDX/cyclonedx-core-java/workflows/Maven%20CI/badge.svg)](https://github.com/CycloneDX/cyclonedx-core-java/actions?workflow=Maven+CI)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.cyclonedx/cyclonedx-core-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.cyclonedx/cyclonedx-core-java)
[![License](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)][License]
[![Website](https://img.shields.io/badge/https://-cyclonedx.org-blue.svg)](https://cyclonedx.org/)
[![Slack Invite](https://img.shields.io/badge/Slack-Join-blue?logo=slack&labelColor=393939)](https://cyclonedx.org/slack/invite)
[![Group Discussion](https://img.shields.io/badge/discussion-groups.io-blue.svg)](https://groups.io/g/CycloneDX)
[![Twitter](https://img.shields.io/twitter/url/http/shields.io.svg?style=social&label=Follow)](https://twitter.com/CycloneDX_Spec)


CycloneDX Core (Java)
=========

The CycloneDX core module provides a model representation of the SBOM along with utilities to assist in creating, 
validating, and parsing SBOMs. OWASP CycloneDX is a full-stack Bill of Materials (BOM) standard that provides advanced
supply chain capabilities for cyber risk reduction

Maven Usage
-------------------

```xml
<dependency>
    <groupId>org.cyclonedx</groupId>
    <artifactId>cyclonedx-core-java</artifactId>
    <version>10.1.0</version>
</dependency>
```

## CycloneDX Schema Support

The following table provides information on the version of this node module, the CycloneDX schema version supported, 
as well as the output format options. Use the latest possible version of this library that is the compatible with 
the CycloneDX version supported by the target system.

| Version |  Schema Version  | Format(s) |
|---------|------------------|-----------|
| 10.x    | CycloneDX v1.6.1 | XML/JSON  |
| 9.x     | CycloneDX v1.6   | XML/JSON  |
| 8.x     | CycloneDX v1.5   | XML/JSON  |
| 7.x     | CycloneDX v1.4   | XML/JSON  |
| 6.x     | CycloneDX v1.4   | XML/JSON  |
| 5.x     | CycloneDX v1.3   | XML/JSON  |
| 4.x     | CycloneDX v1.2   | XML/JSON  |
| 3.x     | CycloneDX v1.2   | XML/JSON  |
| 2.x     | CycloneDX v1.1   | XML       |
| 1.x     | CycloneDX v1.0   | XML       |

## Library API Documentation

The library API documentation can be viewed online at [https://cyclonedx.github.io/cyclonedx-core-java/](https://cyclonedx.github.io/cyclonedx-core-java/).

## Updating the license list
1. Download the latest tagged release from [this repo](https://github.com/spdx/license-list-data/tags).
2. Extract the archived directory.
3. Navigate to the `license-list-vX.X.X/text/` directory.
4. Copy all licenses from that directory to the `src/main/java/resources/licenses/` directory in this repo.
5. Copy `license-list-vX.X.X/json/licenses.json` into the `src/main/java/resources/licenses/` directory in this repo.
6. Download [this file](http://cyclonedx.org/schema/spdx.schema.json) (ex: `curl http://cyclonedx.org/schema/spdx.schema.json -o spdx.schema.json`). The `$comment` field should match the version you donwloaded from GitHub. Copy this file into `src/main/resources/`.
7. Download [this file](https://cyclonedx.org/schema/spdx.xsd) (ex `curl https://cyclonedx.org/schema/spdx.xsd -o spdx.xsd`). The version field should match the version you donwloaded from GitHub. Copy this file into `src/main/resources/`. 

Copyright & License
-------------------

CycloneDX Core (Java) is Copyright (c) OWASP Foundation. All Rights Reserved.

Permission to modify and redistribute is granted under the terms of the Apache 2.0 license. See the [License] file for the full license.

[License]: https://github.com/CycloneDX/cyclonedx-core-java/blob/master/LICENSE
