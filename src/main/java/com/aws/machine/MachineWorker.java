/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2022 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     CodeBox (Rob Dawson).
 *     Hazendaz (Jeremy Landis).
 */
package com.aws.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The Class MachineWorker.
 */
public class MachineWorker {

    /** The machine list. */
    // TODO This will need managing to clear
    List<Machine> machineList = new ArrayList<>();

    /** The task list. */
    // Note this is expected created through property file loading it at startup but created in main for convenience
    List<Task> taskList = new ArrayList<>();

    /**
     * Gets the machine LRU.
     *
     * @return the machine LRU
     */
    public int getMachineLRU() {
        Machine machine;
        long lru = Long.MAX_VALUE;
        int machineId = -1;
        for (int x = 0; x < machineList.size(); x++) {
            machine = machineList.get(x);
            // Check LRU and capacity constraints on in use machines
            if (machine.getCapacity() > machine.getTasks().size() && machine.getLastUsed() < lru) {
                machineId = machine.getMachineId();
                lru = machine.getLastUsed();
                if (lru == -1) {
                    // Brand new, take it and exit
                    break;
                }
            }
        }
        return machineId;
    }

    /**
     * Adds the machine.
     *
     * @param machineId
     *            the machine id
     * @param capacity
     *            the capacity
     */
    public void addMachine(final int machineId, final int capacity) {
        if (machineId < 1 || capacity < 1) {
            // TODO Add proper error handling
            System.exit(-1);
        }
        final Machine machine = new Machine();
        machine.setMachineId(machineId);
        machine.setCapacity(capacity);
        machine.setLastUsed(-1);
        machineList.add(machine);
    }

    /**
     * Adds the machine task.
     *
     * @param machineId            the machine id
     * @param task            the task
     * @throws InterruptedException the interrupted exception
     */
    public void addMachineTask(final int machineId, final int task) throws InterruptedException {
        if (task < 1) {
            // TODO Add proper error handling
            System.exit(-1);
        }

        // Find task
        int foundTask = -1;
        for (int x = 0; x < taskList.size(); x++) {
            if (taskList.get(x).getTaskId() == task) {
                foundTask = task;
                break;
            }
        }

        // Check that task found
        if (foundTask == -1) {
            // TODO Add proper error handling
            System.exit(-1);
        }

        // general idea here to get machine id from the list
        Machine machine;
        for (int x = 0; x < machineList.size(); x++) {
            machine = machineList.get(x);
            if (machine.getMachineId() == machineId && machine.getCapacity() > machine.getTasks().size()) {
                machine.getTasks().add(foundTask);
                machine.setLastUsed(System.currentTimeMillis());
            }
        }

        // Sleep so time changes
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * The main method.
     *
     * @param args            the arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        // Create machine worker
        MachineWorker worker = new MachineWorker();

        // Create tasks
        worker.taskList.add(new Task(1, 100));
        worker.taskList.add(new Task(2, 100));
        worker.taskList.add(new Task(3, 100));
        worker.taskList.add(new Task(4, 100));
        worker.taskList.add(new Task(5, 100));
        worker.taskList.add(new Task(6, 100));
        worker.taskList.add(new Task(7, 100));
        worker.taskList.add(new Task(8, 100));
        worker.taskList.add(new Task(9, 100));
        worker.taskList.add(new Task(10, 100));

        // Add machines
        worker.addMachine(1, 5);
        worker.addMachine(2, 5);

        // Add machine tasks to 4 of max 5
        worker.addMachineTask(worker.getMachineLRU(), 1);
        worker.addMachineTask(worker.getMachineLRU(), 2);
        worker.addMachineTask(worker.getMachineLRU(), 3);
        worker.addMachineTask(worker.getMachineLRU(), 4);
        worker.addMachineTask(worker.getMachineLRU(), 5);
        worker.addMachineTask(worker.getMachineLRU(), 6);
        worker.addMachineTask(worker.getMachineLRU(), 7);
        worker.addMachineTask(worker.getMachineLRU(), 8);
        worker.addMachineTask(worker.getMachineLRU(), 9);
        worker.addMachineTask(worker.getMachineLRU(), 10);

        // Print output
        System.out.println(worker.machineList);
    }

}
