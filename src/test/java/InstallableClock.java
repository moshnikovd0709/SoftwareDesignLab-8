import java.time.Instant;

public class InstallableClock implements Clock {
    private Instant now;

    public InstallableClock(Instant now) {
        this.now = now;
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    public Instant getNow() {
        return now;
    }

    @Override
    public Instant now() {
        return now;
    }
}
