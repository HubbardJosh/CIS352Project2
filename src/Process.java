public class Process {
    private int id;
    private int size;
    private int runTime;
    private boolean isRunning;
    private boolean finished;

    public Process (int id, int size, int runTime) {
        this.id = id;
        this.size = size;
        this.runTime = runTime;
        this.isRunning = false;
        this.finished = false;
    }

    public int getID() {
        return this.id;
    }

    public int getSize() {
        return this.size;
    }

    public int getRunTime() {
        return this.runTime;
    }

    public void decrementRunTime() {
        this.runTime -= 1;
    }

    public boolean getIsRunning() {
        return this.isRunning;
    }

    public boolean getIsFinished() {
        return this.finished;
    }

    public void setIsRunning() {
        if (!this.isRunning) {
            this.isRunning = true;
        } else {
            this.isRunning = false;
            this.finished = true;
        }

    }
}
