public class Process {
  public int cputime;
  public int arrival;
  public int cpudone=0;
  public int max_time;
  public int max_timer=0;
  public int IOblocking;

  public int IOblocking_timer=0;
  public boolean isIOblocked=false;
  public int summaryTime;




  public Process(int cputime,int max_time,int IOblocking,int arrival) {
    this.cputime=cputime;
    this.max_time=max_time;
    this.IOblocking=IOblocking;
    this.arrival=arrival;
  }
  public void print(int number){
    System.out.println("process " + number + " " + cputime + " " + IOblocking + " " + max_time + " " + " " + arrival + " ");
  }
  public int remainingTime(){
    return cputime - cpudone;
  }

  public boolean finished(){
    return cputime == cpudone;
  }
}
