package jobshop.solvers;

public enum EnumPriority {
    SPT, //(Shortest Processing Time) : gives priority to the shortest task
    LRPT, //(Longest Remaining Processing Time) : gives priority to the task belonging to the job with the highest duration
    EST_SPT, //SPT + restriction to tasks that can start as soon as possible
    EST_LRPT //EST + restriction to tasks that can start as soon as possible
}
