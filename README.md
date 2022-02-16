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
validating, and parsing SBOMs. CycloneDX is a lightweight software bill of materials (SBOM) specification designed for 
use in application security contexts and supply chain component analysis.

Maven Usage
-------------------

```xml
<dependency>
    <groupId>org.cyclonedx</groupId>
    <artifactId>cyclonedx-core-java</artifactId>
    <version>6.0.0</version>
</dependency>
```

## CycloneDX Schema Support

The following table provides information on the version of this node module, the CycloneDX schema version supported, 
as well as the output format options. Use the latest possible version of this library that is the compatible with 
the CycloneDX version supported by the target system.

| Version | Schema Version | Format(s) |
| ------- | ----------------- | --------- |
| 6.x | CycloneDX v1.4 | XML/JSON |
| 5.x | CycloneDX v1.3 | XML/JSON |
| 4.x | CycloneDX v1.2 | XML/JSON |
| 3.x | CycloneDX v1.2 | XML/JSON |
| 2.x | CycloneDX v1.1 | XML |
| 1.x | CycloneDX v1.0 | XML |

## Library API Documentation

The library API documentation can be viewed online at [https://cyclonedx.github.io/cyclonedx-core-java/](https://cyclonedx.github.io/cyclonedx-core-java/).

Copyright & License
-------------------

CycloneDX Core (Java) is Copyright (c) OWASP Foundation. All Rights Reserved.

Permission to modify and redistribute is granted under the terms of the Apache 2.0 license. See the [License] file for the full license.

[License]: https://github.com/CycloneDX/cyclonedx-core-java/blob/master/LICENSE
