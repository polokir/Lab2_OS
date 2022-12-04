// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.ArrayList;
import java.io.*;

public class SchedulingAlgorithm {
  private static int compTime = 0;

  private static int getCurrentProcess(ArrayList<Process> processVector) {
    int currentProcess=0;
    for (int i = 0; i < processVector.size(); i++)
      if (!processVector.get(i).isIOblocked && processVector.get(i).arrival <= compTime && !processVector.get(i).finished())
        currentProcess = i;
    if (processVector.isEmpty())
      return -1;
    Process process = processVector.get(currentProcess);
    for (int i = 0; i < processVector.size(); i++) {
      Process temp_process = processVector.get(i);
      if (!temp_process.isIOblocked && !temp_process.finished() && temp_process.remainingTime() < process.remainingTime()
              && compTime >= temp_process.arrival) {
        currentProcess = i;
      }
    }
    return currentProcess;
  }

  private static boolean increaseCompTime(ArrayList<Process> processVector, PrintStream out){
    compTime++;
    boolean arrivedProcess = false;

    for (int i = 0; i < processVector.size(); i++){
      if (processVector.get(i).arrival == compTime) {
        arrivedProcess = true;
        out.println("Process: " + i + " " + compTime + " has arrived... (" + processVector.get(i).cputime + " " + processVector.get(i).IOblocking + " " + processVector.get(i).cpudone + ")");
      }
    }

    return arrivedProcess;
  }

  private static boolean increaseIOTimers(ArrayList<Process> processVector, PrintStream out){
    boolean finishedProcess = false;

    for (int i = 0; i < processVector.size(); i++) {
      if (processVector.get(i).isIOblocked) {
        processVector.get(i).IOblocking_timer++;
        if (processVector.get(i).IOblocking_timer == processVector.get(i).IOblocking) {
          processVector.get(i).isIOblocked = false;
          processVector.get(i).IOblocking_timer = 0;
          finishedProcess = true;
          out.println("Process: " + i + " " + compTime +" unblocked... (" + processVector.get(i).cputime + " " +  processVector.get(i).IOblocking + " " + processVector.get(i).cpudone + ")");
        }
      }
    }

    return finishedProcess;
  }

  public static void Run(int runtime, ArrayList<Process> processVector, Results result) {
    int completed = 0;
    int currentProcess = 0;
    int size = processVector.size();
    Process process;
    String resultsFile = "./src/Summary-Processes.conf";
    result.schedulingType = "Batch";
    result.schedulingName = "Shortest remaining time first";

    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      currentProcess = getCurrentProcess(processVector);
      process = processVector.get(currentProcess);
      out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.IOblocking + " " + process.cpudone + " " + process.cpudone + ")");
      while (compTime < runtime) {
        if (currentProcess != -1 && ((process.cpudone == process.cputime && process.cpudone > 0) || process.cputime == 0)) {
          process.summaryTime = compTime;
          completed++;
          out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.IOblocking + " " + process.cpudone + " " + process.cpudone + ")");
          if (completed == size) {
            result.compuTime = compTime;
            out.close();
            return;
          }
          currentProcess = getCurrentProcess(processVector);
          if (currentProcess != -1) {
            process = processVector.get(currentProcess);
            out.println("Process: " + currentProcess + " " + compTime + " registered... (" + process.cputime + " " + process.IOblocking + " " + process.cpudone + ")");
          }
        }

        if (process.max_timer == process.max_time) {
          out.println("Process: " + currentProcess + " " + compTime + " I/O blocked... (" + process.cputime + " " + process.IOblocking + " " + process.cpudone + ")");
          if (process.IOblocking > 0) {
            process.isIOblocked = true;
            process.max_timer = 0;
          }
          currentProcess = getCurrentProcess(processVector);
          if (currentProcess != -1) {
            process = processVector.get(currentProcess);
            out.println("Process: " + currentProcess + " " + compTime + " registered... (" + process.cputime + " " + process.IOblocking + " " + process.cpudone + ")");
          }
        }
        if (currentProcess != -1) {
          process.cpudone++;
          process.max_timer++;
        }
        boolean isArrived = increaseCompTime(processVector,out);
        boolean isUnlocked = increaseIOTimers(processVector,out);

        if(isArrived || isUnlocked){
          currentProcess=getCurrentProcess(processVector);
          if(currentProcess !=-1){
            process = processVector.get(currentProcess);
            out.println("Process: " + currentProcess + " " + compTime + " registered... (" + process.cputime + " " + process.IOblocking + " " + process.cpudone + ")");
          }
        }
        //out.close();
      }
    } catch (IOException exception) {
        result.compuTime=compTime;
    }
  }
}