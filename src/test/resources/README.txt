The versioned resource directories (e.g. 1.0, 1.1, 1.2, etc) originate from https://github.com/CycloneDX/specification.
These files test the schema itself to ensure they are valid.

CycloneDX Core Java leverages this files for both validity tests and deserialization tests. If files are added to these
versioned directories, they should also likely be added to https://github.com/CycloneDX/specification as well.
