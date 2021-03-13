package jobshop.solvers;

import java.util.ArrayList;
import java.util.List;

import jobshop.Solver;
import jobshop.solvers.DescentSolver.*;
import static jobshop.solvers.DescentSolver.blocksOfCriticalPath;

import jobshop.Instance;
import jobshop.Result;
import jobshop.Schedule;

import jobshop.encodings.ResourceOrder;
import jobshop.encodings.Task;

public class TabouSolver extends DescentSolver {

    @Override
    public Result solve(Instance instance, long stop_time) {
		
		int nb_iter = 0 ;
        int max_iter = 30 ;
        int[][] taboo_solution = new int[instance.numTasks*instance.numJobs][instance.numJobs*instance.numTasks] ;

        Schedule best_sol = new DescentSolver().solve(instance, stop_time).schedule;


        ResourceOrder current_RO= new ResourceOrder(best_sol) ;
        ResourceOrder best_RO = current_RO.copy() ;

        int best_makespan;

        while((nb_iter <= max_iter) && (stop_time - System.currentTimeMillis() > 1)) {
            nb_iter++ ;

            Task t1;
            Task t2;
            List<Task> task_list = current_RO.toSchedule().criticalPath() ;
            List<Swap> swap_list = new ArrayList<>() ;
            List<Block> block_list = blocksOfCriticalPath(current_RO) ;
            for(int block = 0; block < block_list.size(); block++) {
                swap_list.addAll(neighbors(block_list.get(block))) ;
            }
			
            int best_swap_id = -1;
            best_makespan = Integer.MAX_VALUE ;

            for(int swap = 0; swap < swap_list.size(); swap++) 
				{
                ResourceOrder test_RO = current_RO.copy() ;
                Swap current_swap = swap_list.get(swap) ;

                if (current_swap.t1 < task_list.size())
				{
                    t1 = task_list.get(current_swap.t1);
                } 
					else 
				{ 
                    t1 = null;
                }


                if (current_swap.t2 < task_list.size())
				{
                    t2 = task_list.get(current_swap.t2);
                } 
					else 
				{ 
                    t2 = null;
                }

                current_swap.applyOn(test_RO) ;

                //is better result ?
                if ((t1 == null) || (t2 == null))
				{
                    break;
				} 
					else if ((test_RO.toSchedule().makespan() < best_makespan) && (nb_iter >= taboo_solution[t1.job * instance.numTasks + t1.task][t2.job * instance.numTasks + t2.task])) 
				{
                        best_makespan = test_RO.toSchedule().makespan();
                        best_RO = test_RO.copy();
                        best_swap_id = swap;
				}
                }


            if  (best_swap_id != -1) {
                current_RO = best_RO.copy();
                //get best solution
                Swap best_swap = swap_list.get(best_swap_id);
                t1 = task_list.get(best_swap.t1);
                t2 = task_list.get(best_swap.t2);
                taboo_solution[t2.job * instance.numTasks + t2.task][t1.job * instance.numTasks + t1.task] = nb_iter + 2;
            }

        }
        return new Result(instance, best_RO.toSchedule(), Result.ExitCause.Timeout) ;
    }
}

