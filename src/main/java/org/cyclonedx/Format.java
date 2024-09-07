package org.cyclonedx;

public enum Format {
    XML("xml", "application/vnd.cyclonedx+xml"),
    JSON("json", "application/vnd.cyclonedx+json");

    private final String extension;
    private final String mediaType;

    Format(String extension, String mediaType) {
        this.extension = extension;
        this.mediaType = mediaType;
    }

    /**
     * The file extension associated to this format.
     *
     * @return The file extension, excluding the dot.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * The official CycloneDX media type assigned by IANA for this format, see
     * <a href="https://cyclonedx.org/specification/overview/#registered-media-types">...</a>.
     *
     * @return The identifier for the media type.
     */
    public String getMediaType() {
        return mediaType;
    }
}
