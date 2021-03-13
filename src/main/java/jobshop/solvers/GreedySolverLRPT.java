package jobshop.solvers;

import jobshop.Solver;
import jobshop.Result;
import jobshop.Instance;
import jobshop.encodings.Task;
import jobshop.encodings.JobNumbers;

import java.util.ArrayList;

public class GreedySolverLRPT implements Solver {


    //Date of release of the machines
    private int[] machines_release;
    //Instance for solve method
    private Instance instance;
    //Remaining time for jobs, for  LRPT
    private int[][] remaining_time;
    //Array of all tasks that have not yet been processed by the machines
    private ArrayList<Task> tasks;

    //Sorting difference according to priority
    private Task best_task(ArrayList<Task> tasks) {

        ArrayList<Task> resultat = new ArrayList<Task>();
        Task taskN1 = tasks.get(0);
		
                for (int i = 1; i < tasks.size(); i++) {
                    Task current_task = tasks.get(i);

                    //Calculation time not yet done but initialized in solve()
                    if (this.remaining_time[current_task.job][current_task.task] == -1) {
                        this.remaining_time[current_task.job][current_task.task] = 0;
                        //Sum of all tasks
                        for (int tache = current_task.task; tache < this.instance.numTasks; tache++) {
                            this.remaining_time[current_task.job][current_task.task] += this.instance.duration(current_task.job, tache);
                        }
                    }

                    if (this.remaining_time[taskN1.job][taskN1.task] < this.remaining_time[current_task.job][current_task.task]) {
                        taskN1 = current_task;
                    }
                }
				
        return taskN1;
    }


    //This is the general resolution algorithm, identical whatever the priority, it is inherited from the Solver interface.
    //Priorities will be dealt with in another method that will order the tasks to be dealt with
    @Override
    public Result solve(Instance instance, long stop_time) {

        //INSTANTIATION OF THE PROBLEM
        this.instance = instance;
        this.machines_release = new int[this.instance.numMachines];
        //filling with 0's from the previously created table
        for (int i = 0; i < machines_release.length; i++) {
            machines_release[i] = 0;
        }
        //creation of the array of unrealized tasks with creation of the corresponding tasks
        this.tasks = new ArrayList<Task>();
        for (int job = 0; job < this.instance.numJobs; job++) {
            this.tasks.add(new Task(job, 0));
        }

		//Job/Task matrix initialization
		remaining_time = new int[this.instance.numJobs][this.instance.numTasks];
		//fill the [][] with -1
		for (int job = 0; job < this.instance.numJobs; job++) {
			for (int task = 0; task < this.instance.numTasks; task++) {
				remaining_time[job][task] = -1;
			}
		}

        JobNumbers solution = new JobNumbers(instance);

        //START ALGORITHM
        //In the first round, there are all the achievable tasks to be processed
        //At each loop, tasksOngoing are updated.
        //The algorithm stops when there are no more tasks to process, i.e. when the Array is empty.
        while (!(this.tasks.isEmpty())) {
            Task opt = this.best_task(this.tasks);

            //Inspired by BasicSolver
            solution.jobs[solution.nextToSet++] = opt.job;

            this.machines_release[this.instance.machine(opt.job, opt.task)] += this.instance.duration(opt.job, opt.task);

            //UPDATE
            this.tasks.remove(this.tasks.indexOf(opt));
            if (opt.task < this.instance.numTasks - 1) {
                // Next task is pending
                this.tasks.add(new Task(opt.job, opt.task + 1));
            }
        }

        //Cf class Result
        return new Result(instance, solution.toSchedule(), Result.ExitCause.Timeout);
    }




}
