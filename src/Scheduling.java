// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import java.io.*;
import java.util.*;

public class Scheduling {

  private static int processnum = 3;
  private static int runtime = 1000;
  private static final ArrayList<Process> processVector = new ArrayList<>();
  private static final Results result = new Results("null","null",0);

  private static void Init(String file) throws IOException {
    File f = new File(file);
    String line;


      DataInputStream in = new DataInputStream(new FileInputStream(f));
      while ((line = in.readLine()) != null) {
        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          int cpu_time = Common.s2i(st.nextToken(" "));
          int burst_time = Common.s2i(st.nextToken(" "));
          int io_blocking_time = Common.s2i(st.nextToken(" "));
          int arrival_time = Common.s2i(st.nextToken(" "));
          processVector.add(new Process(cpu_time, burst_time, io_blocking_time, arrival_time));
        }
        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }
      }
      in.close();

  }

  public static void debug() {
    int size = processVector.size();
    for (int i = 0; i < size; i++) {
      Process process = processVector.get(i);
    }
    System.out.println("runtime " + runtime);
  }

  public static void main(String[] args) {
    /*args[0] = new String("scheduling.conf");
    if (args.length != 1) {
      System.out.println("Usage: 'java Scheduling <INIT FILE>'");
      System.exit(-1);
    }
    File f = new File(args[0]);
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }*/
    System.out.println("Working...");
    try {
      Init("./src/scheduling.conf");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    SchedulingAlgorithm.Run(runtime, processVector, result);
    try {
      String resultsFile = "./src/Summary-Results";
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Process #\tCPU Time\tCPU Completed\tArrival time\tAll time");
      for (int i = 0; i < processVector.size(); i++) {
        Process process = processVector.get(i);

        out.print(i);

        if (i < 100) {
          out.print("\t\t\t");
        } else {
          out.print("\t\t");
        }

        out.print(process.cputime);

        if (process.cputime < 100)
          out.print(" (ms)\t\t");
        else
          out.print(" (ms)\t");

        out.print(process.cpudone);

        if (process.cpudone < 100)
          out.print(" (ms)\t\t\t");
        else
          out.print(" (ms)\t\t");

        out.print(process.arrival);

        if (process.arrival > 10)
          out.print(" (ms)\t\t");
        else
          out.print(" (ms)\t\t\t");

        out.println(process.summaryTime);
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    System.out.println("Completed.");
  }
}
