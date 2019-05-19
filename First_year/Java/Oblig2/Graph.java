import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class Graph {
    private static int taskAmount;
    private ArrayList<Task> allTasks = new ArrayList<Task>();
    ArrayList<Task> path = new ArrayList<Task>();
    boolean hasLoop = false;
    int endTime;

// ****************************************************************************
// Metode for aa lese inn fil.
    public void readFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            try{
                int firstNumber = scanner.nextInt();
                taskAmount = firstNumber;
            }
            catch(NoSuchElementException e) {
                System.out.println("* FILEN INNEHOLDER INGEN ORD! *");
                System.exit(1);
            }

            for(int i = 0; i < taskAmount; i++) {
                int id = scanner.nextInt();
                String name = scanner.next();
                int time = scanner.nextInt();
                int manpower = scanner.nextInt();
                ArrayList<Integer> innEdgesInt = new ArrayList<Integer>();
                int pre = scanner.nextInt();

                while(pre != 0) {
                    innEdgesInt.add(pre);
                    pre = scanner.nextInt();
                }

             Task thatnewnew = new Task(id, name, time, manpower, innEdgesInt);
                allTasks.add(thatnewnew);
            }

            setEdges();
            //setOutedges();
            //setInedges();

            for(Task root : allTasks) {
                if(root.getInedgesInt().size() == 0) {
                    if(cycle(root)) {
 System.out.println("\n**************** PROGRAM AVSLUTTES ****************\n");
                        System.exit(1);
                    }
                    else {
                        /* To for-each loekker som fikser earliest og latest
                           start. */
                        for(Task t : allTasks) {
                            if(t.getInedgesInt().size() == 0) {
                                t.setEarlieststart(0);
                            }
                        }

                        setLatestLastNodes();

                        for(Task t : allTasks) {
                            if(t.getOutedges().isEmpty()) {
                                setLateststart(t);
                            }
                        }

                        for(Task t : allTasks) {
                            t.setSlack(t.getLateststart() -
                            t.getEarliestStart());
                        }
                    }
                }
            }

            printALL();
        }
        catch(FileNotFoundException e) {
            System.out.println("* FANT IKKE FILEN! *");
            System.exit(1);
        }
    }

// ****************************************************************************
// Setter ALLE edgesene til Taskene
    public void setEdges() {
        for(Task t : allTasks) {
            for(int i : t.getInedgesInt()) {
                allTasks.get(i - 1).addOutedge(t);
                t.addInedge(allTasks.get(i-1));
            }
        }
    }

// ****************************************************************************
    /*
        Dette er slik jeg opprinnelig satt innedges og outedges. Med denne
        maaten vil nodene bli satt riktig uansett om filen har alle tasks i
        riktig rekkefoelge eller ikke. Ulempen med disse to metodene er at
        begge bruker tre for-loekker inn i hverandre, som vil gi en
        O - notasjon som vil ta tid. Saa; istedenfor aa gjoere det paa maaten
        under, har jeg valgt aa lage en metode over, setEdges(), som setter
        alt. Baade inedges og outedges. Dette GITT AT filer vi bruker, har
        tasks i riktig rekkefoelge. Skriver dette ogsaa i readme.txt under
        "assumptions".
    */

// Setter outedgesene til taskene
    // public void setOutedges() {
    //     for(Task t : allTasks) {
    //         for(int i : t.getInedgesInt()) {
    //             for(Task k : allTasks) {
    //                 if(k.getID() == i) {
    //                     k.addOutedge(t);
    //                 }
    //             }
    //         }
    //     }
    // }
// Setter inedgesene til taskene
    // public void setInedges() {
    //     for(Task t : allTasks) {
    //         for(int i : t.getInedgesInt()) {
    //             for(Task k : allTasks) {
    //                 if(k.getID() == i) {
    //                     t.addInedge(k);
    //                 }
    //             }
    //         }
    //     }
    // }

// ****************************************************************************
// Metode som setter latestStart paa de siste noedene i grafen
    public void setLatestLastNodes() {
        ArrayList<Task> buffer = new ArrayList<Task>();

        for(Task t : allTasks) {
            if(t.getOutedges().isEmpty()) {
                buffer.add(t);
            }
        }

        int biggest = 0;
        for(Task t : buffer) {
            if((t.getEarliestStart() + t.getTime()) > biggest) {
                biggest = (t.getEarliestStart() + t.getTime());
            }
        }

        for(Task t : allTasks) {
            if(t.getOutedges().isEmpty()) {
                t.setLatest(biggest - t.getTime());
            }
        }

        endTime = biggest;
    }

// ****************************************************************************
// Metode som setter latestStart
    public void setLateststart(Task t) {
        if(t.getInedges().isEmpty()) {
            return;
        }

        for(Task task : t.getInedges()) {
            if(task.getLateststart() > t.getLateststart() - task.getTime()) {
                task.setLatest(t.getLateststart() - task.getTime());
            }

            setLateststart(task);
        }
    }

// ****************************************************************************
// Metode som sjekker om filen/treet/grafen vi lager har en cycle i seg.
    public boolean cycle(Task t) {
        if(hasLoop) {
            return true;
        }

        if(path.contains(t)) {
            hasLoop = true;

            System.out.print("Fant en loop, tallet er ID:\n[");
            for(int i = path.indexOf(t); i < path.size(); i++) {
                System.out.print(path.get(i).getID() + " --> ");
            }
            System.out.println(t.getID() + "]");

            return hasLoop;
        }

        path.add(t);
        if(t.getOutedges().isEmpty()) {
            for(int i = path.indexOf(t); i < path.size(); i++) {
                path.remove(i);
            }

            return hasLoop;
        }

        for(Task t2 : t.getOutedges()) {
            cycle(t2);
        }
        for(int i = path.indexOf(t); i < path.size(); i++) {
            path.remove(i);
        }

        return hasLoop;
    }

// ****************************************************************************
// Printer ut
    public void printALL() {
        int staff = 0;

        for(int timecounter = 0; timecounter <= endTime; timecounter++) {
            boolean timePrinted = false;

            for(Task t : allTasks) {
                if(t.getEarliestStart() + t.getTime() == timecounter) {
                    if(!timePrinted) {
                        timePrinted = true;
                        System.out.printf("Time: %d\n", timecounter);
                    }
                 System.out.printf("               Finished: %d\n", t.getID());
                    staff -= t.getStaff();
                }
            }
            for(Task t : allTasks) {
                if(t.getEarliestStart() == timecounter) {
                    if(!timePrinted) {
                        timePrinted = true;
                        System.out.printf("Time: %d\n", timecounter);
                    }
                 System.out.printf("               Starting: %d\n", t.getID());
                    staff += t.getStaff();
                }
            }

            if(timePrinted && timecounter != endTime) {
                System.out.printf("          Current staff: %d\n\n", staff);
            }
        }

        System.out.printf("\n**** Shortest possible project excecution " +
                          "is %d ****\n\n\n", endTime);
        System.out.println("____________________________________________\n");

        for(Task t : allTasks) {
            System.out.printf("ID:                   |%d\n", t.getID());
            System.out.printf("Navn:                 |%s\n", t.getName());
            System.out.printf("Tid som trengs:       |%d\n", t.getTime());
            System.out.printf("Manpower som trengs:  |%d\n", t.getStaff());
            System.out.printf("Tidligst startstid:   |%d\n",
                               t.getEarliestStart());
            System.out.printf("Senest startstid:     |%d\n",
                               t.getLateststart());
            System.out.printf("Slack:                |%d\n", t.getSlack());
            System.out.printf("Avhengige tasks (ID): |");

            if(!t.getOutedges().isEmpty()) {
                for(Task k : t.getOutedges()) {
                    System.out.printf("%d. ", k.getID());
                }
            }
            else {
                System.out.printf("Ingen.");
            }

        System.out.println("\n____________________________________________\n");
        }

 System.out.println("\n**************** PROGRAM AVSLUTTES ****************\n");
    }
}
