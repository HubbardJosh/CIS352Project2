import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Proj2 {

    ArrayList<Process> firstFitProcesses = new ArrayList<Process>();
    ArrayList<String> firstFitFrames = new ArrayList<String>();
    ArrayList<Integer> firstFitMemoryBlock = new ArrayList<Integer>();

    ArrayList<Process> bestFitProcesses = new ArrayList<Process>();
    ArrayList<String> bestFitFrames = new ArrayList<String>();

    ArrayList<Process> worstFitProcesses = new ArrayList<Process>();
    ArrayList<String> worstFitFrames = new ArrayList<String>();

    Timer firstFitTimer = new Timer();

    public Proj2() {
        firstFitProcesses = generateProcesses();
        firstFitMemoryBlock = generateMemoryBlock();

        bestFitProcesses = generateProcesses();
        worstFitProcesses = generateProcesses();


        firstFitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runFirstFit();
            }
        }, 0, 200);

//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runBestFit(bestFitProcesses);
//            }
//        }, 0, 1000);
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runWorstFit(worstFitProcesses);
//            }
//        }, 0, 1000);
    }

    private ArrayList<Process> generateProcesses() {
        ArrayList<Process> list = new ArrayList<Process>();
        list.add(new Process(1,3, 4));
        list.add(new Process(2,2, 3));
        list.add(new Process(3,9, 12));
        list.add(new Process(4,4, 5));
        list.add(new Process(5,2, 3));
        list.add(new Process(6,4, 5));
        list.add(new Process(7,3, 4));
        list.add(new Process(8,3, 4));
        list.add(new Process(9,9, 12));
        list.add(new Process(10,5, 6));
        list.add(new Process(11,6, 8));
        list.add(new Process(12,6, 7));
        list.add(new Process(13,3, 4));
        list.add(new Process(14,6, 7));
        list.add(new Process(15,7, 9));
        list.add(new Process(16,2, 3));
        list.add(new Process(17,3, 4));
        list.add(new Process(18,5, 6));
        list.add(new Process(19,5, 6));
        list.add(new Process(20,2, 3));

        return list;
    }

    private ArrayList<Integer> generateMemoryBlock() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < 20; i++) {
            list.add(0);
        }

        return list;
    }

    private void runFirstFit() {
        Process next = new Process(0,0,0);
        int count = 0;
        int startIndex = 0;
        int endIndex = 0;
        boolean isFinished = true;

        for (Process x : firstFitProcesses) {    // get next process that isn't currently running
            if (!x.getIsRunning() && !x.getIsFinished()) {
//                System.out.println("Process ID: " + x.getID() + " size: " + x.getSize() + "\n\n");
                next = x;
                count = next.getSize();
                break;
            }

        }


        if (count > 0) {
            for (int i = 0; i < firstFitMemoryBlock.size(); i++) {  // find start index of free memory hole
                if (firstFitMemoryBlock.get(i) == 0) {
                    startIndex = i;
//                    System.out.print("Trying startIndex: " + startIndex + "\n");
                    count -= 1;
                    if ((i + next.getSize()) < (firstFitMemoryBlock.size() - 1)) {
                        for (int j = i+1; j < firstFitMemoryBlock.size(); j++) {  // find end index of free memory hole
                            if (firstFitMemoryBlock.get(j) == 0) {
                                count -= 1;
                                if (count == 0) {
                                    endIndex = j;
//                                    System.out.print("Found endIndex: " + endIndex + "\n");
                                    next.setIsRunning();
                                    break;
                                }
                            } else {
                                count = next.getSize();
                                break;
                            }
                        }
                    } else {
                        break;
                    }

                }
                if (endIndex > 0 || (i + next.getSize()) > (firstFitMemoryBlock.size() - 1)) {
//                    System.out.println(startIndex + " " + endIndex);
                    break;
                }
            }

            if (count == 0 && endIndex > 0) {

                for (int i = startIndex; i <= endIndex; i++) {
                    firstFitMemoryBlock.set(i, next.getID());
                }
             }
        }

        for (int x : firstFitMemoryBlock) {
            System.out.println(x);
        }
        System.out.println();

        for (Process x : firstFitProcesses) {
            if (x.getIsRunning() && x.getRunTime() > 0) {
                x.decrementRunTime();
            }
            if (x.getRunTime() == 0) {
                x.setIsRunning();
                for (int i = 0; i < firstFitMemoryBlock.size(); i ++) {
                    if (firstFitMemoryBlock.get(i) == x.getID()) {
                        firstFitMemoryBlock.set(i, 0);
                    }
                }
            }

            if (!x.getIsFinished()) {
                isFinished = false;
            }
        }

        if (isFinished) {
            for (int x : firstFitMemoryBlock) {
                if (x != 0) {
                    isFinished = false;
                }
            }

            if (isFinished) {
                firstFitTimer.cancel();
            }

        }
//        this.firstFitProcesses = list;
    }

    private void runBestFit(ArrayList<Process> list) {

        this.bestFitProcesses = list;
    }

    private void runWorstFit(ArrayList<Process> list) {

        this.worstFitProcesses = list;
    }

    public static void main(String[] args) {
        Proj2 test = new Proj2();


//        test();
    }

}

