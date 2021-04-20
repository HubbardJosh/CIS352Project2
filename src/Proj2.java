import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Proj2 {

    ArrayList<Process> firstFitProcesses = new ArrayList<Process>();
    ArrayList<ArrayList<String>> firstFitFrames = new ArrayList<ArrayList<String>>();
    ArrayList<Integer> firstFitMemoryBlock = new ArrayList<Integer>();

    ArrayList<Process> bestFitProcesses = new ArrayList<Process>();
    ArrayList<ArrayList<String>> bestFitFrames = new ArrayList<ArrayList<String>>();
    ArrayList<Integer> bestFitMemoryBlock = new ArrayList<Integer>();

    ArrayList<Process> worstFitProcesses = new ArrayList<Process>();
    ArrayList<ArrayList<String>> worstFitFrames = new ArrayList<ArrayList<String>>();
    ArrayList<Integer> worstFitMemoryBlock = new ArrayList<Integer>();

    Timer timer = new Timer();
    int endTimer = 0;
    boolean endFirst = false;
    boolean endBest = false;
    boolean endWorst = false;

    public Proj2() {
        firstFitProcesses = generateProcesses();
        firstFitMemoryBlock = generateMemoryBlock();

        bestFitProcesses = generateProcesses();
        bestFitMemoryBlock = generateMemoryBlock();

        worstFitProcesses = generateProcesses();
        worstFitMemoryBlock = generateMemoryBlock();

        // set timer to run the algorithms once per second
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runFirstFit();
                runBestFit();
                runWorstFit();
                updateGUI();
            }
        }, 0, 1000);

    }

    // used to populate the same static process array for each algorithm
    private ArrayList<Process> generateProcesses() {
        ArrayList<Process> list = new ArrayList<Process>();
        list.add(new Process(1,3, 4));
        list.add(new Process(2,2, 3));
        list.add(new Process(3,9, 12));
        list.add(new Process(4,4, 5));
        list.add(new Process(5,3, 3));
        list.add(new Process(6,4, 5));
        list.add(new Process(7,3, 4));
        list.add(new Process(8,3, 4));
        list.add(new Process(9,9, 3));
        list.add(new Process(10,5, 6));
        list.add(new Process(11,6, 8));
        list.add(new Process(12,3, 7));
        list.add(new Process(13,3, 4));
        list.add(new Process(14,6, 7));
        list.add(new Process(15,7, 9));
        list.add(new Process(16,2, 3));
        list.add(new Process(17,3, 4));
        list.add(new Process(18,5, 6));
        list.add(new Process(19,15, 6));
        list.add(new Process(20,2, 3));

        return list;
    }

    // used to create a memory space
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
                next = x;
                count = next.getSize();
                break;
            }

        }


        if (count > 0) {
            for (int i = 0; i < firstFitMemoryBlock.size(); i++) {  // find start index of free memory hole
                if (firstFitMemoryBlock.get(i) == 0) {
                    startIndex = i; // set start index
                    count -= 1;
                    if ((i + next.getSize()) < (firstFitMemoryBlock.size() - 1)) {// enough room in memory to place process
                        for (int j = i+1; j < firstFitMemoryBlock.size(); j++) {  // find end index of free memory hole
                            if (firstFitMemoryBlock.get(j) == 0) {  // +1 to size of free memory hole
                                count -= 1;
                                if (count == 0) {   // free memory hole large enough to fit process
                                    endIndex = j;   // set end index
                                    next.setIsRunning();    // run this process
                                    break;
                                }
                            } else {
                                count = next.getSize(); // free memory hole not large enough for process
                                break;
                            }
                        }
                    } else {    // found hole for process, break out of loop
                        break;
                    }

                }
                if (endIndex > 0 || (i + next.getSize()) > (firstFitMemoryBlock.size() - 1)) {
                    break;    // found hole for process, break out of loop
                }
            }

            if (count == 0 && endIndex > 0) {

                for (int i = startIndex; i <= endIndex; i++) {  // place process in free memory hole
                    firstFitMemoryBlock.set(i, next.getID());
                }
             }
        }

        // set this current frame and save it to an array
        ArrayList<String> thisFrame = new ArrayList<String>();
        for (int x : firstFitMemoryBlock) {
            thisFrame.add(String.valueOf(x));
        }
        firstFitFrames.add(thisFrame);

        // handle process functions on each cycle
        for (Process x : firstFitProcesses) {
            if (x.getIsRunning() && x.getRunTime() > 0) {
                x.decrementRunTime();   // decrement remaining runtime for each running process
            }
            if (x.getRunTime() == 0) {  // runtime of a process ran out, take it out of memory
                x.setIsRunning();
                for (int i = 0; i < firstFitMemoryBlock.size(); i ++) {
                    if (firstFitMemoryBlock.get(i) == x.getID()) {
                        firstFitMemoryBlock.set(i, 0);
                    }
                }
            }

            if (!x.getIsFinished()) {   // a process still needs to finish running
                isFinished = false;
            }
        }

        if (isFinished) {   // all processes done running
            for (int x : firstFitMemoryBlock) {
                if (x != 0) {   // double check if a process needs to finish
                    isFinished = false;
                }
            }

            if (isFinished && !endFirst) {
                endFirst = true;
                endTimer += 1;


                System.out.println("\n\nFirst Fit");
                for (int j = 0; j < 20; j++) {  // print out frames for duration of algorithm
                    for (int i = 0; i < firstFitFrames.size(); i ++) {

                        System.out.print(firstFitFrames.get(i).get(j) + "\t");
                    }
                    System.out.println();
                }
            }

        }
    }

    private void runBestFit() {
        Process next = new Process(0,0,0);
        int startIndex = 0;
        int endIndex = 0;
        boolean isFinished = true;

        int compareStartIndex = 0;
        int compareEndIndex = 0;

        int shortestFittingStartIndex = 0;
        int shortestFittingEndIndex = 0;


        for (Process x : bestFitProcesses) {    // get next process that isn't currently running
            if (!x.getIsRunning() && !x.getIsFinished()) {
                next = x;
                break;
            }

        }

        if (next.getSize() > 0) {   // got a process that needs to run
            // cycle through memory to find space to put this process
            for (int i = 0; i < bestFitMemoryBlock.size(); i++) {
                if (bestFitMemoryBlock.get(i) == 0) {
                    startIndex = i; // set start index

                    // find end index
                    for (int j = i + 1; j < bestFitMemoryBlock.size(); j++) {
                        if (bestFitMemoryBlock.get(j) != 0 || j == bestFitMemoryBlock.size() - 1) {
                            // set end index
                            if (j == bestFitMemoryBlock.size() - 1) {
                                endIndex = j;
                                i = j - 1;
                            } else {
                                endIndex = j - 1;
                                i = j;
                            }

                            // exit loop
                            break;
                        }
                    }
                    if (endIndex > 0 && (endIndex - startIndex) >= next.getSize()) {
                        shortestFittingStartIndex = startIndex;
                        shortestFittingEndIndex = endIndex;
                        // set start/end index as the current shortest length to fit process

                        // exit loop
                        break;
                    }
                }
            }
            if (shortestFittingEndIndex > 0) {  // found a space to put process
                // search for shorter length hole
                for (int i = shortestFittingEndIndex + 1; i < bestFitMemoryBlock.size(); i++) {
                    if (bestFitMemoryBlock.get(i) == 0) {
                        compareStartIndex = i;  // set comparative start index
                        for (int j = i + 1; j < bestFitMemoryBlock.size(); j++) {
                            if (bestFitMemoryBlock.get(j) != 0 || j == bestFitMemoryBlock.size() - 1) {
                                // set comparative end index
                                if (j == bestFitMemoryBlock.size() - 1) {
                                    compareEndIndex = j;
                                    i = j - 1;
                                } else {
                                    compareEndIndex = j - 1;
                                    i = j;
                                }

                                break;
                            }
                        }

                        // check if comparative start/end index is smaller hole than previously found
                        // and set if true
                        if ((compareEndIndex - compareStartIndex) < (shortestFittingEndIndex - shortestFittingStartIndex)) {
                            if ((compareEndIndex - compareStartIndex) >= next.getSize() - 1) {
                                shortestFittingStartIndex = compareStartIndex;
                                shortestFittingEndIndex = compareEndIndex;
                            }
                        }
                    }
                }
            }
        }

        // place process in smallest free memory hole and start process
        if (shortestFittingEndIndex > 0) {
            for (int i = shortestFittingStartIndex; i <= (shortestFittingStartIndex + next.getSize() - 1); i++) {
                bestFitMemoryBlock.set(i, next.getID());
            }


            next.setIsRunning();
        }

        // populate algorithm total frame array
        ArrayList<String> thisFrame = new ArrayList<String>();
        for (int x : bestFitMemoryBlock) {
            thisFrame.add(String.valueOf(x));
        }
        bestFitFrames.add(thisFrame);

        // handle process stuff
        for (Process x : bestFitProcesses) {

            // decrement process runtime
            if (x.getIsRunning() && x.getRunTime() > 0) {
                x.decrementRunTime();
            }

            // take process out of memory if it is finished running
            if (x.getRunTime() == 0) {
                x.setIsRunning();
                for (int i = 0; i < bestFitMemoryBlock.size(); i ++) {
                    if (bestFitMemoryBlock.get(i) == x.getID()) {
                        bestFitMemoryBlock.set(i, 0);
                    }
                }
            }

            // check if a process still needs to run
            if (!x.getIsFinished()) {
                isFinished = false;
            }
        }

        // all processes done running
        if (isFinished) {

            // double check that all processes are done
            for (int x : bestFitMemoryBlock) {
                if (x != 0) {
                    isFinished = false;
                }
            }

            if (isFinished && !endBest) {
                endBest = true;
                endTimer += 1;


                // print out total frames to complete running algorithm
                System.out.println("\n\nBest Fit");
                for (int j = 0; j < 20; j++) {
                    for (int i = 0; i < bestFitFrames.size(); i ++) {
                            System.out.print(bestFitFrames.get(i).get(j) + "\t");
                    }
                    System.out.println();
                }

            }
        }
    }

    private void runWorstFit() {
        Process next = new Process(0,0,0);
        int startIndex = 0;
        int endIndex = 0;
        boolean isFinished = true;

        int compareStartIndex = 0;
        int compareEndIndex = 0;

        int longestFittingStartIndex = 0;
        int longestFittingEndIndex = 0;



        for (Process x : worstFitProcesses) {    // get next process that isn't currently running
            if (!x.getIsRunning() && !x.getIsFinished()) {
                next = x;
                break;
            }

        }

        if (next.getSize() > 0) {
            for (int i = 0; i < worstFitMemoryBlock.size(); i++) {
                if (worstFitMemoryBlock.get(i) == 0) {
                    startIndex = i; // set start index
                    for (int j = i + 1; j < worstFitMemoryBlock.size(); j++) {
                        if (worstFitMemoryBlock.get(j) != 0 || j == worstFitMemoryBlock.size() - 1) {
                            // set end index
                            if (j == worstFitMemoryBlock.size() - 1) {
                                endIndex = j;
                                i = j - 1;
                            } else {
                                endIndex = j - 1;
                                i = j;
                            }

                            break;
                        }
                    }
                    // if a hole large enough to fit process exists, set longest start/end index
                    if (endIndex > 0 && (endIndex - startIndex) >= next.getSize()) {
                        longestFittingStartIndex = startIndex;
                        longestFittingEndIndex = endIndex;

                        break;
                    }
                }
            }
            if (longestFittingEndIndex > 0) {
                for (int i = longestFittingEndIndex + 1; i < worstFitMemoryBlock.size(); i++) {
                    if (worstFitMemoryBlock.get(i) == 0) {
                        compareStartIndex = i;  // set comparative start index
                        for (int j = i + 1; j < worstFitMemoryBlock.size(); j++) {
                            if (worstFitMemoryBlock.get(j) != 0 || j == worstFitMemoryBlock.size() - 1) {
                                // set comparative end index
                                if (j == worstFitMemoryBlock.size() - 1) {
                                    compareEndIndex = j;
                                    i = j - 1;
                                } else {
                                    compareEndIndex = j - 1;
                                    i = j;
                                }

                                break;
                            }
                        }

                        // check if comparative is larger than the current longest
                        // and set if true
                        if ((compareEndIndex - compareStartIndex) > (longestFittingEndIndex - longestFittingStartIndex)) {
                            if ((compareEndIndex - compareStartIndex) >= next.getSize() - 1) {
                                longestFittingStartIndex = compareStartIndex;
                                longestFittingEndIndex = compareEndIndex;
                            }
                        }
                    }
                }
            }
        }

        // set process in the largest free memory hole and start running it
        if (longestFittingEndIndex > 0) {
            for (int i = longestFittingStartIndex; i <= (longestFittingStartIndex + next.getSize() - 1); i++) {
                worstFitMemoryBlock.set(i, next.getID());
            }

            next.setIsRunning();
        }

        // set current frame in an array
        ArrayList<String> thisFrame = new ArrayList<String>();
        for (int x : worstFitMemoryBlock) {
            thisFrame.add(String.valueOf(x));
        }
        worstFitFrames.add(thisFrame);

        // handle all process stuff
        for (Process x : worstFitProcesses) {
            // decrement process runtime
            if (x.getIsRunning() && x.getRunTime() > 0) {
                x.decrementRunTime();
            }
            // process is done running, free memory
            if (x.getRunTime() == 0) {
                x.setIsRunning();
                for (int i = 0; i < worstFitMemoryBlock.size(); i ++) {
                    if (worstFitMemoryBlock.get(i) == x.getID()) {
                        worstFitMemoryBlock.set(i, 0);
                    }
                }
            }

            // processes still need to run
            if (!x.getIsFinished()) {
                isFinished = false;
            }
        }

        // all processes finished running
        if (isFinished) {

            // double check that all processes are done
            for (int x : worstFitMemoryBlock) {
                if (x != 0) {
                    isFinished = false;
                }
            }

            // handle algorithm endgame
            if (isFinished && !endWorst) {
                endWorst = true;
                endTimer += 1;


                // print frames of algorithm runtime
                System.out.println("\n\nWorst Fit");
                for (int j = 0; j < 20; j++) {
                    for (int i = 0; i < worstFitFrames.size(); i ++) {
                        System.out.print(worstFitFrames.get(i).get(j) + "\t");
                    }
                    System.out.println();
                }
            }
        }
    }

    private void updateGUI() {
        // update GUI here
        // use either the FitMemoryBlock or FitFrames arrays to update GUI accordingly

        // cancel timer when all three algorithms are done running
        if (endTimer == 3) {
            timer.cancel();
        }
    }

    public static void main(String[] args) {
        Proj2 test = new Proj2();
    }

}

