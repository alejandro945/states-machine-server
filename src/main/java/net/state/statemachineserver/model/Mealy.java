package net.state.statemachineserver.model;
import java.util.ArrayList;

public class Mealy {

    private String[] states, inputSymbols;
    private String[][] successors, outputSymbols;

    public Mealy(String[][] machine) {
        machine = connected(machine);
        fillMealy(machine);
    }

    private String[][] connected(String[][] machine) {
        ArrayList<Integer> included = connectedStates(machine);
        return connectedMachine(machine, included);
    }

    private String[][] connectedMachine(String[][] machine, ArrayList<Integer> included) {
        String[][] connected = new String[included.size() + 1][machine[0].length];
        int additional = 0;
        connected[0] = machine[0];
        for (int i = 1; i < machine.length; i++) {
            if (indexOfList(included, i - 1) == -1)
                additional++;
            else
                connected[i - additional] = machine[i];
        }
        return connected;
    }

    private ArrayList<Integer> connectedStates(String[][] machine) {
        ArrayList<Integer> included = new ArrayList<>();
        included.add(0);
        for (int i = 0; i < included.size(); i++) {
            for (int j = 1; j < machine[0].length; j++) {
                int index = machine[included.get(i) + 1][j].charAt(0) - 'A';
                if (indexOfList(included, index) == -1)
                    included.add(index);
            }
        }
        return included;
    }

    private int indexOfList(ArrayList<Integer> list, int num) {
        int index = -1;
        for (int i = 0; i < list.size() && (index == -1); i++)
            index = list.get(i) == num ? i : -1;
        return index;
    }

    private int indexState(int index) {
        for (int i = 0; i < states.length; i++) {
            if (states[i].charAt(0) - 'A' == index)
                return i;
        }
        return -1;
    }

    public String[][] partitioning(boolean format) {
        ArrayList<ArrayList<Integer>> groups = step2A();
        boolean stop = false;
        while (!stop) {
            ArrayList<ArrayList<Integer>> subGroups1 = step2B(groups), subGroups2 = step2B(subGroups1);
            stop = isTheSame(subGroups1, subGroups2);
            groups = subGroups2;
        }
        String[][] minimize = format(groups, format);
        return minimize;
    }

    private String[][] format(ArrayList<ArrayList<Integer>> groups, boolean format) {
        String[][] minimize = new String[groups.size() + 1][inputSymbols.length + 1];
        minimize[0][0] = format ? "Blocks" : "New Names";
        for (int j = 0; j < inputSymbols.length; j++) {
            minimize[0][j + 1] = inputSymbols[j];
        }
        minimize = format ? intermediateFormat(groups, minimize) : finalFormat(groups, minimize);
        return minimize;
    }

    private String[][] finalFormat(ArrayList<ArrayList<Integer>> groups, String[][] minimize) {
        for (int i = 0, j = groups.size() - 1; i < groups.size(); i++, j--) {
            minimize[i + 1][0] = (char) ('Z' - j) + "";
        }
        String[][] blockSuccessors = new String[groups.size()][inputSymbols.length];
        for (int g = 0; g < groups.size(); g++) {
            ArrayList<Integer> group = groups.get(g);
            for (int j = 0; j < inputSymbols.length; j++) {
                int index = indexOfGroup(groups, indexState(successors[group.get(0)][j].charAt(0) - 65));
                blockSuccessors[g][j] = (char) ('Z' - (groups.size() - 1) + index) + "";
            }
        }
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            for (int j = 0; j < inputSymbols.length; j++)
                minimize[i + 1][j + 1] = blockSuccessors[i][j] + ", " + outputSymbols[group.get(0)][j];
        }
        return minimize;
    }

    private String[][] intermediateFormat(ArrayList<ArrayList<Integer>> groups, String[][] minimize) {
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            for (int j = 0; j < group.size(); j++) {
                char s = states[group.get(j)].charAt(0);
                if (minimize[i + 1][0] == null)
                    minimize[i + 1][0] = j == group.size() - 1 ? "{" + s + "}" : "{" + s + ", ";
                else
                    minimize[i + 1][0] += j == group.size() - 1 ? s + "}" : s + ", ";
            }
        }
        String[][] blockSuccessors = new String[groups.size()][inputSymbols.length];
        for (int g = 0; g < groups.size(); g++) {
            ArrayList<Integer> group = groups.get(g);
            for (int s = 0; s < group.size(); s++) {
                for (int j = 0; j < inputSymbols.length; j++) {
                    String sOld = blockSuccessors[g][j];
                    String sNew = successors[group.get(s)][j];
                    if (sOld == null)
                        blockSuccessors[g][j] = sNew;
                    else
                        blockSuccessors[g][j] = sOld.contains(sNew) ? sOld : sOld + "," + sNew;
                }
            }
        }
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            for (int j = 0; j < inputSymbols.length; j++)
                minimize[i + 1][j + 1] = "{" + blockSuccessors[i][j] + "}, " + outputSymbols[group.get(0)][j];
        }
        return minimize;
    }

    
    private boolean isTheSame(ArrayList<ArrayList<Integer>> subGroups1, ArrayList<ArrayList<Integer>> subGroups2) {
        boolean stop = subGroups1.size() == subGroups2.size() ? false : true;
        for (int i = 0; i < subGroups1.size() && !stop; i++) {
            ArrayList<Integer> subGroup1 = subGroups1.get(i), subGroup2 = subGroups2.get(i);
            stop = subGroup1.size() == subGroup2.size() ? false : true;
            for (int j = 0; j < subGroup1.size() && !stop; j++)
                stop = subGroup1.get(j) == subGroup2.get(j) ? false : true;
        }
        return !stop;
    }

  
    private ArrayList<ArrayList<Integer>> step2A() {
        ArrayList<ArrayList<Integer>> groups = new ArrayList<>();
        for (int i = 0; i < outputSymbols.length; i++) {
            for (int j = i + 1; j < outputSymbols.length; j++) {
                boolean stop = false;
                for (int k = 0; k < outputSymbols[0].length && !stop; k++) {
                    if (!outputSymbols[i][k].equals(outputSymbols[j][k]))
                        stop = true;
                }
                if (!stop) {
                    int iGroup = indexOfGroup(groups, i), jGroup = indexOfGroup(groups, j);
                    if (groups.size() == 0 || (iGroup == -1 && jGroup == -1)) {
                        ArrayList<Integer> group = new ArrayList<Integer>();
                        group.add(i);
                        group.add(j);
                        groups.add(group);
                    } else {
                        if (iGroup != -1 && jGroup == -1)
                            groups.get(iGroup).add(j);
                        if (iGroup == -1 && jGroup != -1)
                            groups.get(jGroup).add(i);
                    }
                }
            }
            if (indexOfGroup(groups, i) == -1) {
                ArrayList<Integer> group = new ArrayList<>();
                group.add(i);
                groups.add(group);
            }
        }
        return groups;
    }

    private ArrayList<ArrayList<Integer>> step2B(ArrayList<ArrayList<Integer>> groups) {
        ArrayList<ArrayList<Integer>> subGroups = new ArrayList<>();
        for (int g = 0; g < groups.size(); g++) {
            ArrayList<Integer> group = groups.get(g);
            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    boolean stop = false;
                    for (int k = 0; k < successors[0].length && !stop; k++) {
                        int i1 = indexState(successors[group.get(i)][k].charAt(0) - 65);
                        int i2 = indexState(successors[group.get(j)][k].charAt(0) - 65);
                        if (!(indexOfGroup(groups, i1) == indexOfGroup(groups, i2)))
                            stop = true;
                    }
                    int iGroup = indexOfGroup(subGroups, group.get(i)), jGroup = indexOfGroup(subGroups, group.get(j));
                    if (!stop) {
                        if (iGroup == -1 && jGroup == -1) {
                            ArrayList<Integer> subGroup = new ArrayList<>();
                            subGroup.add(group.get(i));
                            subGroup.add(group.get(j));
                            subGroups.add(subGroup);
                        } else {
                            if (iGroup == -1 && jGroup != -1)
                                subGroups.get(jGroup).add(group.get(i));
                            if (iGroup != -1 && jGroup == -1)
                                subGroups.get(iGroup).add(group.get(j));
                        }
                    }
                }
                if (indexOfGroup(subGroups, group.get(i)) == -1) {
                    ArrayList<Integer> subGroup = new ArrayList<>();
                    subGroup.add(group.get(i));
                    subGroups.add(subGroup);
                }
            }
        }
        return subGroups;
    }


    private int indexOfGroup(ArrayList<ArrayList<Integer>> groups, int num) {
        int index = -1;
        boolean exists = false;
        for (int i = 0; i < groups.size() && !exists; i++) {
            ArrayList<Integer> group = groups.get(i);
            for (int j = 0; j < group.size() && !exists; j++) {
                if (group.get(j) == num) {
                    exists = true;
                    index = i;
                }
            }
        }
        return index;
    }

    
    private void fillMealy(String[][] machine) {
        inputSymbols(machine);
        states(machine);
        int rows = machine.length;
        int columns = machine[0].length;
        String[][] successors = new String[rows - 1][columns - 1];
        String[][] outputSymbols = new String[rows - 1][columns - 1];
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < columns; j++) {
                String[] symbols = machine[i][j].split(",");
                successors[i - 1][j - 1] = symbols[0];
                outputSymbols[i - 1][j - 1] = symbols[1];
            }
        }
        this.successors = successors;
        this.outputSymbols = outputSymbols;
    }

    private void inputSymbols(String[][] machine) {
        int length = machine[0].length;
        String[] inputSymbols = new String[length - 1];
        for (int i = 1; i < length; i++) {
            inputSymbols[i - 1] = machine[0][i];
        }
        this.inputSymbols = inputSymbols;
    }

    private void states(String[][] machine) {
        int length = machine.length;
        String[] states = new String[length - 1];
        for (int i = 1; i < length; i++) {
            states[i - 1] = machine[i][0];
        }
        this.states = states;
    }
}