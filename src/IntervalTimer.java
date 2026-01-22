public class IntervalTimer {
    private int duration;
    private int timeLeft;

    public IntervalTimer(int time) {
        this.duration = time;
        this.timeLeft = time;
    }

    public void reset() {
        timeLeft = duration;
    }

    public boolean tick() {
        if(timeLeft > 0) {
            timeLeft--;
            return true;
        }
        return false;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setDuration(int seconds) {
        this.duration = seconds;
        this.timeLeft = seconds;
    }
}