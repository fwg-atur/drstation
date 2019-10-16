package com.dcdt.doctorstation.entity;

public class ProblemInfo {

    private int problem_level;

    private String problem_level_name;

    private String problem_type;

    public int getProblem_level() {
        return problem_level;
    }

    public void setProblem_level(int problem_level) {
        this.problem_level = problem_level;
    }

    public String getProblem_level_name() {
        return problem_level_name;
    }

    public void setProblem_level_name(String problem_level_name) {
        this.problem_level_name = problem_level_name;
    }

    public String getProblem_type() {
        return problem_type;
    }

    public void setProblem_type(String problem_type) {
        this.problem_type = problem_type;
    }
}
