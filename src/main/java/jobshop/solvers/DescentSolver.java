package jobshop.solvers;

import jobshop.Instance;
import jobshop.Result;
import jobshop.Solver;
import jobshop.Schedule;
import jobshop.encodings.Task;
import jobshop.encodings.ResourceOrder;
import java.util.ArrayList;
import java.util.List;

public class DescentSolver implements Solver {

    /** A block represents a subsequence of the critical path such that all tasks in it execute on the same machine.
     * This class identifies a block in a ResourceOrder representation.
     *
     * Consider the solution in ResourceOrder representation
     * machine 0 : (0,1) (1,2) (2,2)
     * machine 1 : (0,2) (2,1) (1,1)
     * machine 2 : ...
     *
     * The block with : machine = 1, firstTask= 0 and lastTask = 1
     * Represent the task sequence : [(0,2) (2,1)]
     *
     * */
    static class Block {
        /** machine on which the block is identified */
        final int machine;
        /** index of the first task of the block */
        final int firstTask;
        /** index of the last task of the block */
        final int lastTask;

        Block(int machine, int firstTask, int lastTask) {
            this.machine = machine;
            this.firstTask = firstTask;
            this.lastTask = lastTask;
        }
    }

    /**
     * Represents a swap of two tasks on the same machine in a ResourceOrder encoding.
     *
     * Consider the solution in ResourceOrder representation
     * machine 0 : (0,1) (1,2) (2,2)
     * machine 1 : (0,2) (2,1) (1,1)
     * machine 2 : ...
     *
     * The swam with : machine = 1, t1= 0 and t2 = 1
     * Represent inversion of the two tasks : (0,2) and (2,1)
     * Applying this swap on the above resource order should result in the following one :
     * machine 0 : (0,1) (1,2) (2,2)
     * machine 1 : (2,1) (0,2) (1,1)
     * machine 2 : ...
     */
    static class Swap {
        // machine on which to perform the swap
        final int machine;
        // index of one task to be swapped
        final int t1;
        // index of the other task to be swapped
        final int t2;

        Swap(int machine, int t1, int t2) {
            this.machine = machine;
            this.t1 = t1;
            this.t2 = t2;
        }

        /** Apply this swap on the given resource order, transforming it into a new solution. */
        public void applyOn(ResourceOrder order) {
            Task stockage = order.tasksByMachine[this.machine][t1];
            order.tasksByMachine[this.machine][t1] = order.tasksByMachine[this.machine][t2];
            order.tasksByMachine[this.machine][t2] = stockage;
        }
    }


    @Override
    public Result solve(Instance instance, long deadline) {

        // Schedule best_sol = new GreedySolver(GreedySolver.enumPriority.EST_LRPT).solve(instance, deadline).schedule;
        // Schedule best_sol = new GreedySolverEST_LRPT().solve(instance, deadline).schedule;
        // Schedule best_sol = new GreedySolverEST_SPT().solve(instance, deadline).schedule;
        // Schedule best_sol = new GreedySolverLRPT().solve(instance, deadline).schedule;
        Schedule best_sol = new GreedySolverSPT().solve(instance, deadline).schedule;



        List<Block> blocks = blocksOfCriticalPath(new ResourceOrder(best_sol));

        boolean no_improve = false;
        ResourceOrder best_neighbor = null;

        while (!no_improve && deadline - System.currentTimeMillis() > 1) {
            //starting best neighbor research
            for (int block = 0; block < blocks.size(); block++) {
                List<Swap> Neighbors = neighbors(blocks.get(block));
                for (Swap swap : Neighbors) {
                    //neighbor of the best solution
                    ResourceOrder Neighbor = new ResourceOrder(best_sol);
                    swap.applyOn(Neighbor);
                    Schedule neighbor_scheduling = Neighbor.toSchedule();
                    no_improve = true;
                    //copy neigbhbor in best_neighbor if better than the previous one
                    if (best_neighbor == null) {
                        best_neighbor = Neighbor.copy();
                    } else {
                        if (neighbor_scheduling.makespan() < best_neighbor.toSchedule().makespan()) {
                            best_neighbor = Neighbor.copy();
                        }
                    }
                }
                // end of research
                if (best_neighbor.toSchedule().makespan() < best_sol.makespan()) {
                    best_sol = best_neighbor.toSchedule();
                    best_neighbor = null;
                    no_improve = false;

                }
            }
       }
        return new Result(instance, best_sol, Result.ExitCause.Timeout);
    }

    /** Returns a list of all blocks of the critical path. */
    static List<Block> blocksOfCriticalPath(ResourceOrder order) {

        ArrayList<Block> blocks_list = new ArrayList<Block>();
        List<Task> crit_path = order.toSchedule().criticalPath();

        Task task_crit_path = crit_path.get(0);
        Task start_task = new Task(task_crit_path.job,task_crit_path.task);


        int machine_number = order.instance.machine(task_crit_path.job,task_crit_path.task);
        int number_of_tasks = 1;

        for (int i = 1; i < crit_path.size(); i++){
            task_crit_path = crit_path.get(i);
            //next task same machine
            if(machine_number == order.instance.machine(task_crit_path.job,task_crit_path.task)){
                number_of_tasks++;
            }else{
                // not same machine but min 2 consecutive jobs
                if(number_of_tasks >= 2){
                    int cpt = 0;

                    for(int j = 0; j < order.tasksByMachine[machine_number].length; j++){
                        if(start_task.equals(order.tasksByMachine[machine_number][j])){
                            cpt = j;
                        }
                    }
                    int end_task = cpt + number_of_tasks - 1;
                    blocks_list.add(new Block(machine_number,cpt,end_task));
                }
                number_of_tasks = 1;
                start_task = new Task(task_crit_path.job,task_crit_path.task);
                machine_number = order.instance.machine(task_crit_path.job,task_crit_path.task);
            }
        }
        // end of the critical path
        // there were at least 2 consecutive tasks in it
        if(number_of_tasks >= 2){
            int cpt = 0;

            for(int j = 0; j < order.tasksByMachine[machine_number].length; j++){
                if(start_task.equals(order.tasksByMachine[machine_number][j])){
                    cpt = j;
                }
            }
            int end_task = cpt + number_of_tasks - 1;
            blocks_list.add(new Block(machine_number,cpt,end_task));
        }
        return blocks_list;
    }

    /** For a given block, return the possible swaps for the Nowicki and Smutnicki neighborhood */
    List<Swap> neighbors(Block block) {

        List<Swap> neighbors_list = new ArrayList<Swap>();

            neighbors_list.add(new Swap(block.machine, block.firstTask,	block.firstTask + 1));
            neighbors_list.add(new Swap(block.machine, block.lastTask, 	block.lastTask - 1));

        return neighbors_list;
    }

}
