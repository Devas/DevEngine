package display;

public enum Resolution {

    HD720(1280, 720), HD768(1366, 768), HD900(1600, 900), HD1080(1920, 1080), WQHD(2560, 1440), UHD(3840, 2160), // 16:9
    UWHD(2560, 1080), UWQHD(3440, 1440);                                                                         // 21:9

    public int width;
    public int height;

    Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
