package org.cyclonedx;

public enum Format {
    XML("xml"),
    JSON("json");

    private final String extension;

    Format(String extension) {
        this.extension = extension;
    }

    /**
     * The file extension associated to this format.
     *
     * @return The file extension, excluding the dot.
     */
    public String getExtension() {
        return extension;
    }
}
