package core.settings;

public enum APIEndpoints {
    PING("/ping"),
    BOOKING("/booking");

    private final String path;

    APIEndpoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
